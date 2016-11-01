package com.beatout.core;

import com.beatout.math.Vector;

public class Block implements Positioned {
    Vector position;
    boolean active;

    public Block(Vector position) {
        this.position = position;
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
}
