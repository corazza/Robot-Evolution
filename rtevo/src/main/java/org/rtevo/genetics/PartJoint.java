/**
 * 
 */
package org.rtevo.genetics;

import org.rtevo.util.RandUtil;

/**
 * @author Jan Corazza
 * 
 */
public class PartJoint {
    public float rotateFrom;
    public float rotateTo;
    public float angularVelocity;

    public Part partOne;
    public Part partTwo;

    public float percentOne;
    public float percentTwo;

    public PartJoint() {
    }

    public PartJoint(Part shapeOne, Part shapeTwo) {
        this.partOne = shapeOne;
        this.partTwo = shapeTwo;
    }

    public static PartJoint random() {
        PartJoint partJoint = new PartJoint();

        partJoint.angularVelocity = RandUtil.random(0.01f, 1f);
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
                + ", angularVelocity=" + angularVelocity + ", shapeOne="
                + partOne + ", shapeTwo=" + partTwo + ", percentOne="
                + percentOne + ", percentTwo=" + percentTwo + "]";
    }

}
