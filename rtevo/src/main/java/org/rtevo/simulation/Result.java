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
    public double metersPassed;
    public Chromosome chromosome;

    public int compareTo(Result other) {
        return (int) (metersPassed - other.metersPassed);
    }
}
