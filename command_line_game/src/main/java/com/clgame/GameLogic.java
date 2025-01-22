package com.clgame;

public abstract class GameLogic {
    protected Board board;

    public GameLogic(Board board) {
        this.board = board;
    }

    public abstract void processMove(char move);
    public abstract boolean isGameOver();
}
