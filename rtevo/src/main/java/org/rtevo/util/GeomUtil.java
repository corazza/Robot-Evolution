package org.rtevo.util;

public class GeomUtil {
    public static float circle(double fullCircleIsOne) {
        return (float) (fullCircleIsOne * 2 * Math.PI);
    }

    public static float circle(float fullCircleIsOne) {
        return (float) (fullCircleIsOne * 2 * Math.PI);
    }
}
