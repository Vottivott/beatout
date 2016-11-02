package com.beatout.core;

import com.beatout.math.Vector;

public abstract class Collideable extends RectBounded {
    public abstract Collision collideWith(Vector collisionPoint, boolean verticalEdge);
}
