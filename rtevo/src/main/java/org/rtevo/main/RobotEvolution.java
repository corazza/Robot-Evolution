/**
 * 
 */
package org.rtevo.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.rtevo.common.Configuration;
import org.rtevo.evolution.Chromosome;
import org.rtevo.evolution.Part;
import org.rtevo.evolution.PartJoint;
import org.rtevo.gui.Window;
import org.rtevo.evolution.Generation;
import org.rtevo.simulation.Result;
import org.rtevo.simulation.Robot;
import org.rtevo.simulation.Simulation;

import com.google.gson.Gson;

/**
 * Main application class
 * 
 */
public class RobotEvolution {
	private Window window;

	private Configuration c;
	private Gson gson = new Gson();

	private int generations = 0;
	private Result bestResult;
	Simulation presentationSimulation;

	public RobotEvolution(Configuration config) {
		c = config;

		if (c.parallelSimulations < 1) {
			throw new IllegalArgumentException(
					"The number of parallel simulations must be greater than 0");
		}

		Generation.configureWorkerPool(c.parallelSimulations);
		Generation.setGravity(c.gravity);
		Generation.setTimeStep(c.timeStep);
		Robot.setRobotMilliseconds(c.robotSeconds);
		Chromosome.setMutationChance(c.mutationChance);

		if (!c.load.equals("false")) {
			c.GUI = true;
		}
	}

	private void load() {
		int FPS = 60;
		float waitTime = 1000 / FPS;
		FileReader toLoad;

		try {
			toLoad = new FileReader(new File("chromosomes/" + c.load));

			Chromosome loadedChromosome = gson.fromJson(toLoad,
					Chromosome.class);

			HashMap<Integer, Part> map = new HashMap<Integer, Part>();

			for (PartJoint partJoint : loadedChromosome.partJoints) {
				if (map.containsKey(partJoint.partOne.idm)) {
					partJoint.partOne = map.get(partJoint.partOne.idm);
				} else {
					map.put(partJoint.partOne.idm, partJoint.partOne);
				}

				if (map.containsKey(partJoint.partTwo.idm)) {
					partJoint.partTwo = map.get(partJoint.partTwo.idm);
				} else {
					map.put(partJoint.partTwo.idm, partJoint.partTwo);
				}
			}

			ArrayList<Chromosome> chromosomes = new ArrayList<Chromosome>();

			chromosomes.add(loadedChromosome);

			// Create a simulation for a single chromosome
			Simulation presentationSimulation = new Simulation(chromosomes,
					c.gravity, 0.001f);

			presentationSimulation.setTimeStep(0.01f);
			// presentationSimulation.setTimeStep(0.001f);

			presentationSimulation.setExpire(false);
			presentationSimulation.setup();

			// Submit it to the renderer
			window.setSimulation(presentationSimulation);
			Robot presentedRobot = presentationSimulation.getRobots().get(0);

			while (true) {
				presentationSimulation.update();
				presentedRobot.isBird();
				window.updateDisplay();

				try {
					Thread.sleep((int) waitTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

	}

	private boolean metCriteria() {
		if (c.generations != -200) {
			return generations == c.generations;
		}

		return bestResult.score / c.robotSeconds == c.satisfactory;
	}

	private Generation performGa() {
		// Initialize GA:
		Generation generation = new Generation(c.robotsPerGeneration);

		// Main algorithm
		do {
			System.out.print("generation #" + (generations + 1) + "... ");
			// Create all the simulations and start them in their separate
			// threads
			long started = System.currentTimeMillis();
			generation.computeAll();

			if (!c.GUI) {
				int waitMillis = 100;

				while (!generation.isDone()) {
					try {
						Thread.sleep(waitMillis);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else {
				long wait = c.pause * 1000;
				int FPS = 60;
				float waitTime = 1000 / FPS;

				// Create a simulation for a single chromosome
				presentationSimulation = generation.getSample();
				presentationSimulation.setExpire(false);
				presentationSimulation.setTimeStep(waitTime / 1000);
				presentationSimulation.setup();

				// Submit it to the renderer
				window.setSimulation(presentationSimulation);

				// Simulate and render the presentation simulation while real
				// computation is being done in the backend
				while (!generation.isDone()
						|| started + wait > System.currentTimeMillis()) {
					presentationSimulation.update();
					window.updateDisplay();

					try {
						Thread.sleep((int) waitTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			generation.recordResults();

			// Record end criteria
			++generations;
			bestResult = generation.getBestResult();

			// Evolve
			generation = generation.evolve();

			System.out.println("generation done, took "
					+ (System.currentTimeMillis() - started) / 1000.0
					+ " seconds, best result: " + bestResult);
		} while (!metCriteria());

		return generation;
	}

	private void checkLoad() {
		if (!c.load.equals("false")) {
			load();
			return;
		}
	}

	private void checkGui() {
		if (c.GUI) {
			window = new Window(c.windowWidth, c.windowHeight,
					c.load.equals("false") ? "simulation" : "presentation");
		}
	}

	public void start() {
		checkGui();
		checkLoad();
		Generation lastGeneration = performGa();
		if (!c.save.equals("false")) {
			saveBest(lastGeneration.getPreviousBestChromosome());
		}
	}

	public void saveBest(Chromosome best) {
		File directory = new File("chromosomes");

		if (!directory.exists()) {
			directory.mkdir();
		}

		File saveFile = new File("chromosomes/" + c.save);

		try {
			PrintWriter writer = new PrintWriter(saveFile);
			writer.print(gson.toJson(best));
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
