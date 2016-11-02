package com.beatout.math;

public class BeatOutMath {
    public static float clamp(float x, float min, float max) {
        return Math.max(min, Math.min(x, max));
    }

    public static float clamp01(float x) {
        return clamp(x, 0.0f, 1.0f);
    }
}
