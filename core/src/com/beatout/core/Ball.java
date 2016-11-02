package com.beatout.core;

import com.beatout.math.*;
import com.beatout.math.Line;

// Immutable
public class Ball implements Positioned {
    private final float radius;
    private Vector position;
    private final Vector direction;

    public Ball(float radius, Vector position, Vector direction) {
        this.radius = radius;
        this.position = position;
        this.direction = direction;
    }

    public float getRadius() {
        return radius;
    }

    public Vector getDirection() {
        return direction;
    }

    public Vector getPosition() {
        return position;
    }

    public Line getDirectionLine() {
        return new Line(position, position.add(direction));
    }
}
