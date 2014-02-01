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

        // remove the worst cutoff percent
        if (originalSize > remove) {
            // no need to copy since no modification is done
            myResults = myResults.subList(0, originalSize - remove);
        }

        List<Chromosome> chromosomes = new ArrayList<Chromosome>();

        for (Result result : myResults) {
            chromosomes.add(result.chromosome);
        }

        while (chromosomes.size() != originalSize) {
            int outreach = RandUtil.random(1, myResults.size());
            int need = originalSize - chromosomes.size();

            if (outreach > need) {
                outreach = need;
            }

            List<Result> toAdd = myResults.subList(0, outreach);

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

    public static Chromosome mutate(Chromosome chromosome) {
        //making new chromosome which will be mutated
    	Chromosome mutated = new Chromosome();
        for( PartJoint i : chromosome.partJoints ) { 
        	PartJoint partJ = i;
        	mutated.partJoints.add(partJ);
        }
        
        for( Part i: chromosome.parts) {
        	Part part = i;
        	mutated.parts.add(part);
        }
        
        //iterating through partJoints arraylist
        for( PartJoint i : mutated.partJoints ) {
        	 //randomization genes
        	 float rotateFromToMutate = RandUtil.random(-0.05f, 0.05f);
        	 float rotateToToMutate = RandUtil.random(-0.05f, 0.05f);
        	 float angularVelocityToMutate = RandUtil.random(-0.05f, 0.05f);
        	 float percentOneToMutate = RandUtil.random(-0.05f, 0.05f);
        	 float percentTwoToMutate = RandUtil.random(-0.05f, 0.05f);
        	 
        	 //contition of mutation
        	 if( RandUtil.random(0f, 1f) < 0.2f ) {
        		
        		if( i.rotateFrom + rotateFromToMutate > 0 ) { i.rotateFrom += rotateFromToMutate;  }
        		if( i.rotateTo + rotateToToMutate > 0 ) { i.rotateTo += rotateToToMutate;  }
        		if( i.percentOne + percentOneToMutate > 0 && i.percentOne + percentOneToMutate < 1 ) { 
        			i.percentOne += percentOneToMutate; 
        		}
        		if( i.percentTwo + percentTwoToMutate > 0 && i.percentTwo + percentTwoToMutate < 1 ) { 
        			i.percentTwo += percentTwoToMutate; 
        		}
        		
        		i.angularVelocity += angularVelocityToMutate;
        		
        		}
        	}
        
        for ( Part i : mutated.parts ) {
        	
        	float widthToMutate = RandUtil.random(-0.02f, 0.02f);
        	float heightToMutate = RandUtil.random(-0.02f, 0.02f);
        	
        	 if( RandUtil.random(0f, 1f) < 0.2f ) {
        		 if( i.width + widthToMutate < 2f && i.width + widthToMutate > 0.5f ) {
        			 i.width += widthToMutate;
        		 }
        		 if( i.height + heightToMutate < 2f && i.height + heightToMutate > 0.5f ) {
        			 i.height += heightToMutate;
        		 }
        	 }
        	
        	
        }
        
        return mutated;
    }

    public static Chromosome crossover(Chromosome chromofirst, Chromosome chromosecond) {
    	
    	return null;
    }
   
}
