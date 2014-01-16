/**
 * 
 */
package org.rtevo.genetics;

import java.util.ArrayList;

import org.rtevo.util.RandomUtil;

/**
 * @author Jan Corazza
 * 
 */
public class ChromsomeFactory {
    public static Chromosome random() {
        Chromosome chromosome = new Chromosome();

        // construct legs
        chromosome.legs = new ArrayList<Leg>();
        int numLegs = RandomUtil.random(Chromosome.minNumLegs, Chromosome.maxNumLegs);

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

            chromosome.legs.add(leg);
        }

        // construct body
        chromosome.body = new Body();
        chromosome.body.width = RandomUtil.random(Body.minWidth, Body.maxWidth);
        chromosome.body.height = RandomUtil.random(Body.minHeight, Body.maxHeight);

        return chromosome;
    }

    public static Chromosome mutate(Chromosome chromosome) {
        //TODO vrati slican Chromosome
        return random();
    }

    public static Chromosome crossOver(Chromosome first, Chromosome second) {
        return random();
    }
}
