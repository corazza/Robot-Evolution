/**
 * 
 */
package org.rtevo.evolution;

import java.util.ArrayList;
import java.util.List;

import org.rtevo.util.RandUtil;

public class PartJoint {
    public static final float maxAbsAngularVelocity = 1f;

    private float pointA;
    private float pointB;

    // starting angular velocity for a joint
    private float angularVelocity;

    private float percentOne;
    private float percentTwo;

    public Part partOne;
    public Part partTwo;

    public PartJoint() {
    }

    /**
     * Creates a copy from one PartJoint, but without the shapes
     * 
     * Since multiple PartJoints point to the same shapes that used to cause
     * duplicates.
     * 
     * @param copy
     *            the PartJoint to copy from.
     */
    public PartJoint(PartJoint copy) {
        pointA = copy.pointA;
        pointB = copy.pointB;
        angularVelocity = copy.angularVelocity;
        percentOne = copy.percentOne;
        percentTwo = copy.percentTwo;
    }

    public List<Part> getPartList() {
        ArrayList<Part> partList = new ArrayList<Part>();
        partList.add(partOne);
        partList.add(partTwo);
        return partList;
    }

    public static PartJoint random(Part one, Part two) {
        PartJoint partJoint = new PartJoint();

        partJoint.angularVelocity = RandUtil.random(maxAbsAngularVelocity,
                -maxAbsAngularVelocity);

        partJoint.setPointA(RandUtil.random(0f, 1f));
        partJoint.setPointB(partJoint.pointA + RandUtil.random(-0.5f, 0.5f));

        partJoint.percentOne = RandUtil.random(0f, 1f);
        partJoint.percentTwo = RandUtil.random(0f, 1f);

        partJoint.partOne = one;
        partJoint.partTwo = two;

        return partJoint;
    }

    private float checkBetweenZeroOne(float a) {
        if (a > 1) {
            return 1;
        } else if (a < 0) {
            return 0;
        }

        return a;
    }

    public float getPointA() {
        return pointA;
    }

    public float getPointB() {
        return pointB;
    }

    public void setPointA(float pointA) {
        pointA = checkBetweenZeroOne(pointA);
        this.pointA = pointA;
    }

    public void changePointA(float change) {
        setPointA(pointA + change);
    }

    public void setPointB(float pointB) {
        pointB = checkBetweenZeroOne(pointB);
        this.pointB = pointB;
    }

    public void changePointB(float change) {
        setPointB(pointB + change);
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        if (angularVelocity > maxAbsAngularVelocity) {
            angularVelocity = maxAbsAngularVelocity;
        } else if (angularVelocity < -maxAbsAngularVelocity) {
            angularVelocity = -maxAbsAngularVelocity;
        }

        this.angularVelocity = angularVelocity;
    }

    public void changeAngularVelocity(float change) {
        setAngularVelocity(angularVelocity + change);
    }

    public float getPercentOne() {
        return percentOne;
    }

    public void setPercentOne(float percentOne) {
        if (percentOne < 0) {
            percentOne = 0;
        } else if (percentOne > 1) {
            percentOne = 1;
        }

        this.percentOne = percentOne;
    }

    public void changePercentOne(float change) {
        setPercentOne(percentOne + change);
    }

    public float getPercentTwo() {
        return percentTwo;
    }

    public void setPercentTwo(float percentTwo) {
        if (percentTwo < 0) {
            percentTwo = 0;
        } else if (percentTwo > 1) {
            percentTwo = 1;
        }

        this.percentTwo = percentTwo;
    }

    public void changePercentTwo(float change) {
        setPercentTwo(percentTwo + change);
    }

    @Override
    public String toString() {
        return "PartJoint [pointA=" + pointA + ", pointB=" + pointB
                + ", angularVelocity=" + angularVelocity + ", percentOne="
                + percentOne + ", percentTwo=" + percentTwo + ", partOne="
                + partOne + ", partTwo=" + partTwo + "]";
    }
}
