package com.beatout.core;

import com.beatout.math.BeatOutMath;
import com.beatout.math.Vector;

public class Paddle implements Positioned {
    private Vector size;
    private Line lineOfMovement;
    private float fractionalPosition;
    private float speed;

    public Paddle(Vector size, Line range, float speed) {
        this.size = size;
        this.lineOfMovement = new Line(range.getStart(), range.getEnd().subtract(size.getX(), 0));
        this.speed = speed;
        this.fractionalPosition = 0.5f;
    }

    public void move(float direction, float deltaTime) {
        fractionalPosition = BeatOutMath.clamp01(fractionalPosition + direction*speed*deltaTime);
    }

    @Override
    public Vector getPosition() {
        return lineOfMovement.getPointOnLine(fractionalPosition);
    }

    public Vector getSize() {
        return size;
    }
}
