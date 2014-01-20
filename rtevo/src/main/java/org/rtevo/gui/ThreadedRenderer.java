package org.rtevo.gui;

import org.rtevo.simulation.Simulation;

public class ThreadedRenderer extends RobotEvolutionWindow implements Runnable {
    Simulation simulation;

    @Override
    public synchronized void updateDisplay() {
        super.updateDisplay();
    }

    @Override
    public synchronized void setSimulation(Simulation simulation) {
        super.setSimulation(simulation);
    }

    @Override
    public void run() {
    }

}
