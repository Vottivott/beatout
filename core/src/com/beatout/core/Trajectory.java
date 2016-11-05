package com.beatout.core;

import com.beatout.math.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the path of the ball from one hit of the bat to the return of the ball to the bottom of the screen.
 * This class has no responsibility of the time or speed of the trajectory, but only represents the distances travelled,
 * and the collisions on the way.
 */
public class Trajectory {
    private List<Collision> bounces;

    public Trajectory(List<Collision> bounces) {
        this.bounces = bounces;
    }

    public Vector getPosition(PointOnTrajectory point) {
        Collision currentBounce = bounces.get(point.getBounceIndex());
        Collision nextBounce = bounces.get(point.getBounceIndex() + 1);
        com.beatout.math.Line line = new com.beatout.math.Line(currentBounce.getPosition(), nextBounce.getPosition());
        return line.getPointOnLine(point.getLineFraction());
    }

    public List<Collision> getBounces() {
        return bounces;
    }

    public float getTotalDistance() {
        float distance = 0;
        for (int bounce = 0; bounce < bounces.size()-1; bounce++) {
            Vector currentBouncePos = bounces.get(bounce).getPosition();
            Vector nextBouncePos = bounces.get(bounce + 1).getPosition();
            distance += nextBouncePos.subtract(currentBouncePos).length();
        }
        return distance;
    }

    public List<Line> getLinesBetweenBounces() {
        List<Line> lines = new ArrayList<Line>();
        for (int bounce = 0; bounce < bounces.size()-1; bounce++) {
            Vector currentBouncePos = bounces.get(bounce).getPosition();
            Vector nextBouncePos = bounces.get(bounce + 1).getPosition();
            Line line = new Line(currentBouncePos, nextBouncePos);
            lines.add(line);
        }
        return lines;
    }

    /**
     * Represents a point on the trajectory by the index of the latest collision that will happen before being at that point,
     * and the fractional position on the line between this last collision and the next one
     * (lineFraction = 0.0f means being at the latest collision point and lineFraction = 1.0f means being at the next one)
     */
    public static class PointOnTrajectory {
        private int bounceIndex;
        private float lineFraction;

        public PointOnTrajectory(int bounceIndex, float lineFraction) {
            this.bounceIndex = bounceIndex;
            this.lineFraction = lineFraction;
        }

        public int getBounceIndex() {
            return bounceIndex;
        }

        public float getLineFraction() {
            return lineFraction;
        }

        public boolean isOnSameBounce(PointOnTrajectory pointOnTrajectory) {
            return getBounceIndex() == pointOnTrajectory.getBounceIndex();
        }
    }
}
