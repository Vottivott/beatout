package com.beatout.core;

import com.beatout.math.Vector;

public class Block extends Collideable {
    private Vector position;
    private Vector size;
    private boolean active;

    public Block(Vector position, Vector size) {
        this.position = position;
        this.size = size;
        this.active = true;
    }

    public Block(Block block) {
        this.position = block.getPosition();
        this.size = block.getSize();
        this.active = block.isActive();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public Vector getPosition() {
        return position;
    }

    @Override
    public Vector getSize() {
        return size;
    }

    @Override
    public Collision collideWith(Vector ballPosition, Collision.Direction direction) {
        return new BlockCollision(ballPosition, direction, this);
    }
}
