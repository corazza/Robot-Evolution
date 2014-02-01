/**
 * 
 */
package org.rtevo.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.rtevo.genetics.Chromosome;
import org.rtevo.genetics.ChromosomeFactory;

class OpJobThreadFactory implements ThreadFactory {
    private int priority;
    private boolean daemon;
    private final String namePrefix;
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    public OpJobThreadFactory(int priority) {
        this(priority, false);
    }

    public OpJobThreadFactory(int priority, boolean daemon) {
        this.priority = priority;
        this.daemon = daemon;
        namePrefix = "jobpool-" + poolNumber.getAndIncrement() + "-thread-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, namePrefix + threadNumber.getAndIncrement());
        t.setDaemon(daemon);
        t.setPriority(priority);
        return t;
    }
}

/**
 * @author Jan Corazza
 * 
 */
public class Generation {
    private static ThreadPoolExecutor workerPool;
    private static int parallelSimulations;

    public static void configureWorkerPool(int parallelSimulations) {
        Generation.parallelSimulations = parallelSimulations;

        workerPool = new ThreadPoolExecutor(parallelSimulations,
                parallelSimulations, 100L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        workerPool.setThreadFactory(new OpJobThreadFactory(Thread.MAX_PRIORITY,
                false));
    }

    private int numSimulations;
    private List<Chromosome> chromosomes;
    private List<Result> results = new ArrayList<Result>();
    private List<Simulation> simulations;
    private List<Future<List<Result>>> futureSimulationResults = new ArrayList<Future<List<Result>>>();

    // configuration cache
    private static float gravity;
    private static float timeStep;

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
     * @param robotMilliseconds
     * @param chromosomes
     */
    public Generation(List<Chromosome> chromosomes) {
        this.chromosomes = chromosomes;
    }

    /**
     * Random generation
     * 
     * @param robotMilliseconds
     * @param numChromosomes
     */
    public Generation(int numChromosomes) {
        this(ChromosomeFactory.random(numChromosomes));
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
    public Generation evolve() {
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

        return new Generation(ChromosomeFactory.evolve(results));
    }

    /**
     * Returns a random sample from the current generation.
     * 
     * @param robotMilliseconds
     * @return
     */
    public Simulation getSample() {
        List<Chromosome> bestChromosomes = new ArrayList<Chromosome>();
        bestChromosomes.add(chromosomes.get(0));

        return new Simulation(bestChromosomes, gravity, timeStep);
    }

    public Chromosome getBestChromosome() {
        return chromosomes.get(0);
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
