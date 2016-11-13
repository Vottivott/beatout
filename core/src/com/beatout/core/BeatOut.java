package com.beatout.core;

import com.beatout.math.BeatOutMath;
import com.beatout.math.Vector;

public class BeatOut {

    private GameBoard gameBoard;

    private BallAnimator ballAnimator;
    private Trajectory currentTrajectory;
    private TimePlanner timePlanner;
    private TimePlan timePlan;
    private float time;

    public static String TEST_BEAT_EVENT = "testBeatoutEvent";
    public static String CYCLE_END = "cycleEnd";

    public BeatOut(float width, float height) {
        this.gameBoard = new GameBoard(width, height);

        QuantizationTimePlanner planner = new QuantizationTimePlanner();
//        planner.setSmallestBeat(NoteValues.EIGHTH_NOTE);
        this.timePlanner = planner;

        currentTrajectory = gameBoard.calculateTrajectory();
        timePlan = timePlanner.getTimePlan(currentTrajectory);

        this.ballAnimator = new BallAnimator(gameBoard.getBall(), currentTrajectory, timePlan);
        ballAnimator.setOnFinished(new Runnable() {
            @Override
            public void run() {
                gameBoard.getBall().setDirection(calculateDirectionAfterPaddle(gameBoard.getBall(), gameBoard.getPaddle()));
                currentTrajectory = gameBoard.calculateTrajectory();
                timePlan = timePlanner.getTimePlan(currentTrajectory);
                ballAnimator.setTrajectory(currentTrajectory);
                ballAnimator.setTimePlan(timePlan);
                NotificationManager.getDefault().registerEvent(CYCLE_END, this);
            }


        });
    }

    private Vector calculateDirectionAfterPaddle(Ball ball, Paddle paddle) {
        float ballCenterX = ball.getCenterX();
        float paddleCenterX = paddle.getCenterX();
        float halfPaddleWidth = paddle.getSize().getX() / 2;
        float directionFactor = BeatOutMath.clamp((ballCenterX - paddleCenterX) / halfPaddleWidth, -0.8f, 0.8f);
        System.out.println("Direction factor = " + directionFactor);
        float angle = (float)(Math.PI/2 - directionFactor*Math.PI/2);
        System.out.println("angle =  " + angle);
        return getUnitVectorWithAngle(angle);
    }

    private Vector getUnitVectorWithAngle(float angle) {
        return new Vector((float)Math.cos(angle), -(float)Math.sin(angle));
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
        return getGameBoard().getBall().getPosition();
    }
}
