package com.example.overflowpaint.dto;

import java.util.ArrayList;
import java.util.List;

public class BoardRequestDto {
    private ArrayList<List<Integer>> board;
    private int x;
    private int y;
    private int oldColor;
    private int newColor;

    // Getters and Setters
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
    }
}
