/**
 * 
 */
package org.rtevo.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.rtevo.genetics.Chromosome;

/**
 * Evaluates chromosomes. Encapsulates and represents a simulation.
 * 
 * @author Jan Corazza
 */
public class Simulation implements Callable<List<Result>> {
    // robots have a time limit
    private boolean expire = true;

    private List<Chromosome> chromosomes;
    private List<Result> results = new ArrayList<Result>();
    private ArrayList<Robot> robots = new ArrayList<Robot>();

    // simulation
    private float timeStep;

    private int velocityIterations = 6;
    private int positionIterations = 2;
    private int groundLength = 1000; // in meters

    private float gravity;
    private World world;

    /**
     * 
     * 
     * @param chromosomes
     *            list of all the chromosomes that should be evaluated by this
     *            simulation
     */
    public Simulation(List<Chromosome> chromosomes, float gravity,
            float timeStep) {
        if (chromosomes.isEmpty()) {
            throw new IllegalArgumentException(
                    "There must be more than 0 chromosomes in the simulation.");
        }

        this.chromosomes = chromosomes;
        this.gravity = gravity;
        this.timeStep = timeStep;
    }

    // MEMO - FOR THREADS - the current implementation is not optimal because if
    // the user wants
    // to render it he has to simulate it in his own thread himself because of
    // data sharing. Solution: Monitor object that has synchronized methods for
    // reading and writing what has to be rendered - PROBLEM: the r/w is
    // synchronized, but the actual objects in it might not be - THEY PROBABLY
    // ARE, but ask SO. They WILL BE if using BlockingQueue. Currently
    // unimportant.

    private void setGround() {
        BodyDef bd = new BodyDef();
        bd.position.set(Math.max(-groundLength / 2, -10), 0f);
        bd.type = BodyType.STATIC;

        // add chain segments to the ground
        ChainShape shape = new ChainShape();

        ArrayList<Vec2> groundVertices = new ArrayList<Vec2>();

        for (int i = 0; i < groundLength / 5; ++i) {
            Vec2 segment = new Vec2((float) 5 * i, 0f);
            groundVertices.add(segment);
        }

        Vec2[] spec = {};

        shape.createChain(groundVertices.toArray(spec), groundVertices.size());

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        Filter filter = new Filter();
        filter.groupIndex = 1;
        fd.filter = filter;
        fd.friction = 0.3f;
        fd.restitution = 0.5f;

        // create the body and add fixture to it
        Body body = world.createBody(bd);
        body.createFixture(fd);
        body.setUserData(new GroundUserData());
    }

    private void setRobots() {
        for (Chromosome chromosome : chromosomes) {
            robots.add(new Robot(chromosome, world));
        }
    }

    /**
     * Generate physics objects and add them to the JBox2D world.
     */
    public void setup() {
        Vec2 gravityVec2 = new Vec2(0f, gravity);
        world = new World(gravityVec2);
        world.setAllowSleep(true);

        setGround();
        setRobots();
    }

    /**
     * Advances the simulation.
     */
    // MEMO check if this is compatible with JBox2D (the approach of using +int
    // milliseconds to advance the simulation)
    public synchronized void update() {
        world.step(timeStep, velocityIterations, positionIterations);

        for (RevoluteJoint joint = (RevoluteJoint) world.getJointList(); joint != null; joint = (RevoluteJoint) joint
                .getNext()) {
            if (joint.getJointAngle() <= joint.getLowerLimit()
                    || joint.getJointAngle() >= joint.getUpperLimit()) {
                joint.setMotorSpeed(-joint.getMotorSpeed());
            }
        }
    }

    /**
     * Searches for failed chromosomes and removes them from the simulation
     */
    public synchronized void removeFinished() {
        if (!expire) {
            return;
        }

        ArrayList<Robot> robotsToRemove = new ArrayList<Robot>();
        ArrayList<Chromosome> chromosomesToRemove = new ArrayList<Chromosome>();

        for (Robot robot : robots) {
            if (robot.isDone(timeStep)) {
                results.add(robot.removeFromWorld());
                robotsToRemove.add(robot);
                chromosomesToRemove.add(robot.getChromosome());
            }
        }

        robots.removeAll(robotsToRemove);
        chromosomes.removeAll(chromosomesToRemove);
    }

    public List<Result> simulate() {
        setup();

        while (!robots.isEmpty()) {
            update();
            removeFinished();
        }

        return results;
    }

    @Override
    public List<Result> call() {
        return simulate();
    }

    public boolean isExpire() {
        return expire;
    }

    public void setExpire(boolean expire) {
        this.expire = expire;
    }

    public synchronized World getWorld() {
        return world;
    }

    public void addChromosome(Chromosome toAdd) {
        chromosomes.add(toAdd);
    }

    public List<Chromosome> getChromosomes() {
        return chromosomes;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setTimeStep(float timeStep) {
        this.timeStep = timeStep;
    }

    public float getTimeStep() {
        return timeStep;
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

}
