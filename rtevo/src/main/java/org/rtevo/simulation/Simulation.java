/**
 * 
 */
package org.rtevo.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.rtevo.genetics.Chromosome;
import org.rtevo.util.RandomUtil;

/**
 * Evaluates chromosomes. Encapsulates and represents a simulation.
 * 
 * @author Jan Corazza
 */
public class Simulation implements Callable<List<Result>> {
    // robots that are all in this same simulation
    private List<Chromosome> chromosomes;

    // array of results for each chromosome
    private List<Result> results = new ArrayList<Result>();

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
    public Simulation(List<Chromosome> chromosomes, int timeStep) {
        if (chromosomes.isEmpty()) {
            throw new IllegalArgumentException("There must be more than 0 chromosomes in the simulation.");
        }
        
        this.chromosomes = chromosomes;
        this.timeStep = timeStep;
    }

    // MEMO - FOR THREADS - the current implementation is not optimal because if
    // the user wants
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
    // MEMO check if this is compatible with JBox2D (the approach of using +int
    // milliseconds to advance the simulation)
    public synchronized void update(/* timeStep is known to the object */) {
        // MEMO actually simulate

        // search for failed, remove from chromosomes

        // add failed to results
                
      Chromosome finished = chromosomes.remove(0);
      Result finishedResult = new Result(finished, RandomUtil.random(0, 100));
      results.add(finishedResult);
    }

    public List<Result> simulate() {
        setup();
        
        while (!chromosomes.isEmpty()) {
            update();
        }
        
        return results;
    }

    /**
     * Returns an array of Renderable objects.
     */
    public synchronized void snapshot() {

    }

    public void addChromosome(Chromosome toAdd) {
        chromosomes.add(toAdd);
    }

    @Override
    public List<Result> call() {
        return simulate();
    }

    
    public List<Chromosome> getChromosomes() {
        return chromosomes;
    }

    public List<Result> getResults() {
        return results;
    }

    public int getTimeStep() {
        return timeStep;
    }
    
}
