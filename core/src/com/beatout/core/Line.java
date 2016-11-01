package com.beatout.core;

import com.beatout.math.Vector;

public class Line {
    private Vector start;
    private Vector end;

    public Line(Vector start, Vector end) {
        this.start = start;
        this.end = end;
    }

    public Vector getPointOnLine(float fraction) {
        return start.add(end.subtract(start).scale(fraction));
    }

    public float getLength() {
        return end.subtract(start).length();
    }

}
