package com.beatout.core;

import com.beatout.math.Vector;

public class Block implements Positioned {
    private Vector position;
    private Vector size;
    private boolean active;

    public Block(Vector position, Vector size) {
        this.position = position;
        this.size = size;
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

    public Vector getSize() {
        return size;
    }
}
