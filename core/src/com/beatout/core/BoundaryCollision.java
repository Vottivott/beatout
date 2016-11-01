package com.beatout.core;

import com.beatout.math.Vector;

public class BoundaryCollision extends Collision {

    public static final String BOUNDARY_COLLISION_EVENT = "BoundaryCollisionEvent";

    public BoundaryCollision(Vector position) {
        super(position);
    }

    @Override
    public void performCollision() {
        NotificationManager.getDefault().registerEvent(BOUNDARY_COLLISION_EVENT, this); // Create block collision event
    }

    @Override
    public boolean isBlockCollision() {
        return false;
    }


}
