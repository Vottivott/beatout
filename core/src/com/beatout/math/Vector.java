package com.beatout.math;

/**
 * Immutable vector class
 */
public class Vector {
    private final float x;
    private final float y;

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector add(float x, float y) {
        return new Vector(this.x + x, this.y + y);
    }

    public Vector add(Vector v) {
        return add(v.getX(), v.getY());
    }

    public Vector subtract(float x, float y) {
        return add(-x, -y);
    }

    public Vector subtract(Vector v) {
        return subtract(v.getX(), v.getY());
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + String.format("%.1f", getX()) + ", " + String.format("%.1f", getY()) + ")";
    }

    public Vector scale(float scalar) {
        return new Vector(scalar*x, scalar*y);
    }

    public float lengthSquared() {
        return x*x + y*y;
    }

    public float length() {
        return (float)Math.sqrt(lengthSquared());
    }

    public Vector normalize() {
        float length = length();
        return new Vector(x / length, y / length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector vector = (Vector) o;

        if (Float.compare(vector.x, x) != 0) return false;
        return Float.compare(vector.y, y) == 0;

    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }
}