/**
 * 
 */
package org.rtevo.main;

import org.rtevo.common.Configuration;
import org.rtevo.gui.RobotEvolutionWindow;
import org.rtevo.simulation.Generation;
import org.rtevo.simulation.Simulation;

/* MEMO Weight is a property of all objects, joints and body have weights, make them
 * extend something, evolve this weight
 * 
 * Think about: no legs or anything like that, just random polygons with weight
 * and joints
 */

/**
 * main application class
 * 
 */
public class RobotEvolution {
    private RobotEvolutionWindow window;

    private int generations;
    private int robotsPerGeneration;
    private int timeStep;
    private int parallelSimulations;
    private int robotMilliseconds;

    public RobotEvolution(Configuration config) {
        generations = config.getGenerations();
        robotsPerGeneration = config.getRobotsPerGeneration();
        timeStep = config.getTimeStep();
        parallelSimulations = config.getParallelSimulations();
        robotMilliseconds = config.getRobotMilliseconds();
    }

    public void start() {
        window = new RobotEvolutionWindow();

        // Initialize GA:
        Generation generation = new Generation(timeStep, robotMilliseconds,
                parallelSimulations, robotsPerGeneration);

        // Main algorithm
        for (int i = 0; i < generations; ++i) {
            // Create all the simulations and start them in their separate
            // threads
            generation.computeAll();

            // Create a simulation for a single chromosome
            Simulation presentationSimulation = generation.getSample();
            presentationSimulation.setup();

            // Submit it to the renderer
            window.setSimulation(presentationSimulation);

            // Simulate and render the presentation simulation while real
            // computation is being done in the backend
            while (!generation.isDone()) {
                presentationSimulation.update();
                window.updateDisplay();

                if (window.isCloseRequested()) {
                    exit();
                }
            }

            // Evolve
            generation = generation.nextGeneration();

            System.out.println("generation #" + (i + 1) + " done");
        }

        exit();
    }

    public void exit() {
        window.destroy();

        // TODO convert to log
        System.out.println("Program done.");

        System.exit(0);
    }

}
