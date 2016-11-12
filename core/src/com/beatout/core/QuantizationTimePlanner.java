package com.beatout.core;

import com.beatout.math.*;

import java.util.ArrayList;
import java.util.List;

public class QuantizationTimePlanner implements TimePlanner {

    private int timeSignatureUpper = 4;
    private int timeSignatureLower = 4;
    private float smallestBeat = NoteValues.EIGHTH_NOTE;

    public void setTimeSignature(int upper, int lower) {
        this.timeSignatureUpper = upper;
        this.timeSignatureLower = lower;
    }

    public void setSmallestBeat(float noteValue) {
        this.smallestBeat = noteValue;
    }

    @Override
    public TimePlan getTimePlan(Trajectory trajectory) {

        List<Float> timePositions = getTimePositionsBasedOnConstantSpeed(trajectory);
        // TODO: Implement quantization

        for (int i = 0; i < timePositions.size(); i++) {
            float p = timePositions.get(i);
            timePositions.set(i, getClosestTimePosition(p));
        }



        TimePlan timePlan = new LinearTimePlan(timePositions);

        return timePlan;
    }

    private float getClosestTimePosition(float p) {
        double numOfSmallestUnits = Math.round(p / smallestBeat);
        return (float)(numOfSmallestUnits * smallestBeat);
    }

    private List<Float> getTimePositionsBasedOnConstantSpeed(Trajectory trajectory) {
        float totalDistance = trajectory.getTotalDistance();
        List<com.beatout.math.Line> lines = trajectory.getLinesBetweenBounces();
        List<Float> timePositions = new ArrayList<Float>();
        timePositions.add(0f);
        float distanceSoFar = 0;
        for (com.beatout.math.Line line : lines) {
            distanceSoFar += line.getLength();
            timePositions.add(distanceSoFar / totalDistance);
        }
        return timePositions;
    }
}
