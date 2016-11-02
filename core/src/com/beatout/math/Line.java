package com.beatout.math;

import com.beatout.math.Vector;

public class Line {
    private Vector start;
    private Vector end;

    public Line(Vector start, Vector end) {
        this.start = start;
        this.end = end;
    }

    public Line(float x1, float y1, float x2, float y2) {
        this(new Vector(x1, y1), new Vector(x2, y2));
    }

    public Vector getPointOnLine(float fraction) {
        return start.add(end.subtract(start).scale(fraction));
    }

    public float getLength() {
        return end.subtract(start).length();
    }

    public Vector getStart() {
        return start;
    }

    public Vector getEnd() {
        return end;
    }
}
