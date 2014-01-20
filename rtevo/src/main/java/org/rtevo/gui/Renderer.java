package org.rtevo.gui;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.rtevo.simulation.Simulation;

public class Renderer {
	Simulation simulation;

	public void updateDisplay() {
		System.out.println("updatean");
		if (simulation != null) {
			System.out.println("displayan");
			// render simulation
			Display.update();
		}

	}

	public void setSimulation(Simulation simulation) {
		System.out.println("settan");
		this.simulation = simulation;
	}

	public Renderer() {
		try {
			System.out.println("TRYAN");
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setTitle("Robot Evolution");
			Display.setVSyncEnabled(true);
			Display.create();
			System.out.println("SUCCEEDAN");
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}
