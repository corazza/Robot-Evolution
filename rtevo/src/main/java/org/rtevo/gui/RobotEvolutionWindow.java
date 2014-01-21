package org.rtevo.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.rtevo.simulation.Simulation;

@SuppressWarnings("serial")
class Renderer extends JPanel {
    private RobotEvolutionWindow window;
    private Simulation simulation;

    public Renderer(RobotEvolutionWindow window) {
        this.window = window;

        initUI();
    }

    private void initUI() {
        setDoubleBuffered(true);
    }

    private void setup(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    private void drawBackground(Graphics2D g2d) {
        g2d.setColor(new Color(50, 50, 50));
        g2d.fillRect(0, 0, window.getWidth(), window.getHeight());
    }

    private void drawRenderables(Graphics2D g2d) {
        if (simulation == null) {
            return;
        }

        for (Renderable renderable : simulation.getBodies()) {
            renderable.draw(g2d);
        }
    }

    private void drawInfo(Graphics2D g2d) {
        Font font = new Font("Serif", Font.BOLD, 40);
        g2d.setFont(font);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        setup(g2d);
        drawBackground(g2d);
        drawRenderables(g2d);
        drawInfo(g2d);
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

}

@SuppressWarnings("serial")
public class RobotEvolutionWindow extends JFrame {
    Renderer renderer;

    public RobotEvolutionWindow(int width, int height) {
        setTitle("Robot Evolution");
        setSize(width, height);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        renderer = new Renderer(this);
        add(renderer);
    }

    public void updateDisplay() {
        renderer.repaint();
    }

    public void setSimulation(Simulation simulation) {
        renderer.setSimulation(simulation);
    }

}
