/**
 * 
 */
package org.rtevo.main;

import org.rtevo.common.Configuration;
import org.rtevo.gui.Window;
import org.rtevo.simulation.Generation;
import org.rtevo.simulation.Robot;
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
    private Window window;

    private Configuration c;

    public RobotEvolution(Configuration config) {
        c = config;

        if (c.parallelSimulations < 1) {
            throw new IllegalArgumentException(
                    "The number of parallel simulations must be greater than 0");
        }

        Generation.configureWorkerPool(c.parallelSimulations);
        Generation.setGravity(c.gravity);
        Generation.setTimeStep(c.timeStep);
        Robot.setRobotMilliseconds(c.robotMilliseconds);
    }

    public void start() {
        window = new Window(c.windowWidth, c.windowHeight);

        // Initialize GA:
        Generation generation = new Generation(c.robotsPerGeneration);

        // Main algorithm
        for (int i = 0; i < c.generations; ++i) {
            long wait = 0;
            int FPS = 60;
            float waitTime = 1000 / FPS;
            long started = System.currentTimeMillis();

            // Create all the simulations and start them in their separate
            // threads
            generation.computeAll();

            // Create a simulation for a single chromosome
            Simulation presentationSimulation = generation.getSample();
            presentationSimulation.setExpire(false);
            presentationSimulation.setTimeStep(waitTime / 1000);
            presentationSimulation.setup();

            // Submit it to the renderer
            window.setSimulation(presentationSimulation);

            // Simulate and render the presentation simulation while real
            // computation is being done in the backend

            while (!generation.isDone()
                    || started + wait > System.currentTimeMillis()) {
                presentationSimulation.update();
                presentationSimulation.removeFinished();
                window.updateDisplay();

                try {
                    Thread.sleep((int) waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Evolve
            generation = generation.evolve();

            System.out.println("generation #" + (i + 1) + " done, took "
                    + (System.currentTimeMillis() - started) + " milliseconds");
        }

        exit();
    }

    public void exit() {
        System.out.println("Program done.");
        System.exit(0);
    }

}
