package org.rtevo.simulation;

import java.util.HashMap;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.rtevo.genetics.Chromosome;
import org.rtevo.genetics.Part;
import org.rtevo.genetics.PartJoint;
import org.rtevo.util.GeomUtil;
import org.rtevo.util.RandUtil;

public class Robot {
    private World world;
    private HashMap<Part, Body> partToBody = new HashMap<Part, Body>();

    public Robot(Chromosome chromosome, World world) {
        // TODO correctly position the parts and angles

        this.world = world;

        for (PartJoint partJoint : chromosome.partJoints) {
            if (!partToBody.containsKey(partJoint.partOne)) {
                partToBody.put(partJoint.partOne, setPart(partJoint.partOne));
            }

            if (!partToBody.containsKey(partJoint.partTwo)) {
                partToBody.put(partJoint.partTwo, setPart(partJoint.partTwo));
            }

            setJoint(partJoint);
        }
    }

    private Body setPart(Part part) {
        // body definition
        BodyDef bd = new BodyDef();
        bd.position.set(0f, -10f);
        bd.angle = 0f;
//        bd.angularDamping = 0.5f;
        bd.type = BodyType.DYNAMIC;

        // define shape of the body.
        PolygonShape Shape = new PolygonShape();
        Shape.setAsBox(part.width / 2, part.height / 2);

        // define fixture of the body.
        FixtureDef fd = new FixtureDef();
        Filter filter = new Filter();
        filter.groupIndex = -1;
        fd.filter = filter;
        fd.shape = Shape;
        fd.density = 0.5f;
        fd.friction = 0.3f;
        fd.restitution = 0.5f;

        // create the body and add fixture to it
        Body body = world.createBody(bd);
        body.createFixture(fd);
        body.setUserData(new PartUserData());

        return body;
    }

    private void setJoint(PartJoint partJoint) {
        Body bodyOne = partToBody.get(partJoint.partOne);
        Body bodyTwo = partToBody.get(partJoint.partTwo);

        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = bodyOne;
        jointDef.bodyB = bodyTwo;

        jointDef.localAnchorA = partJoint.partOne
                .getAnchor(partJoint.percentOne);
        jointDef.localAnchorB = partJoint.partTwo
                .getAnchor(partJoint.percentTwo);

        // rotation
        jointDef.lowerAngle = GeomUtil.circle(partJoint.rotateFrom);
        jointDef.upperAngle = GeomUtil.circle(partJoint.rotateTo);
        jointDef.enableLimit = true;
        jointDef.maxMotorTorque = 10.0f; // TODO limit maximum torque
        jointDef.motorSpeed = GeomUtil.circle(partJoint.angularVelocity);
        jointDef.enableMotor = true;

        world.createJoint(jointDef);
    }

    public float getDistance() {
        // TODO getDistance

        return RandUtil.random(0f, 100f);
    }

}
