package com.beatout.core;

import com.badlogic.gdx.Gdx;
import com.beatout.math.NoteValues;
import com.beatout.math.Vector;

public class BeatOut {

    private GameBoard gameBoard;

    private Trajectory currentTrajectory;
    private TimePlanner timePlanner;
    private TimePlan timePlan;
    private float time;

    public BeatOut(float width, float height) {
        this.gameBoard = new GameBoard(width, height);



        QuantizationTimePlanner planner = new QuantizationTimePlanner();
        planner.setSmallestBeat(NoteValues.EIGHTH_NOTE);
        this.timePlanner = planner;

//        currentTrajectory = gameBoard.calculateTrajectory();
//        timePlan = timePlanner.getTimePlan(currentTrajectory);
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

    public float getBallRadius() {
        return gameBoard.getBall().getRadius();
    }

    public Vector getBallPosition() {
        // TODO: Implement
        return null;
    }
}
