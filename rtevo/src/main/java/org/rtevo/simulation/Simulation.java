/**
 * 
 */
package org.rtevo.simulation;

import java.util.ArrayList;

import org.rtevo.common.Result;
import org.rtevo.genome.Genome;

/**
 * @author Jan Corazza
 *
 */
public class Simulation {
    //robots that are all in this same simulation
    private ArrayList<Genome> genomes;
    
    public Simulation(ArrayList<Genome> robots) {
        this.genomes = robots;
    }
    
    public ArrayList<Result> start() {
        ArrayList<Result> results = new ArrayList<Result>(genomes.size());
        
        //TODO generate and simulate robots
        
        return results;
    }
}
