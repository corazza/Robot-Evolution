/**
 * 
 */
package org.rtevo.main;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.lwjgl.opengl.Display;
import org.rtevo.common.Configuration;
import org.rtevo.gui.Renderer;
import org.rtevo.simulation.Generation;
import org.rtevo.simulation.Result;
import org.rtevo.simulation.Simulation;

/**
 * 
 * Supports creating multiple instances when.
 * 
 * @author Jan Corazza
 */

/*
 * Weight is a property of all objects, joints and body have weights, make them
 * extend something, evolve this weight
 * 
 * Think about: no legs or anything like that, just random polygons with weight
 * and joints
 */
/**
 * main application class
 * 
 */
public class RobotEvolution {
	private Configuration configuration;
	private ExecutorService workerPool;

	// how many simulations will be created for one generation?
	private int numSimulations = 10;
	private int timeStep = 10; // in milliseconds

	public RobotEvolution(Configuration config) {
		this.configuration = config;

		workerPool = Executors.newCachedThreadPool();
	}

	// MEMO think about threads - do they actually pay of in the way we're
	// implementing them?

	// MEMO thread priorities

	public void start() {
		// TODO thread for calc?

		// Start GUI:
		Renderer renderer = new Renderer();

		// Initialize GA:
		Generation generation = new Generation(
				configuration.getRobotsPerGeneration());

		// Start the application:
		for (int i = 0; i < configuration.getGenerations(); ++i) {
			System.out.println("generation #" + i);

			List<Simulation> simulations = generation.getSimulations(
					numSimulations, timeStep,
					configuration.getMillisecondsPerSimulation());
			
			renderer.setSimulation(simulations.get(0));

			for (Simulation simulation : simulations) {
				workerPool.submit(simulation);
				List<Result> results = simulation.simulate();
				generation.submitResults(results);
			}

			// evolve
			generation = generation.nextGeneration();
		}

		// Close the window
		Display.destroy();

		System.out.println("Done.");

		// threads:
		// IF GUI ENABLED:
		// 1. Create initial population. GeneticFactory
		// 2. Divide that population to parallelSimulation simulations. -
		// POSSIBILITY
		// 3. submit() (parallelSimulation-1) simulations to workerPool. -
		// NEBITNO
		// 4. Keep the remaining simulation in an update-render loop. If it is -
		// NEBITNO
		// done display an overlay "waiting for other threads" or pause
		// rendering - POSSIBLE: call get() on all Futures that you kept from
		// submit() calls, this will pause the current thread without wasting
		// resources.
		// 5. In that loop check all simulations if they are finished. If not
		// GOTO 4, else GOTO 6.
		// 6. get() all results from Callable simulations, create a new
		// generation, GOTO 2.

		// IF GUI DISABLED:
		// all the same, without update-render loop, just call() the Callable
		// simulation, then get() all other thread's Results from their Future.
	}

}
