/**
 * 
 */
package org.rtevo.simulation;

import org.rtevo.genetics.Chromosome;

/**
 * @author Jan Corazza
 * 
 */
public class Result implements Comparable<Result> {
    public Chromosome chromosome;
    public double metersPassed;
    
    public Result(Chromosome chromosome, double metersPassed) {
        this.chromosome = chromosome;
        this.metersPassed = metersPassed;
    }

    public int compareTo(Result other) {
        return (int) (metersPassed - other.metersPassed);
    }
    
}
