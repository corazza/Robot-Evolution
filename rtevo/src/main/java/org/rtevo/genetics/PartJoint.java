/**
 * 
 */
package org.rtevo.genetics;

import org.rtevo.util.RandUtil;

/**
 * @author Jan Corazza & Luka Bubalo
 * 
 */
public class PartJoint {
    public static final float minAngularVelocity = -1f;
    public static final float maxAngularVelocity = 1f;
    
    public float rotateFrom;
    public float rotateTo;
    public float angularVelocity;

    public float percentOne;
    public float percentTwo;

    public Part partOne;
    public Part partTwo;

    public PartJoint() {
    }

    public PartJoint(PartJoint copy) {
        rotateFrom = copy.rotateFrom;
        rotateTo = copy.rotateTo;
        angularVelocity = copy.angularVelocity;
        percentOne = copy.percentOne;
        percentTwo = copy.percentTwo;

        partOne = new Part(copy.partOne);
        partTwo = new Part(copy.partTwo);
    }

    public PartJoint(Part partOne, Part partTwo) {
        this.partOne = partOne;
        this.partTwo = partTwo;
    }

    public static PartJoint random() {
        PartJoint partJoint = new PartJoint();

        partJoint.angularVelocity = RandUtil.random(0f, 1f);
        partJoint.rotateFrom = RandUtil.random(0f, 1f);
        partJoint.rotateTo = RandUtil.random(0f, 1f);

        if (partJoint.rotateFrom > partJoint.rotateTo) {
            float tmp = partJoint.rotateFrom;
            partJoint.rotateFrom = partJoint.rotateTo;
            partJoint.rotateTo = tmp;
        }

        partJoint.percentOne = RandUtil.random(0f, 1f);
        partJoint.percentTwo = RandUtil.random(0f, 1f);

        return partJoint;
    }

    @Override
    public String toString() {
        return "Joint [rotateFrom=" + rotateFrom + ", rotateTo=" + rotateTo
                + ", angularVelocity=" + angularVelocity + ", partOne="
                + partOne + ", partTwo=" + partTwo + ", percentOne="
                + percentOne + ", percentTwo=" + percentTwo + "]";
    }

}
