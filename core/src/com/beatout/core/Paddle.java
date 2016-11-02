package com.beatout.core;

import com.beatout.math.*;

public class Paddle extends Collideable { // Maybe not necessary to be a Collideable
    private Vector size;
    private com.beatout.math.Line lineOfMovement;
    private float fractionalPosition;
    private float speed;

    public Paddle(Vector size, com.beatout.math.Line range, float speed) {
        this.size = size;
        this.lineOfMovement = new com.beatout.math.Line(range.getStart(), range.getEnd().subtract(size.getX(), 0));
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

    @Override
    public Collision collideWith(Vector collisionPoint, boolean verticalEdge) {
        return new PaddleCollision(collisionPoint);
    }
}
