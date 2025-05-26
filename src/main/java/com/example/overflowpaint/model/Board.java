package com.example.overflowpaint.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Arrays;

@Data
@Entity
/*
实体类，用于将棋盘数据同步至数据库
* */
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String boardState;
//设置棋盘状态，即将反序列化后的二维数组序列化成JSON
    public void setBoardState(int[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            for (int cell : row) {
                sb.append(cell).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(";");
        }
        sb.deleteCharAt(sb.length() - 1);
        this.boardState = sb.toString();
    }
//获取棋盘状态，将数据库中的JSON反序列化为Java二维数组以便于逻辑运算
    public int[][] getBoardState() {
        String[] rows = boardState.split(";");
        int[][] board = new int[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            String[] cells = rows[i].split(",");
            board[i] = Arrays.stream(cells).mapToInt(Integer::parseInt).toArray();
        }
        return board;
    }
}
