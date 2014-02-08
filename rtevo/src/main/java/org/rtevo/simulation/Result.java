/**
 * 
 */
package org.rtevo.simulation;

import org.rtevo.evolution.Chromosome;

/**
 * @author Jan Corazza
 * 
 */
public class Result implements Comparable<Result> {
    public final Chromosome chromosome;
    public float score;
    public float normalized;

    public Result(Chromosome chromosome, float metersPassed) {
        this.chromosome = chromosome;
        this.score = metersPassed >= 0 ? metersPassed : 0;
    }

    public int compareTo(Result other) {
        double difference = score - other.score;

        if (difference < 0) {
            return 1;
        } else if (difference > 0) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return score + "";
    }

}
