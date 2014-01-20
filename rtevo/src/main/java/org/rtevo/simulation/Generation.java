/**
 * 
 */
package org.rtevo.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    private ExecutorService workerPool;
    private List<Simulation> simulations;
    private List<Future<List<Result>>> futureSimulationResults = new ArrayList<Future<List<Result>>>();

    // configuration cache
    private int timeStep;
    private int robotMilliseconds;
    private int parallelSimulations;

    /**
     * Generation from a list of chromosomes
     * 
     * @param timeStep
     * @param robotMilliseconds
     * @param chromosomes
     */
    public Generation(int timeStep, int robotMilliseconds,
            int parallelSimulations, List<Chromosome> chromosomes) {
        this.timeStep = timeStep;
        this.chromosomes = chromosomes;
        this.parallelSimulations = parallelSimulations;

        workerPool = Executors.newCachedThreadPool();
    }

    /**
     * Random generation
     * 
     * @param timeStep
     * @param robotMilliseconds
     * @param numChromosomes
     */
    public Generation(int timeStep, int robotMilliseconds,
            int parallelSimulations, int numChromosomes) {
        this(timeStep, robotMilliseconds, parallelSimulations,
                ChromosomeFactory.random(numChromosomes));
    }

    /**
     * Returns true if all results have been submitted.
     * 
     * @return
     */
    public boolean isDone() {
        for (Future<List<Result>> futureSimulationResult : futureSimulationResults) {
            if (!futureSimulationResult.isDone()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Computes the evolution of this generation and returns it.
     * 
     * @return the next (evolved) generation
     */
    public Generation nextGeneration() {
        for (Future<List<Result>> futureSimulationResult : futureSimulationResults) {
            if (!futureSimulationResult.isDone()) {
                throw new IllegalStateException(
                        "Not all simulations have finished.");
            }

            try {
                List<Result> simulationResults = futureSimulationResult.get();
                results.addAll(simulationResults);
            } catch (Exception e) {
                throw new IllegalStateException("Simulations could not finish");
            }
        }

        if (results.size() != chromosomes.size()) {
            throw new IllegalStateException(
                    "Not all simulations have finished.");
        }

        return new Generation(timeStep, robotMilliseconds, parallelSimulations,
                ChromosomeFactory.evolve(results));
    }

    /**
     * Returns a random sample from the current generation.
     * 
     * @param timeStep
     * @param robotMilliseconds
     * @return
     */
    public Simulation getSample() {
        // CHECKLATER if chromosomes.get(0) is indeed the best one
        List<Chromosome> bestChromosomes = new ArrayList<Chromosome>();
        bestChromosomes.add(chromosomes.get(0));

        return new Simulation(bestChromosomes, timeStep, robotMilliseconds);
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
    public ArrayList<Simulation> getSimulations(int numSimulations) {
        int numChromosomes = chromosomes.size();

        if (numSimulations > numChromosomes) {
            throw new IllegalArgumentException(
                    "The number of simulations must be lower than the number of chromosomes.");
        }

        int robotsPerSimulation = numChromosomes / numSimulations;
        ArrayList<Simulation> simulations = new ArrayList<Simulation>();

        for (int i = 0; i < numSimulations; ++i) {
            List<Chromosome> taken = new ArrayList<Chromosome>(
                    chromosomes.subList(i * robotsPerSimulation, i
                            * robotsPerSimulation + robotsPerSimulation));
            simulations.add(new Simulation(taken, timeStep, robotMilliseconds));
        }

        int robotsLeft = numChromosomes % numSimulations;

        for (int i = 0; i < robotsLeft; ++i) {
            simulations.get(i).addChromosome(
                    chromosomes.get(numChromosomes - i - 1));
        }

        return simulations;
    }

    public void computeAll() {
        simulations = getSimulations(parallelSimulations);

        for (Simulation simulation : simulations) {
            futureSimulationResults.add(workerPool.submit(simulation));
        }
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

    public int getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(int timeStep) {
        this.timeStep = timeStep;
    }

    public int getRobotMilliseconds() {
        return robotMilliseconds;
    }

    public void setRobotMilliseconds(int robotMilliseconds) {
        this.robotMilliseconds = robotMilliseconds;
    }

}
