package com.example.overflowpaint.config;

import com.example.overflowpaint.service.BoardService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/*
* 在程序退出的时候，将数据库记录的棋盘内容清空
* */
@Component
public class DatabaseCleanupOnShutdown implements DisposableBean {

    @Autowired
    private BoardService boardService;

    @Override
    public void destroy() throws Exception {
        boardService.clearBoardTable();
    }
}
