/**
 * 
 */
package org.rtevo.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.rtevo.genetics.Chromosome;
import org.rtevo.util.RandomUtil;

/**
 * Evaluates chromosomes. Encapsulates and represents a simulation.
 * 
 * @author Jan Corazza
 */
public class Simulation implements Callable<List<Result>> {
    // robots that are all in this same simulation
    private List<Chromosome> chromosomes;

    // array of results for each chromosome
    private List<Result> results = new ArrayList<Result>();

    // TODO map Body -> Chromosome?

    // simulation:
    private float timeStep = 0.01f; // in seconds
    private int robotMilliseconds;

    private int velocityIterations = 6;
    private int positionIterations = 2;

    private float gravity;
    private boolean doSleep = true;
    private World world;

    // temporary
    float counter = 0;

    /**
     * 
     * 
     * @param chromosomes
     *            list of all the chromosomes that should be evaluated by this
     *            simulation
     */
    public Simulation(List<Chromosome> chromosomes, int robotMilliseconds,
            float gravity) {
        if (chromosomes.isEmpty()) {
            throw new IllegalArgumentException(
                    "There must be more than 0 chromosomes in the simulation.");
        }

        this.chromosomes = chromosomes;
        this.robotMilliseconds = robotMilliseconds;
        this.gravity = gravity;
    }

    // MEMO - FOR THREADS - the current implementation is not optimal because if
    // the user wants
    // to render it he has to simulate it in his own thread himself because of
    // data sharing. Solution: Monitor object that has synchronized methods for
    // reading and writing what has to be rendered - PROBLEM: the r/w is
    // synchronized, but the actual objects in it might not be - THEY PROBABLY
    // ARE, but ask SO. They WILL BE if using BlockingQueue. Currently
    // unimportant.

    private void setFloor() {
        // body definition
        BodyDef bd = new BodyDef();
        bd.position.set(-25f, 5f);
        bd.type = BodyType.STATIC;

        // define shape of the body.
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50f, 0.1f);

        // define fixture of the body.
        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 0.5f;
        fd.friction = 0.3f;
        fd.restitution = 0.5f;

        // create the body and add fixture to it
        Body body = world.createBody(bd);
        body.createFixture(fd);
    }

    /**
     * Generate physics objects and add them to the JBox2D world.
     */
    public void setup() {
        // create the world
        Vec2 gravityVec2 = new Vec2(0f, gravity);
        // Vec2 gravityVec2 = new Vec2(10.0f, 105.0f);
        world = new World(gravityVec2);
        world.setAllowSleep(doSleep);

        setFloor();

        // set all the chromosomes
        for (Chromosome chromosome : chromosomes) {
            // body definition
            BodyDef bd = new BodyDef();
            bd.position.set(4f, 1.1f);
            bd.type = BodyType.DYNAMIC;

            // define shape of the body.
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.5f, 1f);

            // define fixture of the body.
            FixtureDef fd = new FixtureDef();
            fd.shape = shape;
            fd.density = 0.5f;
            fd.friction = 0.3f;
            fd.restitution = 0.5f;

            // create the body and add fixture to it
            Body body = world.createBody(bd);
            body.createFixture(fd);
        }
    }

    /**
     * Advances the simulation.
     */
    // MEMO check if this is compatible with JBox2D (the approach of using +int
    // milliseconds to advance the simulation)
    public synchronized void update() {
        world.step(timeStep, velocityIterations, positionIterations);
    }

    /**
     * Searches for failed chromosomes and removes them from the simulation
     */
    public synchronized void removeFinished() {
        counter += timeStep;

        if (counter * 1000 > robotMilliseconds) {
            Chromosome finished = chromosomes.remove(0);
            Result finishedResult = new Result(finished, RandomUtil.random(0,
                    100));
            results.add(finishedResult);

            counter = 0;
        }
    }

    public List<Result> simulate() {
        setup();

        while (!chromosomes.isEmpty()) {
            update();
            // TODO flag to avoid unnecessary call
            removeFinished();
        }

        return results;
    }

    public synchronized World getWorld() {
        return world;
    }

    public void addChromosome(Chromosome toAdd) {
        chromosomes.add(toAdd);
    }

    @Override
    public List<Result> call() {
        return simulate();
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
