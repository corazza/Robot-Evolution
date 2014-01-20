/**
 * 
 */
package org.rtevo.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.jbox2d.collision.shapes.CircleShape;
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
	private int timeStep = 10; // in milliseconds
	private int getMillisecondsPerSimulation;

	private int velocityIterations = 6;
	private int positionIterations = 2;

	private Vec2 gravity;
	private boolean doSleep = true;
	private World world;

	private List<Body> bodies;

	// temporary
	int counter = 0;

	/**
	 * 
	 * 
	 * @param chromosomes
	 *            list of all the chromosomes that should be evaluated by this
	 *            simulation
	 * @param timeStep
	 *            the number of milliseconds that each update lasts
	 */
	public Simulation(List<Chromosome> chromosomes, int timeStep, int maxTime) {
		if (chromosomes.isEmpty()) {
			throw new IllegalArgumentException(
					"There must be more than 0 chromosomes in the simulation.");
		}

		this.chromosomes = chromosomes;
		this.timeStep = timeStep;
		this.getMillisecondsPerSimulation = maxTime;

		bodies = new ArrayList<Body>();
	}

	// MEMO - FOR THREADS - the current implementation is not optimal because if
	// the user wants
	// to render it he has to simulate it in his own thread himself because of
	// data sharing. Solution: Monitor object that has synchronized methods for
	// reading and writing what has to be rendered - PROBLEM: the r/w is
	// synchronized, but the actual objects in it might not be - THEY PROBABLY
	// ARE, but ask SO. They WILL BE if using BlockingQueue. Currently
	// unimportant.

	/**
	 * Generate physics objects and add them to the JBox2D world.
	 */
	public void setup() {
		// create the world
		gravity = new Vec2(0.0f, -10.0f);
		world = new World(gravity);
		world.setAllowSleep(doSleep);

		// set all the chromosomes
		for (Chromosome chromosome : chromosomes) {
			// body definition
			BodyDef bd = new BodyDef();
			bd.position.set(50, 50);
			bd.type = BodyType.DYNAMIC;

			// define shape of the body.
			CircleShape cs = new CircleShape();
			cs.m_radius = 0.5f;

			// define fixture of the body.
			FixtureDef fd = new FixtureDef();
			fd.shape = cs;
			fd.density = 0.5f;
			fd.friction = 0.3f;
			fd.restitution = 0.5f;

			// create the body and add fixture to it
			Body body = world.createBody(bd);
			body.createFixture(fd);

			bodies.add(body);
		}
	}

	/**
	 * Advances the simulation.
	 * 
	 * @param timeStep
	 *            the number of milliseconds to advance the simulation
	 */
	// MEMO check if this is compatible with JBox2D (the approach of using +int
	// milliseconds to advance the simulation)
	public synchronized void update() {
		// TODO search for failed, remove from chromosomes, add to results

		world.step(timeStep, velocityIterations, positionIterations);

		counter += timeStep;

		if (counter > getMillisecondsPerSimulation) {
			Chromosome finished = chromosomes.remove(0);
			Result finishedResult = new Result(finished, RandomUtil.random(0,
					100));
			results.add(finishedResult);

			System.out.println("one over, " + chromosomes.size()
					+ " left to go");

			counter = 0;
		}
	}

	public List<Result> simulate() {
		setup();

		while (!chromosomes.isEmpty()) {
			update();
		}

		return results;
	}

	/**
	 * Returns an array of Renderable objects.
	 */
	public synchronized void snapshot() {

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

	public int getTimeStep() {
		return timeStep;
	}

}
