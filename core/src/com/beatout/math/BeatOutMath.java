package com.beatout.math;

public class BeatOutMath {
    public static float clamp(float x, float min, float max) {
        return Math.max(min, Math.min(x, max));
    }

    public static float clamp01(float x) {
        return clamp(x, 0.0f, 1.0f);
    }


    /**
     * @param lineIsOneEnded If false the specified line is treated as stretching infinitely far
     *                       in both directions of the starting point,
     *                       if true the specified line is treated as stretching infinitely far
     *                       in only the direction towards the endpoint (and beyond)
     * @return null if the lines don't intersect
     */
    public static Vector getIntersectionBetweenVerticalLineSegmentAndLine(float x, float y1, float y2, Line line, boolean lineIsOneEnded) {
        Vector intersection = getIntersectionBetweenVerticalLineAndLine(x, line, lineIsOneEnded);
        float yMin = Math.min(y1, y2);
        float yMax = Math.max(y1, y2);
        if (intersection != null && intersection.getY() >= yMin && intersection.getY() <= yMax) {
            return intersection;
        } else {
            return null;
        }
    }

    /**
     * @param lineIsOneEnded If false the specified line is treated as stretching infinitely far
     *                       in both directions of the starting point,
     *                       if true the specified line is treated as stretching infinitely far
     *                       in only the direction towards the endpoint (and beyond)
     * @return null if the lines don't intersect
     */
    public static Vector getIntersectionBetweenHorizontalLineSegmentAndLine(float y, float x1, float x2, Line line, boolean lineIsOneEnded) {
        Vector intersection = getIntersectionBetweenHorizontalLineAndLine(y, line, lineIsOneEnded);
        float xMin = Math.min(x1, x2);
        float xMax = Math.max(x1, x2);
        if (intersection != null && intersection.getX() >= xMin && intersection.getX() <= xMax) {
            return intersection;
        } else {
            return null;
        }
    }

    /**
     * @param lineIsOneEnded If false the specified line is treated as stretching infinitely far
     *                       in both directions of the starting point,
     *                       if true the specified line is treated as stretching infinitely far
     *                       in only the direction towards the endpoint (and beyond)
     * @return null if the lines don't intersect
     */
    public static Vector getIntersectionBetweenVerticalLineAndLine(float x, Line line, boolean lineIsOneEnded) {
        Vector start = line.getStart();
        Vector direction = line.getEnd().subtract(line.getStart());
        if (direction.getX() == 0) {
            return null; // Both lines are vertical so they don't intersect
        }
        if (lineIsOneEnded && (direction.getX() > 0 && x < 0 || direction.getX() < 0 && x > 0)) {
            return null; // The vertical line is on the wrong side of the one-ended line
        }
        float slope = direction.getY() / direction.getX();
        float yIntercept = start.getY() - slope * start.getX();
        float intersectionY = slope * x + yIntercept;
        return new Vector(x, intersectionY);
    }

    /**
     * @param lineIsOneEnded If false the specified line is treated as stretching infinitely far
     *                       in both directions of the starting point,
     *                       if true the specified line is treated as stretching infinitely far
     *                       in only the direction towards the endpoint (and beyond)
     * @return null if the lines don't intersect
     */
    public static Vector getIntersectionBetweenHorizontalLineAndLine(float y, Line line, boolean lineIsOneEnded) {
        return getVectorWithXAndYSwapped(getIntersectionBetweenVerticalLineAndLine(y, getLineWithXAndYSwapped(line), lineIsOneEnded));
    }

    private static Line getLineWithXAndYSwapped(Line line) {
        Vector start = getVectorWithXAndYSwapped(line.getStart());
        Vector end = getVectorWithXAndYSwapped(line.getEnd());
        return new Line(start, end);
    }

    private static Vector getVectorWithXAndYSwapped(Vector vector) {
        return new Vector(vector.getY(), vector.getX());
    }

    /**
     * Returns the point where the two lines intersect.
     * The lines are treated as infinitely long.
     * If there is no such point null is returned
     */
    public static Vector getLineLineIntersection(Line a, Line b) {
        float x1 = a.getStart().getX();
        float y1 = a.getStart().getY();
        float x2 = a.getEnd().getX();
        float y2 = a.getEnd().getY();
        float x3 = b.getStart().getX();
        float y3 = b.getStart().getY();
        float x4 = b.getEnd().getX();
        float y4 = b.getEnd().getY();
        float denominator = ((x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4));
        if (denominator == 0) {
            return null;
        }
        float x = ((x1*y2 - y1*x2)*(x3 - x4) - (x1 - x2)*(x3*y4 - y3*x4)) / denominator;
        float y = ((x1*y2 - y1*x2)*(y3 - y4) - (y1 - y2)*(x3*y4 - y3*x4)) / denominator;
        return new Vector(x,y);
    }


}
