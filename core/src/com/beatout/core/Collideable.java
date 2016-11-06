package com.beatout.core;

import com.beatout.math.Vector;

import static com.beatout.core.Collision.Direction.*;

public abstract class Collideable extends RectBounded {

    public abstract Collision collideWith(Vector ballPosition, Collision.Direction direction);

    public Collision collideWith(Vector ballPosition, Ball ball, boolean isVertical) {
        if (isVertical) {
            if (ball.getDirection().getX() > 0) {
                return collideWith(ballPosition, BALL_FROM_LEFT);
            } else {
                return collideWith(ballPosition, BALL_FROM_RIGHT);
            }
        } else {
            if (ball.getDirection().getY() > 0) {
                return collideWith(ballPosition, BALL_FROM_TOP);
            } else {
                return collideWith(ballPosition, BALL_FROM_BOTTOM);
            }
        }
    }
}
