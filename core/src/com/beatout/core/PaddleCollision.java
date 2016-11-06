package com.beatout.core;

import com.beatout.math.Vector;

public class PaddleCollision extends Collision {

    public static final String BAT_COLLISION_EVENT = "BatCollisionEvent";

    public PaddleCollision(Vector ballPosition) {
        super(ballPosition, Direction.BALL_FROM_TOP);
    }

    @Override
    public void performCollision() {
        NotificationManager.getDefault().registerEvent(BAT_COLLISION_EVENT, this);
    }

    @Override
    public boolean isBlockCollision() {
        return false;
    }

}
