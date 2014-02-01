/**
 * 
 */
package org.rtevo.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.rtevo.common.Configuration;
import org.rtevo.genetics.Chromosome;
import org.rtevo.gui.Window;
import org.rtevo.simulation.Generation;
import org.rtevo.simulation.Robot;
import org.rtevo.simulation.Simulation;

import com.google.gson.Gson;

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
    private Gson gson = new Gson();

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

    private void load() {
        if (!c.save.equals("false")) {
            System.out.println("saving will have no effect");
        }

        int FPS = 60;
        float waitTime = 1000 / FPS;
        FileReader toLoad;

        try {
            toLoad = new FileReader(new File("chromosomes/" + c.load));

            Chromosome loadedChromosome = gson.fromJson(toLoad,
                    Chromosome.class);

            ArrayList<Chromosome> chromosomes = new ArrayList<Chromosome>();

            chromosomes.add(loadedChromosome);

            // Create a simulation for a single chromosome
            Simulation presentationSimulation = new Simulation(chromosomes,
                    c.gravity, waitTime / 1000);
            presentationSimulation.setExpire(false);
            presentationSimulation.setup();

            // Submit it to the renderer
            window.setSimulation(presentationSimulation);

            while (true) {
                presentationSimulation.update();
                window.updateDisplay();

                try {
                    Thread.sleep((int) waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

    }

    public void start() {
        if (c.GUI) {
            window = new Window(c.windowWidth, c.windowHeight);
        }

        if (!c.load.equals("false")) {
            load();
            exit();
        }

        // Initialize GA:
        Generation generation = new Generation(c.robotsPerGeneration);

        // Main algorithm
        for (int i = 0; i < c.generations; ++i) {
            // Create all the simulations and start them in their separate
            // threads
            long started = System.currentTimeMillis();
            generation.computeAll();

            if (!c.GUI) {
                int waitMillis = 100;

                while (!generation.isDone()) {
                    try {
                        Thread.sleep(waitMillis);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                long wait = 0;
                int FPS = 60;
                float waitTime = 1000 / FPS;

                // Create a simulation for a single chromosome
                Simulation presentationSimulation = generation.getSample();
                presentationSimulation.setExpire(false);
                presentationSimulation.setTimeStep(0.01f);
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
            }

            // Evolve
            generation = generation.evolve();

            System.out.println("generation #" + (i + 1) + " done, took "
                    + (System.currentTimeMillis() - started) + " milliseconds");
        }

        if (!c.save.equals("false")) {
            saveBest(generation.getBestChromosome());
        }

        exit();
    }

    // FIXME after a long simulation, some joints and parts had disconnected,
    // resulting in several separate constructions
    public void saveBest(Chromosome best) {
        File directory = new File("chromosomes");

        if (!directory.exists()) {
            directory.mkdir();
        }

        File saveFile = new File("chromosomes/" + c.save);

        try {
            PrintWriter writer = new PrintWriter(saveFile);
            writer.print(gson.toJson(best));
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        System.out.println("Program done.");
        System.exit(0);
    }

}
