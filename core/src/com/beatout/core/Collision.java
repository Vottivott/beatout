package com.beatout.core;

import com.beatout.math.Vector;

public abstract class Collision implements Positioned {
    public abstract void performCollision();
    public abstract boolean isBlockCollision();

    private Vector position;

    public Collision(Vector position) {
        this.position = position;
    }

    @Override
    public Vector getPosition() {
        return position;
    }
}
