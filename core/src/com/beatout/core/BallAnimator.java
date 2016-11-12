package com.beatout.core;

import static com.beatout.core.BeatOut.TEST_BEAT_EVENT;

public class BallAnimator {
    private Ball ball;
    private Trajectory trajectory;
    private TimePlan timePlan;

    private Runnable onFinished;

    private float timeDuration = 2;
    private float time = 0;
    private int lastBounceIndex = -1;

    public BallAnimator(Ball ball, Trajectory trajectory, TimePlan timePlan) {
        this.ball = ball;
        this.trajectory = trajectory;
        this.timePlan = timePlan;
    }

    public void update(float deltaTime) {
        time += deltaTime;

        if (time > timeDuration) {
            time = time - timeDuration;
            if (onFinished != null) {
                onFinished.run();
            }
        }

        if (Math.round(time/timeDuration * 4) != Math.round((time - deltaTime)/timeDuration * 4)) { // Test
            NotificationManager.getDefault().registerEvent(TEST_BEAT_EVENT, this);
        }

        Trajectory.PointOnTrajectory point = timePlan.getPointOnTrajectory(time / timeDuration);
        int bounceIndex = point.getBounceIndex();
        if (bounceIndex != lastBounceIndex) {
            Collision bounce = trajectory.getBounce(bounceIndex);
            bounce.performCollision();
            lastBounceIndex = bounceIndex;
        }
        ball.setPosition(trajectory.getPosition(point));
    }

    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }

}
