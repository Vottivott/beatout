package com.beatout.core;

public class BallAnimator {
    private Ball ball;
    private Trajectory trajectory;
    private TimePlan timePlan;

    private float timeDuration = 5;
    private float time = 0;
    private int lastBounceIndex = -1;

    public BallAnimator(Ball ball, Trajectory trajectory, TimePlan timePlan) {
        this.ball = ball;
        this.trajectory = trajectory;
        this.timePlan = timePlan;
    }

    public void update(float deltaTime) {
        time += deltaTime;
        Trajectory.PointOnTrajectory point = timePlan.getPointOnTrajectory(time / timeDuration);
        int bounceIndex = point.getBounceIndex();
        if (bounceIndex != lastBounceIndex) {
            Collision bounce = trajectory.getBounce(bounceIndex);
            bounce.performCollision();
        }
        ball.setPosition(trajectory.getPosition(point));
    }

}
