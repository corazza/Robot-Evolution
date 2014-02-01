/**
 * 
 */
package org.rtevo.common;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Jan Corazza & Luka Bubalo
 * 
 */
public class Configuration {
    public int generations;
    public int robotsPerGeneration;
    public int robotMilliseconds;
    public int parallelSimulations;
    public int windowWidth;
    public int windowHeight;
    public float gravity;
    public float timeStep;
    public boolean GUI;
    public String save;
    public String load;

    public Configuration() {
        applyProperties(getDefaultProperties());
    }

    public Configuration(Properties p) {
        applyProperties(p);
    }

    private void applyProperties(Properties p) {
        robotsPerGeneration = Integer.parseInt(p.getProperty(
                "robotsPerGeneration", "100"));
        robotMilliseconds = Integer.parseInt(p.getProperty("robotMilliseconds",
                "120000"));
        parallelSimulations = Integer.parseInt(p.getProperty(
                "parallelSimulations", "7"));
        generations = Integer.parseInt(p.getProperty("generations", "1000"));
        windowWidth = Integer.parseInt(p.getProperty("windowWidth", "1024"));
        windowHeight = Integer.parseInt(p.getProperty("windowHeight", "512"));
        gravity = Float.parseFloat(p.getProperty("gravity", "10"));
        timeStep = Float.parseFloat(p.getProperty("timeStep", "0.01"));
        GUI = Boolean.parseBoolean(p.getProperty("GUI", "true"));
        save = p.getProperty("save", "false");
        load = p.getProperty("load", "false");
    }

    private static Properties getDefaultProperties() {
        Properties defaultProperties = new Properties();
        try {
            defaultProperties.load(Configuration.class
                    .getResourceAsStream("/default-configuration.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read default configuration");
        }
        return defaultProperties;
    }

}
