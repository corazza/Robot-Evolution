/**
 * 
 */
package org.rtevo.genetics;

import java.util.ArrayList;

import org.rtevo.util.RandUtil;

/**
 * Most fields are public for convenience.
 * 
 */
public class Chromosome {
    private static final int minShapes = 2;
    private static final int maxShapes = 50;

    public ArrayList<PartJoint> partJoints = new ArrayList<PartJoint>();
    public ArrayList<Part> parts = new ArrayList<Part>();

    public static Chromosome random() {
        Chromosome chromosome = new Chromosome();

        for (int i = 0; i < RandUtil.random(minShapes, maxShapes); ++i) {
            Part newShape = Part.random();

            if (chromosome.parts.size() > 0) {
                int index = RandUtil.random(0, chromosome.parts.size());
                Part toConnect = chromosome.parts.get(index);

                PartJoint connectingJoint = PartJoint.random();

                connectingJoint.partOne = newShape;
                connectingJoint.partTwo = toConnect;

                chromosome.partJoints.add(connectingJoint);
                chromosome.parts.add(toConnect);
            }

            chromosome.parts.add(newShape);
        }

        return chromosome;
    }

    @Override
    public String toString() {
        return "Chromosome [joints=" + partJoints + "]";
    }

}
