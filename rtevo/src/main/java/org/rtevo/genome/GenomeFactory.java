/**
 * 
 */
package org.rtevo.genome;

import java.util.ArrayList;

import org.rtevo.util.RandomUtil;

/**
 * @author Jan Corazza
 * 
 */
public class GenomeFactory {
    public static Genome random() {
        Genome genome = new Genome();

        // construct legs
        genome.legs = new ArrayList<Leg>();
        int numLegs = RandomUtil.random(Genome.minNumLegs, Genome.maxNumLegs);

        for (int i = 0; i < numLegs; ++i) {
            Leg leg = new Leg();
            int numJoints = RandomUtil.random(Leg.minNumJoints,
                    Leg.maxNumJoints);

            for (int j = 0; j < numJoints; ++j) {
                Joint joint = new Joint();
                joint.width = RandomUtil.random(Joint.minWidth, Joint.maxWidth);
                joint.height = RandomUtil.random(Joint.minHeight,
                        Joint.maxHeight);
                leg.joints.add(joint);
            }

            genome.legs.add(leg);
        }

        // construct body
        genome.body = new Body();
        genome.body.width = RandomUtil.random(Body.minWidth, Body.maxWidth);
        genome.body.height = RandomUtil.random(Body.minHeight, Body.maxHeight);

        return genome;
    }

    public static Genome copy(Genome genome) {
        return random();
    }

    public static Genome crossOver(Genome first, Genome second) {
        return random();
    }
}
