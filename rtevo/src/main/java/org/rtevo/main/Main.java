/**
 * Measurement units:
 * 
 * length: meters
 * time: seconds
 * weight: kilograms
 */
package org.rtevo.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.rtevo.common.Configuration;

public class Main {
    public static void main(String[] args) throws FileNotFoundException,
            IOException {
        Configuration config;

        if (args.length == 0) {
            config = new Configuration();
        } else {
            Properties properties = new Properties();
            properties.load(new FileInputStream(args[0]));
            config = new Configuration(properties);
        }

        RobotEvolution app = new RobotEvolution(config);
        app.start();
    }
}
