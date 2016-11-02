package com.beatout.core;

import com.beatout.math.Vector;

public class PaddleCollision extends Collision {

    public static final String BAT_COLLISION_EVENT = "BatCollisionEvent";

    public PaddleCollision(Vector position) {
        super(position);
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
