package com.beatout.core;

import com.beatout.math.BeatOutMath;
import com.beatout.math.Vector;

public class Ball {
    private float radius;
    private Vector direction;

    public Ball(float radius, Vector startDirection) {
        this.radius = radius;
        this.direction = startDirection;
    }

    public float getRadius() {
        return radius;
    }

    public Vector getDirection() {
        return direction;
    }
}
