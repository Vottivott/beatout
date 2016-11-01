package com.beatout.core;

/**
 * Represents the movement of the ball through the trajectory over time
 */
public interface TimePlan {
    Trajectory.PointOnTrajectory getPointOnTrajectory(float time);
}