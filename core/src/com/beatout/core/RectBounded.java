package com.beatout.core;

import com.beatout.math.Vector;
import com.beatout.math.Line;

import java.util.ArrayList;
import java.util.List;

public abstract class RectBounded implements Positioned {
    public abstract Vector getPosition();
    public abstract Vector getSize();
    public List<Line> getBoundingLines() {
        List<Line> lines = new ArrayList<Line>();
        lines.add(getTopLine());
        lines.add(getLeftLine());
        lines.add(getBottomLine());
        lines.add(getRightLine());
        return lines;
    }
    public float getTop() {
        return getPosition().getY();
    }
    public float getLeft() {
        return getPosition().getX();
    }
    public float getBottom() {
        return getPosition().add(getSize()).getY();
    }
    public float getRight() {
        return getPosition().add(getSize()).getX();
    }
    public Line getTopLine() {
        return new Line(getPosition(), getPosition().add(getSize().getX(), 0));
    }
    public Line getLeftLine() {
        return new Line(getPosition(), getPosition().add(0, getSize().getY()));
    }
    public Line getBottomLine() {
        return new Line(getPosition().add(0, getSize().getY()), getPosition().add(getSize()));
    }
    public Line getRightLine() {
        return new Line(getPosition().add(getSize().getX(), 0), getPosition().add(getSize()));
    }
    public Vector getTopLeft() {
        return getPosition();
    }
    public Vector getTopRight() {
        return getPosition().add(getSize().getX(), 0);
    }
    public Vector getBottomLeft() {
        return getPosition().add(0, getSize().getY());
    }
    public Vector getBottomRight() {
        return getPosition().add(getSize());
    }

    public float getCenterX() {
        return getPosition().getX() + getSize().getX() / 2;
    }
    public float getCenterY() {
        return getPosition().getY() + getSize().getY() / 2;
    }
}
