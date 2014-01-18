/**
 * 
 */
package org.rtevo.genetics;

import java.util.ArrayList;
import java.util.Collections;

import org.rtevo.simulation.Result;
import org.rtevo.util.RandomUtil;

/**
 * @author Jan Corazza
 * 
 */
public class ChromosomeFactory {
    // percentage of the generation on the bottom that will be excluded from the
    // next generation
    private static double cutoff = 20;

    /**
     * Returns a random chromosome in some limits
     * 
     * @return
     */
    public static Chromosome random() {
        Chromosome chromosome = new Chromosome();

        return chromosome;
    }

    public static ArrayList<Chromosome> random(int n) {
        ArrayList<Chromosome> chromosomes = new ArrayList<Chromosome>();

        for (int i = 0; i < n; ++i) {
            Chromosome chromosome = new Chromosome();
            chromosomes.add(chromosome);
        }

        return chromosomes;
    }

    public static ArrayList<Chromosome> evolve(ArrayList<Result> results) {
        // 1. sort the array based on metersPassed
        // 2. remove the below ChromosomeFactory.cutoff percent
        // 3. select ChromosomeFactory.cutoff percent from the remaining
        // chromosomes based on their metersPassed
        // 4. mutate the selected chromosomes and add the mutations to the list

        int originalSize = results.size();
        int remove = (int) (cutoff / 100.0 * originalSize);

        ArrayList<Result> myResults = (ArrayList<Result>) results.clone();

        // best results first
        Collections.sort(myResults);

        // remove the worst cutoff percent
        if (originalSize > remove) {
            myResults = (ArrayList<Result>) myResults.subList(0, originalSize
                    - remove);
        }

        ArrayList<Chromosome> chromosomes = new ArrayList<Chromosome>();

        for (Result result : myResults) {
            chromosomes.add(result.chromosome);
        }

        while (chromosomes.size() != originalSize) {
            int outreach = RandomUtil.random(1, myResults.size());

            ArrayList<Result> toAdd = (ArrayList<Result>) myResults.subList(0,
                    outreach);

            for (Result result : toAdd) {
                chromosomes.add(mutate(result.chromosome));
            }
        }

        return chromosomes;
    }

    /**
     * Returns a mutated chromosome.
     * 
     * @param chromosome
     *            Chromosome object to mutate
     * @return new Chromosome object that has been mutated from 1st parameter
     */
    private static Chromosome mutate(Chromosome chromosome) {
        // TODO vrati slican Chromosome
        return random();
    }

}
