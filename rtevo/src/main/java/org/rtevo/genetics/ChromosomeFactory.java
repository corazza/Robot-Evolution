/**
 * 
 */
package org.rtevo.genetics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rtevo.simulation.Result;
import org.rtevo.util.RandUtil;

/**
 * @author Jan Corazza & Luka Bubalo
 * 
 */
public class ChromosomeFactory {
    // percentage of the generation on the bottom that will be excluded from the
    // next generation
    private static double cutoff = 20;

    public static List<Chromosome> random(int n) {
        ArrayList<Chromosome> chromosomes = new ArrayList<Chromosome>();

        for (int i = 0; i < n; ++i) {
            chromosomes.add(Chromosome.random());
        }

        return chromosomes;
    }

    // FIXME new evolve function, think about this, after completing the mutate
    // function
    public static List<Chromosome> evolve(List<Result> results) {
        // 1. sort the array based on metersPassed
        // 2. remove the below ChromosomeFactory.cutoff percent
        // 3. select ChromosomeFactory.cutoff percent from the remaining
        // chromosomes based on their metersPassed
        // 4. mutate the selected chromosomes and add the mutations to the list

        int originalSize = results.size();
        int remove = (int) (cutoff / 100.0 * originalSize);

        List<Result> myResults = new ArrayList<Result>(results);

        // best results first
        Collections.sort(myResults);

        System.out.println(myResults);

        // remove the worst cutoff percent
        if (originalSize > remove) {
            // no need to copy since no modification is done
            myResults = myResults.subList(0, originalSize - remove);
        }

        List<Chromosome> chromosomes = new ArrayList<Chromosome>();

        for (Result result : myResults) {
            chromosomes.add(result.chromosome.mutate());
        }

        while (chromosomes.size() != originalSize) {
            int outreach = RandUtil.random(1, myResults.size());
            int need = originalSize - chromosomes.size();

            if (outreach > need) {
                outreach = need;
            }

            List<Result> toAdd = myResults.subList(0, outreach);

            for (Result result : toAdd) {
                chromosomes.add(result.chromosome.mutate());
            }
        }

        return chromosomes;
    }

    public static Chromosome crossover(Chromosome chromofirst,
            Chromosome chromosecond) {

        return null;
    }

}
