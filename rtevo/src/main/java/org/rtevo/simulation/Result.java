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
    public final Chromosome chromosome;
    public final double metersPassed;

    public Result(Chromosome chromosome, double metersPassed) {
        this.chromosome = chromosome;
        this.metersPassed = metersPassed;
    }

    @Override
    public int compareTo(Result other) {
        double difference = metersPassed - other.metersPassed;

        if (difference > 0) {
            return 1;
        } else if (difference < 0) {
            return -1;
        } else {
            return 0;
        }
    }

}
