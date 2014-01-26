/**
 * 
 */
package org.rtevo.util;

/**
 * @author Jan Corazza
 * 
 */
public class RandUtil {
    public static int random(int min, int max) {
        return (int) (min + (Math.random() * (max - min)));
    }

    public static float random(float min, float max) {
        return (float) (min + (Math.random() * (max - min)));
    }

    public static double random(double min, double max) {
        return min + (Math.random() * (max - min));
    }
}
