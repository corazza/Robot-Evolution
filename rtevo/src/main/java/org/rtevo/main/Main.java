/**
 * Measurement units:
 * 
 * length: meters
 * time: seconds
 * weight: kilograms
 */
package org.rtevo.main;

import org.rtevo.common.Configuration;

public class Main {
    public static void main(String[] args) {
        Configuration config = new Configuration();
        RobotEvolution app = new RobotEvolution(config);
        app.start();
    }
}
