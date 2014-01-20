/**
 * 
 */
package org.rtevo.simulation;

import java.util.ArrayList;
import java.util.List;

import org.rtevo.genetics.Chromosome;
import org.rtevo.genetics.ChromosomeFactory;

/**
 * @author Jan Corazza
 * 
 */
public class Generation {
    private int numSimulations;
    private List<Chromosome> chromosomes;
    private List<Result> results = new ArrayList<Result>();

    /**
     * Random generation
     * 
     * @param numChromosomes
     */
    public Generation(int numChromosomes) {
        chromosomes = ChromosomeFactory.random(numChromosomes);
    }

    public Generation(List<Chromosome> chromosomes) {
        this.chromosomes = chromosomes;
    }

    /**
     * Computes the evolution of this generation and returns it.
     * 
     * @return the next (evolved) generation
     */
    public Generation nextGeneration() {
        if (results.size() != chromosomes.size()) {
            throw new IllegalStateException(
                    "Not all simulations have finished.");
        }

        return new Generation(ChromosomeFactory.evolve(results));
    }

    /**
     * Creates a number simulations out of an array of chromosomes.
     * 
     * @param allChromosomes
     *            array of chromosomes to be spread across several simulations
     * @param chromosomesPerSimulation
     *            how many chromosomes should be included in one simulation,
     *            last simulation will have allChromosomes.size() %
     *            chromosomesPerSimulation chromosomes less
     * @param timeStep
     * @return
     */
	public ArrayList<Simulation> getSimulations(int numSimulations,
			int timeStep, int getMillisecondsPerSimulation) {
        int numChromosomes = chromosomes.size();

        if (numSimulations > numChromosomes) {
            throw new IllegalArgumentException(
                    "The number of simulations must be lower than the number of chromosomes.");
        }

        int robotsPerSimulation = numChromosomes / numSimulations;
        ArrayList<Simulation> simulations = new ArrayList<Simulation>();

        for (int i = 0; i < numSimulations; ++i) {
            List<Chromosome> taken = new ArrayList<Chromosome>(chromosomes.subList(i
                    * robotsPerSimulation, i * robotsPerSimulation
                    + robotsPerSimulation));
            simulations.add(new Simulation(taken, timeStep, getMillisecondsPerSimulation));
        }

        int robotsLeft = numChromosomes % numSimulations;

        for (int i = 0; i < robotsLeft; ++i) {
            simulations.get(i).addChromosome(
                    chromosomes.get(numChromosomes - i - 1));
        }
        
        return simulations;
    }

    /**
     * To be called when a simulation is complete.
     * 
     * @param results
     *            some simulation's results
     */
    public void submitResults(List<Result> results) {
        this.results.addAll(results);
    }

    public int getNumSimulations() {
        return numSimulations;
    }

    public void setNumSimulations(int numSimulations) {
        this.numSimulations = numSimulations;
    }

    public List<Chromosome> getChromosomes() {
        return chromosomes;
    }

    public void setChromosomes(ArrayList<Chromosome> chromosomes) {
        this.chromosomes = chromosomes;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

}
