package org.rtevo.gui;

import javax.swing.JFrame;

import org.rtevo.simulation.Simulation;

@SuppressWarnings("serial")
public class Window extends JFrame {
    Renderer renderer;

    public Window(int width, int height) {
        setTitle("Robot Evolution");
        setSize(width, height);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        renderer = new Renderer(this);
        add(renderer);
        renderer.revalidate();
    }

    public void updateDisplay() {
        renderer.repaint();
    }

    public void setSimulation(Simulation simulation) {
        renderer.setSimulation(simulation);
    }

}
