/**
 * 
 */
package org.rtevo.simulation;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.rtevo.genetics.Chromosome;

/**
 * @author Jan Corazza
 * 
 */
public class Simulation implements Callable<Result> {
    // robots that are all in this same simulation
    private ArrayList<Chromosome> chromosomes;
    private ArrayList<Result> results;
    // map Body -> Chromosome?
    private int timeStep = 10; // in milliseconds

    public Simulation(ArrayList<Chromosome> robots, int timeStep) {
        this.chromosomes = robots;
        this.timeStep = timeStep;
    }

    // FIXME the current implementation is not optimal because if the user wants
    // to render it he has to simulate it in his own thread himself because of
    // data sharing. Solution: Monitor object that has synchronized methods for
    // reading and writing what has to be rendered - PROBLEM: the r/w is
    // synchronized, but the actual objects in it might not be.

    /**
     * Generate physics objects and add them to the JBox2D world.
     */
    public void setup() {

    }

    /**
     * Advances the simulation.
     * 
     * @param timeStep
     *            the number of milliseconds to advance the simulation
     */
    // TODO check if this is compatible with JBox2D
    public void update(int timeStep) {
        // TODO actually simulate
    }

    /**
     * Scans the entire world and returns the results for the current state of
     * the simulation.
     * 
     * @return
     */
    public ArrayList<Result> generateResults() {
        return new ArrayList<Result>();
    }

    /**
     * Returns an array of Renderable objects.
     */
    public void snapshot() {

    }

    @Override
    public Result call() {

        // TODO just a loop that calls (update(timeStep)) all the time

        return new Result();
    }
}
