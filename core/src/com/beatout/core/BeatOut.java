package com.beatout.core;

import com.badlogic.gdx.Gdx;

public class BeatOut {

    private GameBoard gameBoard;

    public BeatOut(float width, float height) {
        this.gameBoard = new GameBoard(width, height);
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void update(float deltaTime) {
        // TODO: Add simulation of future trajectory and time planning
    }

    public void moveBat(float direction, float deltaTime) {
        gameBoard.moveBat(direction, deltaTime);
    }
}
