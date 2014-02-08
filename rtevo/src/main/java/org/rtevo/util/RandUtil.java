package org.rtevo.util;

public class RandUtil {
    public static int random(int min, int max) {
        return (int) (min + (Math.random() * (max - min)));
    }

    public static float random(float min, float max) {
        return (float) (min + (Math.random() * (max - min)));
    }
    
    public static float random() {
        return random(0f, 1f);
    }

    public static double random(double min, double max) {
        return min + (Math.random() * (max - min));
    }
}
