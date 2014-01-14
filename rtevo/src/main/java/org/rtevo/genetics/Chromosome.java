/**
 * 
 */
package org.rtevo.genetics;

import java.util.ArrayList;

/**
 * Most fields are public for convenience.
 * 
 * @author Jan Corazza
 */
public class Chromosome {
    // TODO set in config
    public static int minNumLegs = 1;
    public static int maxNumLegs = 20;

    public ArrayList<Leg> legs;
    public Body body;
}
