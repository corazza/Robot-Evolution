/**
 * 
 */
package org.rtevo.main;

import java.util.ArrayList;

import org.rtevo.simulation.Worker;
import org.rtevo.common.Result;

/**
 * Returns the work that needs to be completed.
 * 
 * @author Jan Corazza
 */
// FIXME Controller class is bad design, convert to Generation. Generation has
// next(), ArrayList of genomes, methods for returning workers
public class Controller {
    /**
     * Creates the workers for the initial generation.
     * 
     * @param numRobots
     * @return
     */
    public static ArrayList<Worker> getInitialGenerationWorkers(int numRobots) {
        return new ArrayList<Worker>();
    }

    /**
     * Computes the required work based on previous results.
     * 
     * @param previousResults
     * @return ArrayList of workers needed to advance the simulation
     */
    public static ArrayList<Worker> getWorkers(ArrayList<Result> previousResults) {
        return new ArrayList<Worker>();
    }

}
