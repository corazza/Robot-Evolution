/**
 * 
 */
package org.rtevo.simulation;

import java.util.ArrayList;

import org.rtevo.common.Result;

/**
 * Computes a part of a generation.
 * 
 * @author Jan Corazza
 */
public class Worker implements Runnable {
    private ArrayList<Simulation> simulations;
    private ArrayList<Result> results;

    public Worker(ArrayList<Simulation> simulations) {
        this.simulations = simulations;
    }

    /**
     * Computes a snapshot of the current simulation which includes robot
     * shapes, positions, etc.
     * 
     * @return ArrayList of things which need to be drawn
     */
    // TODO ^^^what are these things?^^^ think of some JBox2D -> visual map,
    // possibly search online for JBox2D renderers and use their types for this.
    // Possibly static methods for converting coordinates (simulation ->
    // visual)
    public ArrayList<Result> getSnapshot() {
        return new ArrayList<Result>();
    }

    @Override
    public void run() {
        for (int i = 0; i < simulations.size(); ++i) {
            ArrayList<Result> simulationResults = simulations.get(i).start();
            results.addAll(simulationResults);
        }
    }

    public ArrayList<Result> getResults() {
        return results;
    }

}
