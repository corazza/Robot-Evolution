package org.rtevo.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.rtevo.simulation.Simulation;

@SuppressWarnings("serial")
class Renderer extends JPanel {
    private Window window;
    private Simulation simulation;

    static final double pixelsPerMeter = 50;

    public Renderer(Window window) {
        this.window = window;

        initUI();
    }

    private void initUI() {
        setDoubleBuffered(true);
    }

    private double metersToPixels(float a) {
        return pixelsPerMeter * a;
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

    private void drawBody(Graphics2D g2d, Body body) {
        g2d.setColor(new Color(250, 250, 250));

        // setup the transforms
        g2d.translate(metersToPixels(body.getPosition().x),
                metersToPixels(body.getPosition().y));
        g2d.rotate(body.getAngle());

        // do the actual rendering
        for (Fixture fixture = body.getFixtureList(); fixture != null; fixture = fixture
                .getNext()) {
            switch (fixture.getType()) {
            case CIRCLE:
                int radius = (int) (metersToPixels(fixture.getShape()
                        .getRadius()));
                g2d.fillOval(0, 0, radius, radius);
                break;

            case POLYGON:
                PolygonShape shape = (PolygonShape) fixture.getShape();

                GeneralPath shapeDrawing = new GeneralPath();

                shapeDrawing.moveTo(0, 0);

                Vec2 point = shape.getVertex(0);

                //TODO draw lines instead of filling polygons
                //TODO different colors (green for ground, white for robots)
                shapeDrawing.moveTo(metersToPixels(point.x),
                        metersToPixels(point.y));

                for (int i = 1; i < shape.getVertexCount(); ++i) {
                    point = shape.getVertex(i);
                    shapeDrawing.lineTo(metersToPixels(point.x),
                            metersToPixels(point.y));
                }

                shapeDrawing.closePath();
                g2d.fill(shapeDrawing);
                break;

            default:
                break;
            }
        }

        // clean up
        g2d.rotate(-body.getAngle());
        g2d.translate(-metersToPixels(body.getPosition().x),
                -metersToPixels(body.getPosition().y));
    }

    private void drawWorld(Graphics2D g2d) {
        if (simulation == null) {
            return;
        }

        for (Body body = simulation.getWorld().getBodyList(); body != null; body = body
                .getNext()) {
            drawBody(g2d, body);
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
        drawWorld(g2d);
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
    }

    public void updateDisplay() {
        renderer.repaint();
    }

    public void setSimulation(Simulation simulation) {
        renderer.setSimulation(simulation);
    }

}
