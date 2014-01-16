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
    // MEMO set in config
    public static int minNumLegs = 1;
    public static int maxNumLegs = 20;

    // public Body body;
    // public ArrayList<Leg> legs;

    public ArrayList<Shape> shapes;
    public ArrayList<Joint> joints;

}
