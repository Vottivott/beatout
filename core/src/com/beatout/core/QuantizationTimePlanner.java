package com.beatout.core;

import com.beatout.math.*;

import java.util.ArrayList;
import java.util.List;

public class QuantizationTimePlanner implements TimePlanner {

    private int timeSignatureUpper = 4;
    private int timeSignatureLower = 4;
    private float smallestBeat = NoteValues.THIRTYSECOND_NOTE; //NoteValues.EIGHTH_NOTE;

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

//        getPossibleBeatPlans(timePositions.size());

        setBounceIntensities(trajectory, timePositions);

        TimePlan timePlan = new LinearTimePlan(timePositions);

        return timePlan;
    }

    private void setBounceIntensities(Trajectory trajectory, List<Float> timePositions) {
        for (int i = 0; i < timePositions.size(); i++) {
            float t = timePositions.get(i);
//            trajectory.getBounce(i).setIntensity(getIntensity(t, timePositions));
            trajectory.getBounce(i).setIntensity(1);//getIntensity(t, timePositions));
        }
    }

    private float getIntensity(float t, List<Float> timePositions) {
        float factor = 0;
        int numCounted = 0;
        for (float p : timePositions) {
            if (dist(t,p) > 0.01) {
                factor += 1f / (1+squareDist(t, p));
                numCounted++;
            }
        }
        if (numCounted == 0) {
            return 1;
        }
        return factor/numCounted + 1;
    }

    private float squareDist(float t, float p) {
        return (t-p)*(t-p);
    }

    private float dist(float t, float p) {
        return Math.abs(t-p);
    }


    private void getPossibleBeatPlans(int size) {

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
