package org.rtevo.gui;

import org.lwjgl.opengl.Display;
import org.rtevo.simulation.Simulation;

public class ThreadedRenderer extends Renderer implements Runnable {
	Simulation simulation;

	@Override
	public synchronized void updateDisplay() {
		super.updateDisplay();
		Display.sync(60);
	}

	@Override
	public synchronized void setSimulation(Simulation simulation) {
		super.setSimulation(simulation);
	}

	@Override
	public void run() {
		while (!Display.isCloseRequested()) {
			updateDisplay();
		}
	}

}
