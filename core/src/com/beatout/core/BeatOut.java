package com.beatout.core;

import com.badlogic.gdx.Gdx;
import com.beatout.math.NoteValues;
import com.beatout.math.Vector;

public class BeatOut {

    private GameBoard gameBoard;

    private BallAnimator ballAnimator;
    private Trajectory currentTrajectory;
    private TimePlanner timePlanner;
    private TimePlan timePlan;
    private float time;

    public static String TEST_BEAT_EVENT = "testBeatoutEvent";

    public BeatOut(float width, float height) {
        this.gameBoard = new GameBoard(width, height);

        QuantizationTimePlanner planner = new QuantizationTimePlanner();
//        planner.setSmallestBeat(NoteValues.EIGHTH_NOTE);
        this.timePlanner = planner;

        currentTrajectory = gameBoard.calculateTrajectory();
        timePlan = timePlanner.getTimePlan(currentTrajectory);

        this.ballAnimator = new BallAnimator(gameBoard.getBall(), currentTrajectory, timePlan);
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void update(float deltaTime) {
        ballAnimator.update(deltaTime);
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
