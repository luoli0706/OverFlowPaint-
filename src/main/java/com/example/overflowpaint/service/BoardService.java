package com.example.overflowpaint.service;

import com.example.overflowpaint.model.Board;
import com.example.overflowpaint.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;
/*
* 用于棋盘数据的逻辑处理
* */
@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;
    //日志，便于调试问题所在
    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);
//核心方法，将棋盘数据拷贝至数据库，并执行四连通区域填充
    @Transactional
    public int[][] processBoard(int[][] board, int x, int y, int oldColor, int newColor) {
        // 保存棋盘状态到数据库
        logger.info("收到棋盘数据，尺寸: {}x{}", board.length, board[0].length);
        logger.info("坐标: ({}, {}), 旧颜色: {}, 新颜色: {}", x, y, oldColor, newColor);
        try {
            Board boardEntity = new Board();
            boardEntity.setBoardState(board);
            boardRepository.save(boardEntity);
        } catch (Exception e) {
            logger.error("保存棋盘状态到数据库时出错: ", e);
            // 可以根据具体需求进行回滚或其他处理
        }

        // 四连通区域填充算法
        int[][] processedBoard = floodFill(board, x, y, oldColor, newColor);

        return processedBoard;
    }
    //四连通区域填充算法实现
    private int[][] floodFill(int[][] board, int x, int y, int oldColor, int newColor) {
        int rows = board.length;
        //特别校验，防止越界
        int cols = (rows ==0)?0:board[0].length;

        // 检查起始坐标是否合法
        if (y < 0 || y >= rows || x < 0 || x >= cols || board[y][x] != oldColor) {
            logger.warn("无效坐标：({}, {})，棋盘尺寸：{}x{}", x, y, rows, cols);
            return board;
        }

        boolean[][] visited = new boolean[rows][cols];
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{y, x}); // 队列中存储 [行, 列]
        visited[y][x] = true;
        board[y][x] = newColor;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int currentRow = current[0];
            int currentCol = current[1];

            for (int[] dir : directions) {
                int newRow = currentRow + dir[0];
                int newCol = currentCol + dir[1];

                // 检查新坐标是否合法
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols
                        && !visited[newRow][newCol] && board[newRow][newCol] == oldColor) {

                    board[newRow][newCol] = newColor;
                    visited[newRow][newCol] = true;
                    queue.offer(new int[]{newRow, newCol});
                }
            }
        }
        return board;
    }
    // 新增方法：清空 Board 表记录
    public void clearBoardTable() {
        try {
            boardRepository.deleteAll();
            logger.info("数据库表 Board 的记录已清空");
        } catch (Exception e) {
            logger.error("清空数据库表 Board 的记录时出错: ", e);
        }
    }
}