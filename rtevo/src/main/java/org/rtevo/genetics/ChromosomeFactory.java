/**
 * 
 */
package org.rtevo.genetics;

import java.util.ArrayList;

import org.rtevo.util.RandomUtil;

/**
 * @author Jan Corazza
 * 
 */
public class ChromosomeFactory {
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

    public static Chromosome mutate(Chromosome chromosome) {
        //TODO vrati slican Chromosome
        return random();
    }

    public static Chromosome crossOver(Chromosome first, Chromosome second) {
        return random();
    }
}
