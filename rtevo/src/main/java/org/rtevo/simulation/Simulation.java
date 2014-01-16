/**
 * 
 */
package org.rtevo.simulation;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.rtevo.genetics.Chromosome;

/**
 * Evaluates chromosomes. Encapsulates and represents a simulation.
 * 
 * @author Jan Corazza
 */
public class Simulation implements Callable<ArrayList<Result>> {
    // robots that are all in this same simulation
    private ArrayList<Chromosome> chromosomes;

    // array of results for each chromosome
    private ArrayList<Result> results;

    // TODO map Body -> Chromosome?

    private int timeStep = 10; // in milliseconds

    /**
     * 
     * 
     * @param chromosomes
     *            list of all the chromosomes that should be evaluated by this
     *            simulation
     * @param timeStep
     *            the number of milliseconds that each update lasts
     */
    public Simulation(ArrayList<Chromosome> chromosomes, int timeStep) {
        this.chromosomes = chromosomes;
        this.timeStep = timeStep;
    }

    // FIXME the current implementation is not optimal because if the user wants
    // to render it he has to simulate it in his own thread himself because of
    // data sharing. Solution: Monitor object that has synchronized methods for
    // reading and writing what has to be rendered - PROBLEM: the r/w is
    // synchronized, but the actual objects in it might not be - THEY PROBABLY
    // ARE, but ask SO. They WILL BE if using BlockingQueue. Currently
    // unimportant.

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
    // TODO check if this is compatible with JBox2D (the approach of using +int
    // milliseconds to advance the simulation)
    public void update(/* timeStep is known to the object */) {
        // TODO actually simulate
        box2d.update(timeStep);
        
        //search for failed, remove from chromosomes
        
        //add failed to results
        
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

    public ArrayList<Result> simulate() {
        setup();

        while (!chromosomes.isEmpty()) {
            update();
        }

        return generateResults();
    }

    @Override
    public ArrayList<Result> call() {
        return simulate();
    }

}
