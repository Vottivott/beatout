package com.beatout.core;

import static com.beatout.core.BeatOut.TEST_BEAT_EVENT;

public class BallAnimator {
    private Ball ball;
    private Trajectory trajectory;
    private TimePlan timePlan;

    private Runnable onFinished;

    private float timeDuration = 4;
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

//            Trajectory.PointOnTrajectory point = timePlan.getPointOnTrajectory(0.999f);
//            ball.setPosition(trajectory.getPosition(point));
//            ball.setDirection(trajectory.getDirection(point));

            if (onFinished != null) {
                onFinished.run();
            }
            return;
        }

        if (Math.round(time/timeDuration * 8) != Math.round((time - deltaTime)/timeDuration * 8)) { // Test
            NotificationManager.getDefault().registerEvent(TEST_BEAT_EVENT, this);
        }

        Trajectory.PointOnTrajectory point = timePlan.getPointOnTrajectory(time / timeDuration);
        int bounceIndex = point.getBounceIndex();
        if (bounceIndex != lastBounceIndex) {
            for (int i = lastBounceIndex+1; i <= bounceIndex; i++) {
                Collision bounce = trajectory.getBounce(i);
                bounce.performCollision();
            }
            lastBounceIndex = bounceIndex;
        }
        ball.setPosition(trajectory.getPosition(point));
        ball.setDirection(trajectory.getDirection(point));
    }

    public void setTrajectory(Trajectory trajectory) {
        this.trajectory = trajectory;
    }

    public void setTimePlan(TimePlan timePlan) {
        this.timePlan = timePlan;
    }

    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }

}
