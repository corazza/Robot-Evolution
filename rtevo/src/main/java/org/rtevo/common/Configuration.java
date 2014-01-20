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
    private int robotMilliseconds;
    private int parallelSimulations;
    private int timeStep;

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

        robotMilliseconds = Integer.parseInt(p.getProperty("robotMilliseconds",
                "120000"));

        parallelSimulations = Integer.parseInt(p.getProperty(
                "parallelSimulations", "7"));

        timeStep = Integer.parseInt(p.getProperty("timeStep", "10"));
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
                + ", robotMilliseconds=" + robotMilliseconds
                + ", parallelSimulations=" + parallelSimulations
                + ", timeStep=" + timeStep + "]";
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

    public int getRobotMilliseconds() {
        return robotMilliseconds;
    }

    public void setRobotMilliseconds(int robotMilliseconds) {
        this.robotMilliseconds = robotMilliseconds;
    }

    public int getParallelSimulations() {
        return parallelSimulations;
    }

    public void setParallelSimulations(int parallelSimulations) {
        this.parallelSimulations = parallelSimulations;
    }

    public int getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(int timeStep) {
        this.timeStep = timeStep;
    }

}
