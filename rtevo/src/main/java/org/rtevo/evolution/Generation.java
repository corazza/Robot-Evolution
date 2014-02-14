/**
 * 
 */
package org.rtevo.evolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.rtevo.simulation.Result;
import org.rtevo.simulation.Simulation;
import org.rtevo.simulation.SimulationThreadFactory;
import org.rtevo.util.RandUtil;

public class Generation {
    private static ThreadPoolExecutor workerPool;
    private static int parallelSimulations;

    public static volatile int generationNumber = 0;

    public static void configureWorkerPool(int parallelSimulations) {
        Generation.parallelSimulations = parallelSimulations;

        workerPool = new ThreadPoolExecutor(parallelSimulations,
                parallelSimulations, 100L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        workerPool.setThreadFactory(new SimulationThreadFactory(
                Thread.MAX_PRIORITY, false));
    }

    private int numSimulations;
    private List<Chromosome> chromosomes;
    private List<Result> results = new ArrayList<Result>();
    private List<Simulation> simulations;
    private List<Future<List<Result>>> futureSimulationResults = new ArrayList<Future<List<Result>>>();
    private Chromosome previousBestChromosome;
    private Chromosome myBestChromosome;

    // configuration cache
    private static float gravity;
    private static float timeStep;
    private final static double random = 0.05;
    private final static double best = 0.1;

    public static float getGravity() {
        return gravity;
    }

    public static void setGravity(float gravity) {
        Generation.gravity = gravity;
    }

    public static float getTimeStep() {
        return timeStep;
    }

    public static void setTimeStep(float timeStep) {
        Generation.timeStep = timeStep;
    }

    /**
     * Generation from a list of chromosomes
     * 
     * @param robotSeconds
     * @param chromosomes
     */
    public Generation(List<Chromosome> chromosomes,
            Chromosome bestPreviousChromosome) {
        this.chromosomes = chromosomes;
        this.previousBestChromosome = bestPreviousChromosome;
        ++generationNumber;
    }

    /**
     * Random generation
     * 
     * @param robotSeconds
     * @param numChromosomes
     */
    public Generation(int numChromosomes) {
        this(Chromosome.random(numChromosomes), null);
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

    public ArrayList<Result> recordResults() {
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

        Collections.sort(results);

        myBestChromosome = new Chromosome(results.get(0).chromosome);

        return new ArrayList<Result>(results);
    }

    private static void normalize(List<Result> results, float unit) {
        for (Result result : results) {
            result.normalized = result.score / unit;
        }
    }

    private static float sum(List<Result> results) {
        float sum = 0;

        for (Result result : results) {
            sum += result.score;
        }

        return sum;
    }

    /**
     * Computes the evolution of this generation and returns it.
     * 
     * @return the next (evolved) generation
     */
    public Generation evolve() {
        normalize(results, sum(results));

        List<Chromosome> chromosomes = new ArrayList<Chromosome>();

        int useBest = (int) (best * results.size());
        int useRandom = Math.abs((int) (random * (results.size() - useBest)));

        for (int i = 0; i < useBest; ++i) {
            chromosomes.add(results.get(i).chromosome.mutate());
        }

        for (int i = 0; i < useRandom; ++i) {
            chromosomes.add(Chromosome.random());
        }

        while (chromosomes.size() != results.size()) {
            float sum = 0;
            float r = RandUtil.random();

            for (Result result : results) {
                sum += result.normalized;

                if (sum >= r) {
                    chromosomes.add(result.chromosome.mutate());
                    break;
                }
            }
        }

        return new Generation(chromosomes, myBestChromosome);
    }

    public Simulation getSample() {
        List<Chromosome> presentationChromosomes = new ArrayList<Chromosome>();

        // add previous best chromosome
        presentationChromosomes.add(getPreviousBestChromosome());
        // add random current chromosome
        presentationChromosomes.add(chromosomes.get(RandUtil.random(0,
                chromosomes.size())));

        return new Simulation(presentationChromosomes, gravity, timeStep);
    }

    public Chromosome getPreviousBestChromosome() {
        if (previousBestChromosome != null) {
            return previousBestChromosome;
        } else {
            return chromosomes.get(0);
        }
    }

    public Chromosome getMyBestChromosome() {
        if (myBestChromosome != null) {
            return myBestChromosome;
        } else {
            return getPreviousBestChromosome();
        }
    }

    public Result getBestResult() {
        return results.get(0);
    }

    /**
     * Divides all robots into a number of simulations that have to be computed
     * in order to advance to the next generation.
     * 
     * @param numSimulations
     *            the number of simulations to divide the chromosomes in
     * @return an array of simulations to compute
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
            simulations.add(new Simulation(taken, gravity, timeStep));
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

    public int numThreads() {
        return workerPool.getActiveCount();
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
