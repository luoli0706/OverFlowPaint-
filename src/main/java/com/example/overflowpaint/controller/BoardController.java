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

@RestController
public class BoardController {

    @Autowired
    private BoardService boardService;

    @PostMapping("/board")
    public ResponseEntity<?> processBoard(@RequestBody BoardRequestDto request) {
        try {
            int[][] board = request.getBoard().stream()
                    .map(row -> row.stream().mapToInt(Integer::intValue).toArray())
                    .toArray(int[][]::new);
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
