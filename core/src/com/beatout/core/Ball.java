package com.beatout.core;

import com.beatout.math.*;
import com.beatout.math.Line;

// Immutable
public class Ball extends RectBounded {
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

    @Override
    public Vector getPosition() {
        return position;
    }

    @Override
    public Vector getSize() {
        return new Vector(2*radius, 2*radius);
    }

    public Line getDirectionLine() {
        return new Line(position, position.add(direction));
    }
}
