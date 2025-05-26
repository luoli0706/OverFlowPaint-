package com.example.overflowpaint.controller;

import com.example.overflowpaint.dto.BoardRequestDto;
import com.example.overflowpaint.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
/*
棋盘控制器
负责转发来自前端的JSON至后端*
* */
@RestController
public class BoardController {

    @Autowired
    private BoardService boardService;
/*
* processBoard方法，执行
* 接受来自BoardRequestDto封装的棋盘信息，并将其提取至堆栈，然后执行逻辑计算后返回响应，响应在DTO层封装回JSON
* */
    @PostMapping("/board")
    public ResponseEntity<?> processBoard(@RequestBody BoardRequestDto request) {
        try {
            int[][] board = request.getBoard().stream()
                    .map(row -> row.stream().mapToInt(Integer::intValue).toArray())
                    .toArray(int[][]::new);//提取请求体携带的JSON，将棋盘信息序列化至二维数组、
            //提取主要的参数，包括点击棋盘格的X，Y信息，与被填充前的颜色和填充颜色
            int x = request.getX();
            int y = request.getY();
            int oldColor = request.getOldColor();
            int newColor = request.getNewColor();

            return ResponseEntity.ok(boardService.processBoard(board, x, y, oldColor, newColor));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器处理请求时出现错误: " + e.getMessage());
        }
    }
}
