package org.rtevo.simulation;

import java.util.ArrayList;
import java.util.HashMap;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.rtevo.common.Vector;
import org.rtevo.evolution.Chromosome;
import org.rtevo.evolution.Part;
import org.rtevo.evolution.PartJoint;
import org.rtevo.util.GeomUtil;

public class Robot {
    private World world;
    private Chromosome chromosome;
    private HashMap<Part, Body> partToBody = new HashMap<Part, Body>();
    private ArrayList<Body> bodies = new ArrayList<Body>();
    private ArrayList<Joint> joints = new ArrayList<Joint>();

    private float maxDistance = -1000;
    private float timer;
    private float restTimer;

    private static long robotSeconds;
    private static long restSeconds;

    public static void setRobotMilliseconds(long robotSeconds) {
        Robot.robotSeconds = robotSeconds;
        Robot.restSeconds = Math.min(10, robotSeconds / 2);
    }

    public Robot(Chromosome chromosome, World world) {
        // asdf correctly position the parts and angles
        this.world = world;
        this.chromosome = chromosome;

        for (PartJoint partJoint : chromosome.partJoints) {
            // if the bodies for the two parts haven't already been generated
            if (!partToBody.containsKey(partJoint.partOne)) {
                Body bodyOne = setPart(partJoint.partOne);
                bodies.add(bodyOne);
                partToBody.put(partJoint.partOne, bodyOne);
            }

            if (!partToBody.containsKey(partJoint.partTwo)) {
                Body bodyTwo = setPart(partJoint.partTwo);
                bodies.add(bodyTwo);
                partToBody.put(partJoint.partTwo, bodyTwo);
            }

            joints.add(setJoint(partJoint));
        }
    }

    private Body setPart(Part part) {
        // body definition
        BodyDef bd = new BodyDef();
        bd.position.set(0f, -5f);
        bd.angle = 0f;
        // bd.angularDamping = 0.5f;
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
        
        PartUserData userData = new PartUserData();
        userData.chromosome = chromosome;
        
        body.setUserData(userData);

        return body;
    }

    private Joint setJoint(PartJoint partJoint) {
        Body bodyOne = partToBody.get(partJoint.partOne);
        Body bodyTwo = partToBody.get(partJoint.partTwo);

        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = bodyOne;
        jointDef.bodyB = bodyTwo;

        jointDef.localAnchorA = partJoint.partOne.getAnchor(partJoint
                .getPercentOne());
        jointDef.localAnchorB = partJoint.partTwo.getAnchor(partJoint
                .getPercentTwo());

        jointDef.lowerAngle = Math.min(partJoint.getPointA(),
                partJoint.getPointB());
        jointDef.upperAngle = Math.max(partJoint.getPointA(),
                partJoint.getPointB());

        jointDef.maxMotorTorque = 100.0f;
        jointDef.motorSpeed = GeomUtil.circle(partJoint.getAngularVelocity());

        jointDef.enableMotor = true;
        jointDef.enableLimit = true;

        return world.createJoint(jointDef);
    }

    public Chromosome getChromosome() {
        return chromosome;
    }

    public Result removeFromWorld() {
        for (Body body : bodies) {
            world.destroyBody(body);
        }

        for (Joint joint : joints) {
            world.destroyJoint(joint);
        }

        return new Result(chromosome, getDistance());
    }

    public boolean isDone(float time) {
        timer += time;

        if (timer > robotSeconds) {
            return true;
        }

        float distance = getDistance();

        if (distance > maxDistance) {
            maxDistance = distance;
            restTimer = 0;
        } else {
            restTimer += time;
        }

        if (restTimer >= restSeconds) {
            return true;
        }

        return false;
    }

    public float getDistance() {
        return getPosition().x;
    }

    public Vector<Float> getPosition() {
        Vector<Float> position = new Vector<Float>(0f, 0f);

        for (Body body : bodies) {
            position.x += body.getPosition().x;
            position.y += body.getPosition().y;
        }

        position.x /= bodies.size();
        position.y /= bodies.size();

        return position;
    }

}
