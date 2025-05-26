package com.example.overflowpaint.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
/*
* 封装棋盘数据
* */
@Getter
@Setter
public class BoardRequestDto {
    private ArrayList<List<Integer>> board;
    private int x;
    private int y;
    private int oldColor;
    private int newColor;

  /*  用Getters and Setters代替以下内容
    public ArrayList<List<Integer>> getBoard() {
        return board;
    }

    public void setBoard(ArrayList<List<Integer>> board) {
        this.board = board;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getOldColor() {
        return oldColor;
    }

    public void setOldColor(int oldColor) {
        this.oldColor = oldColor;
    }

    public int getNewColor() {
        return newColor;
    }

    public void setNewColor(int newColor) {
        this.newColor = newColor;
    }*/
}
