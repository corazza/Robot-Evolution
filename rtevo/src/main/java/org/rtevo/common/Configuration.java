/**
 * 
 */
package org.rtevo.common;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Jan Corazza
 * 
 */
public class Configuration {
    private int generations;
    private int robotsPerGeneration;
    private int millisecondsPerSimulation;

    public Configuration() throws IOException {
        applyProperties(getDefaultProperties());
    }

    public Configuration(Properties p) throws IOException {
        applyProperties(p);
    }

    private void applyProperties(Properties p) throws IOException {
        generations = Integer.parseInt(p.getProperty("generations", "1000"));

        robotsPerGeneration = Integer.parseInt(p.getProperty(
                "robotsPerGeneration", "100"));

        millisecondsPerSimulation = Integer.parseInt(p.getProperty(
                "millisecondsPerSimulation", "120000"));
    }

    private static Properties getDefaultProperties() throws IOException {
        Properties defaultProperties = new Properties();
        defaultProperties.load(Configuration.class
                .getResourceAsStream("/default-configuration.properties"));
        return defaultProperties;
    }

    @Override
    public String toString() {
        return "Configuration [generations=" + generations
                + ", robotsPerGeneration=" + robotsPerGeneration
                + ", millisecondsPerSimulation=" + millisecondsPerSimulation
                + "]";
    }

    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }

    public int getRobotsPerGeneration() {
        return robotsPerGeneration;
    }

    public void setRobotsPerGeneration(int robotsPerGeneration) {
        this.robotsPerGeneration = robotsPerGeneration;
    }

    public int getMillisecondsPerSimulation() {
        return millisecondsPerSimulation;
    }

    public void setMillisecondsPerSimulation(int millisecondsPerSimulation) {
        this.millisecondsPerSimulation = millisecondsPerSimulation;
    }

}
