package com.beatout.core;

import com.beatout.math.*;
import com.beatout.math.Line;

// Immutable
public class Ball extends RectBounded {
    private final float radius;
    private final Vector position;
    private final Vector direction;

    // HVC points are three corner points of the ball's rect, defined by the direction of the ball where:
    // c = the corner most directly pointed in the direction of the ball
    // h = the corner on the same horizontal level as c
    // v = the corner on the same vertical level as c
    private Vector h,v,c;
    // HVC lines are lines originating at the HVC points, going in the direction of the ball
    private Line hLine, vLine, cLine;
    // The offset points indicate offset from the top-left corner
    private Vector hOffset,vOffset,cOffset;

    public Ball(float radius, Vector position, Vector direction) {
        this.radius = radius;
        this.position = position;
        this.direction = direction;
        calculateHVCPoints();
    }

    public float getRadius() {
        return radius;
    }

    public Vector getDirection() {
        return direction;
    }

    @Override
    public Vector getPosition() {
        return position;
    }

    @Override
    public Vector getSize() {
        return new Vector(2*radius, 2*radius);
    }

    public Line getDirectionLine() {
        return new Line(position, position.add(direction));
    }

    private void calculateHVCPoints() {
        float dx = this.getDirection().getX();
        float dy = this.getDirection().getY();
        if (dx >= 0 && dy > 0) {
            h = this.getBottomLeft();
            v = this.getTopRight();
            c = this.getBottomRight();
        } else if (dx < 0 && dy >= 0) {
            h = this.getBottomRight();
            v = this.getTopLeft();
            c = this.getBottomLeft();
        } else if (dx <= 0 && dy < 0) {
            h = this.getTopRight();
            v = this.getBottomLeft();
            c = this.getTopLeft();
        } else {//if (dx > 0 && dy <= 0) {
            h = this.getTopLeft();
            v = this.getBottomRight();
            c = this.getTopRight();
        }
        hLine = new Line(h, h.add(this.getDirection()));
        vLine = new Line(v, v.add(this.getDirection()));
        cLine = new Line(c, c.add(this.getDirection()));
        hOffset = h.subtract(this.getPosition());
        vOffset = v.subtract(this.getPosition());
        cOffset = c.subtract(this.getPosition());
    }

    public Vector getH() {
        return h;
    }

    public Vector getV() {
        return v;
    }

    public Vector getC() {
        return c;
    }

    public Line getHLine() {
        return hLine;
    }

    public Line getVLine() {
        return vLine;
    }

    public Line getCLine() {
        return cLine;
    }

    public Vector getPositionCoordinateFromH(Vector h) {
        return h.subtract(hOffset);
    }

    public Vector getPositionCoordinateFromV(Vector v) {
        return v.subtract(vOffset);
    }

    public Vector getPositionCoordinateFromC(Vector c) {
        return c.subtract(cOffset);
    }


}
