package com.beatout.core;

import java.util.List;

public class LinearTimePlan implements TimePlan {

    List<Float> timePositions; // The fractional time position (0 = beginning of the bar, 1 = end of the bar (e.g. 4 beats passed)) of each collision
    // Invariants : must be ascending

    public LinearTimePlan(List<Float> timePositions) {
        this.timePositions = timePositions;
    }

    @Override
    public Trajectory.PointOnTrajectory getPointOnTrajectory(float time) {
        for (int bounce = 0; bounce < timePositions.size()-1; bounce++) {
            float timeCurrentBounce = timePositions.get(bounce);
            float timeNextBounce = timePositions.get(bounce + 1);
            if (timeCurrentBounce <= time && timeNextBounce > time) {
                float fraction = (time - timeCurrentBounce) / (timeNextBounce - timeCurrentBounce);
                return new Trajectory.PointOnTrajectory(bounce, fraction);
            }
        }
        throw new RuntimeException("The time " + time + " was not found in the interval represented by this LinearTimePlan!");
    }
}
