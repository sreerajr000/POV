package com.pazhankanjiz.pov.util;

public class AnimationUtils {

    public static void animateOut() {

    }

    public static Integer map(Integer x, Integer inMin, Integer inMax, Integer outMin, Integer outMax) {
        return (x -inMin) * (outMax-outMin) / (inMax-inMin) + outMin;
    }
}
