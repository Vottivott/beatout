package com.beatout.core;

import com.beatout.math.Vector;

public abstract class Collision implements Positioned {
    public abstract void performCollision();

    public abstract boolean isBlockCollision();

    private Vector position;
    private boolean isVertical;

    public Collision(Vector position, boolean isVertical) {
        this.position = position;
        this.isVertical = isVertical;
    }

    @Override
    public Vector getPosition() {
        return position;
    }

    public boolean isVertical() {
        return isVertical;
    }

    public Vector getResultingDirection(Vector directionBeforeCollision) {
        float dirX = directionBeforeCollision.getX();
        float dirY = directionBeforeCollision.getY();
        if (isVertical) {
            if (directionBeforeCollision.getX() < 0) {
                return new Vector(-dirY, dirX);
            } else {
                return new Vector(dirY, -dirX);
            }
        } else {
            if (directionBeforeCollision.getY() < 0) {
                return new Vector(-dirY, dirX);
            } else {
                return new Vector(dirY, -dirX);
            }
        }
    }
}
