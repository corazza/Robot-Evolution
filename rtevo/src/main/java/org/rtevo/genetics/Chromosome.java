/**
 * 
 */
package org.rtevo.genetics;

import java.util.ArrayList;

import org.rtevo.util.GeomUtil;
import org.rtevo.util.RandUtil;

/**
 * Most fields are public for convenience.
 * 
 */
public class Chromosome {
    private static final int minShapes = 2;
    private static final int maxShapes = 20;
    private static final float mutateChance = 0.2f;

    public ArrayList<PartJoint> partJoints = new ArrayList<PartJoint>();
    public ArrayList<Part> parts = new ArrayList<Part>();

    /**
     * Default constructor
     */
    public Chromosome() {

    }

    /**
     * Copy constructor
     * 
     * @param copy
     *            - the chromosome to copy from
     */
    public Chromosome(Chromosome copy) {
        for (PartJoint partJoint : copy.partJoints) {
            PartJoint my = new PartJoint(partJoint);
            addPartJoint(my);
        }
    }

    private void addPartJoint(PartJoint toAdd) {
        partJoints.add(toAdd);
        parts.add(toAdd.partOne);
        parts.add(toAdd.partTwo);
    }

    /**
     * Represents a single mutation
     */
    private float mutation(float from, float to) {
        if (RandUtil.random(0f, 1f) < mutateChance) {
            return RandUtil.random(from, to);
        }

        return 0f;
    }

    // temporary debugging function
    private void correctSearchSpace() {

    }

    /**
     * Moves the chromosome across the search space.
     * 
     * @return a new mutated copy of the chromosome
     */
    public Chromosome mutate() {
        // TODO add new parts and joints

        // create a copy chromosome
        Chromosome mutated = new Chromosome(this);

//        System.out.println(mutated);

        for (PartJoint partJoint : mutated.partJoints) {
            // TODO define mutation ranges
            float rotateFromToMutate = mutation(-0.05f, 0.05f);
            float rotateToToMutate = mutation(-0.05f, 0.05f);
            float percentOneToMutate = mutation(-0.05f, 0.05f);
            float percentTwoToMutate = mutation(-0.05f, 0.05f);
            float angularVelocityToMutate = mutation(-0.05f, 0.05f);

            if (partJoint.rotateFrom + rotateFromToMutate >= 0
                    && partJoint.rotateFrom + rotateFromToMutate <= GeomUtil
                            .circle(1)) {
                partJoint.rotateFrom += rotateFromToMutate;
            }

            if (partJoint.rotateTo + rotateToToMutate >= 0
                    && partJoint.rotateTo + rotateToToMutate <= GeomUtil
                            .circle(1)) {
                partJoint.rotateTo += rotateToToMutate;
            }

            if (partJoint.percentOne + percentOneToMutate >= 0
                    && partJoint.percentOne + percentOneToMutate <= 1) {
                partJoint.percentOne += percentOneToMutate;
            }

            if (partJoint.percentTwo + percentTwoToMutate >= 0
                    && partJoint.percentTwo + percentTwoToMutate <= 1) {
                partJoint.percentTwo += percentTwoToMutate;
            }

            if (partJoint.angularVelocity + angularVelocityToMutate >= PartJoint.minAngularVelocity
                    && partJoint.angularVelocity + angularVelocityToMutate <= PartJoint.maxAngularVelocity) {
                partJoint.angularVelocity += angularVelocityToMutate;
            }
        }

        for (Part part : mutated.parts) {
            float widthToMutate = mutation(-0.02f, 0.02f);
            float heightToMutate = mutation(-0.02f, 0.02f);

            if (part.width + widthToMutate >= Part.minWidth
                    && part.width + widthToMutate <= Part.maxWidth) {
                part.width += widthToMutate;
            }

            if (part.height + heightToMutate >= Part.minHeight
                    && part.height + heightToMutate <= Part.maxHeight) {
                part.height += heightToMutate;
            }
        }

//        System.out.println(mutated);
//        System.out.println();

        mutated.correctSearchSpace();

        return mutated;
    }

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
