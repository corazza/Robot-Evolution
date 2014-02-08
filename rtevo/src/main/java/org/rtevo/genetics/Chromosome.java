/**
 * 
 */
package org.rtevo.genetics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.rtevo.util.GeomUtil;
import org.rtevo.util.RandUtil;

/**
 * Most fields are public for convenience.
 * 
 */
public class Chromosome {
    private static final int minShapes = 2;
    private static final int maxParts = 20;
    private static float mutationChance = 0.2f;

    public static void setMutationChance(float mutationChance) {
        Chromosome.mutationChance = mutationChance;
    }

    public ArrayList<PartJoint> partJoints = new ArrayList<PartJoint>();

    // public ArrayList<Part> parts = new ArrayList<Part>();

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
        HashMap<Integer, Part> used = new HashMap<Integer, Part>();

        for (PartJoint partJoint : copy.partJoints) {
            // copy numerical values but not parts
            PartJoint my = new PartJoint(partJoint);

            if (used.containsKey(partJoint.partOne.idm)) {
                my.partOne = used.get(partJoint.partOne.idm);
            } else {
                my.partOne = new Part(partJoint.partOne);
                used.put(partJoint.partOne.idm, my.partOne);
            }

            if (used.containsKey(partJoint.partTwo.idm)) {
                my.partTwo = used.get(partJoint.partTwo.idm);
            } else {
                my.partTwo = new Part(partJoint.partTwo);
                used.put(partJoint.partTwo.idm, my.partTwo);
            }

            partJoints.add(my);
        }
    }

    /**
     * Represents a single mutation
     */
    private float mutation(float from, float to) {
        if (RandUtil.random(0f, 1f) < mutationChance) {
            return RandUtil.random(from, to);
        }

        return 0f;
    }

    private List<Part> getPartList() {
        HashSet<Part> partSet = new HashSet<Part>();

        for (PartJoint partJoint : partJoints) {
            partSet.addAll(partJoint.getPartList());
        }

        ArrayList<Part> partList = new ArrayList<Part>(partSet);

        return partList;
    }

    /**
     * Moves the chromosome across the search space.
     * 
     * @return a new mutated copy of the chromosome
     */
    public Chromosome mutate() {
        // create a copy chromosome
        Chromosome mutated = new Chromosome(this);

        for (PartJoint partJoint : mutated.partJoints) {
            float rotateFromToMutate = mutation(-0.05f, 0.05f);
            float rotateToToMutate = mutation(-0.05f, 0.05f);
            float percentOneToMutate = mutation(-5f, 5f);
            float percentTwoToMutate = mutation(-5f, 5f);
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

        List<Part> partList = mutated.getPartList();

        for (Part part : partList) {
            float widthToMutate = mutation(-0.05f, 0.05f);
            float heightToMutate = mutation(-0.05f, 0.05f);

            if (part.width + widthToMutate >= Part.minWidth
                    && part.width + widthToMutate <= Part.maxWidth) {
                part.width += widthToMutate;
            }

            if (part.height + heightToMutate >= Part.minHeight
                    && part.height + heightToMutate <= Part.maxHeight) {
                part.height += heightToMutate;
            }
        }

        if (mutationChance / 10 > RandUtil.random(0f, 1f)
                && partList.size() > 2) {
            mutated.removeRandomPart();
        }

        if (mutationChance / 10 > RandUtil.random(0f, 1f)
                && partList.size() < Chromosome.maxParts) {
            mutated.addRandomPart();
        }

        return mutated;
    }

    private Part getRandomExistingPart() {
        PartJoint connector = partJoints.get(RandUtil.random(0,
                partJoints.size()));
        return RandUtil.random() < 0.5 ? connector.partOne : connector.partTwo;
    }

    private void addRandomPart() {
        Part newPart = Part.random();
        Part toConnect = getRandomExistingPart();

        PartJoint connectingJoint = PartJoint.random(newPart, toConnect);

        partJoints.add(connectingJoint);
    }

    private void removeRandomPart() {
        Part toRemove = getRandomExistingPart();

        // don't remove it if it's connected to multiple other parts
        if (toRemove.partJoints.size() != 1) {
            return;
        }

        // remove the single joint and the single part
        partJoints.remove(toRemove.partJoints.get(0));
    }

    // TODO rotate to and rotate from must not be more than 0.5 appart
    // TODO limit max torque, but increase base
    // TODO start with premade structure
    public static Chromosome random() {
        Chromosome chromosome = new Chromosome();

        // initial random part pair
        Part partOne = Part.random();
        Part partTwo = Part.random();
        PartJoint initialPartJoint = PartJoint.random(partOne, partTwo);
        chromosome.partJoints.add(initialPartJoint);

        for (int i = 0; i < RandUtil.random(minShapes, maxParts - 2); ++i) {
            chromosome.addRandomPart();
        }

        return chromosome;
    }

    public static List<Chromosome> random(int n) {
        ArrayList<Chromosome> chromosomes = new ArrayList<Chromosome>();

        for (int i = 0; i < n; ++i) {
            chromosomes.add(Chromosome.random());
        }

        return chromosomes;
    }

    @Override
    public String toString() {
        return "Chromosome [joints=" + partJoints + "]";
    }

}
