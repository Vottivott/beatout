package com.beatout.core;

import com.beatout.math.Vector;

public abstract class Collision implements Positioned {
    public abstract void performCollision();

    public abstract boolean isBlockCollision();

    private Vector ballPosition;
    private Direction direction;

    private float intensity = 1.0f; // Experimental

    public Collision(Vector ballPosition, Direction direction) {
        this.ballPosition = ballPosition;
        this.direction = direction;
    }

    @Override
    public Vector getPosition() {
        return ballPosition;
    }

    public boolean isVertical() {
        return direction == Direction.BALL_FROM_LEFT || direction == Direction.BALL_FROM_RIGHT;
    }

    public Vector getResultingDirection(Vector directionBeforeCollision) {
        float dirX = directionBeforeCollision.getX();
        float dirY = directionBeforeCollision.getY();
//        float xSign = directionBeforeCollision.getX() < 0 ? -1 : 1;
//        float ySign = directionBeforeCollision.getY() < 0 ? -1 : 1;
//        float sign = xSign * ySign;
        if (isVertical()) {
            return new Vector(-dirX, dirY);
        } else {
            return new Vector(dirX, -dirY);
        }
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public float getIntensity() {
        return intensity;
    }

    public Direction getDirection() {
        return direction;
    }

    public enum Direction {
        BALL_FROM_LEFT, BALL_FROM_TOP, BALL_FROM_RIGHT, BALL_FROM_BOTTOM
    }
}
