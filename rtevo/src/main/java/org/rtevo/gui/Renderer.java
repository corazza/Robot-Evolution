/**
 * 
 */
package org.rtevo.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.rtevo.common.Vector;
import org.rtevo.evolution.Generation;
import org.rtevo.simulation.PartUserData;
import org.rtevo.simulation.Robot;
import org.rtevo.simulation.Simulation;

@SuppressWarnings("serial")
class Renderer extends JPanel implements ActionListener {
    private Window window;
    private Simulation simulation;
    private Graphics2D currentGraphics;
    private Camera camera;
    private Body groundBody;
    private String mode;

    private long startMillis;

    // rendering
    private static final Color partFillColor = new Color(200, 200, 205, 100);
    private static final Color partOutlineColor = new Color(200, 220, 200, 200);
    private static final Color groundColor = new Color(10, 210, 50, 70); // green
    private static final Color backgroundColor = new Color(50, 50, 50);
    private static final BasicStroke partOutlineStroke = new BasicStroke(1f);
    private static final BasicStroke groundStroke = new BasicStroke(1f);

    private static final Color infoColor = new Color(250, 250, 250);
    private static final Font infoFont = new Font(Font.SANS_SERIF, Font.BOLD,
            13);
    private static final BasicStroke infoStroke = new BasicStroke(1.5f);
    private static final Font robotFont = new Font(Font.SANS_SERIF, Font.PLAIN,
            12);

    public Renderer(Window window, String mode) {
        this.window = window;
        this.mode = mode;
        camera = new Camera(this);

        startMillis = System.currentTimeMillis();

        addMouseListener(new RendererMouseAdapter());
        addMouseMotionListener(new RendererMouseMotionAdapter());
        addMouseWheelListener(new RendererMouseWheelListener());

        setFocusable(true);

        initUI();
    }

    private class RendererMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            camera.startDragging(e.getX(), e.getY());
        }

    }

    private class RendererMouseMotionAdapter extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent e) {
            camera.updateDragging(e.getX(), e.getY());
        }

    }

    private class RendererMouseWheelListener implements MouseWheelListener {
        public void mouseWheelMoved(MouseWheelEvent e) {
            camera.scrolled(e.getWheelRotation());
        }
    }

    private void initUI() {
        setDoubleBuffered(true);
    }

    private void setup() {
        currentGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        currentGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    private void drawBackground() {
        currentGraphics.setColor(backgroundColor);
        currentGraphics.fillRect(0, 0, window.getWidth(), window.getHeight());
    }

    private void fillShape(PolygonShape shape) {
        GeneralPath groundPath = new GeneralPath();
        Vec2 point = shape.getVertex(0);
        Vector<Integer> translated = camera.translateRelative(point);

        groundPath.moveTo(translated.x, translated.y);

        for (int i = 1; i < shape.getVertexCount(); ++i) {
            point = shape.getVertex(i);
            translated = camera.translateRelative(point);
            groundPath.lineTo(translated.x, translated.y);
        }

        groundPath.closePath();
        currentGraphics.fill(groundPath);
    }

    private void fillShape(PolygonShape shape, Color color) {
        currentGraphics.setColor(color);
        fillShape(shape);
    }

    private void outlineShape(PolygonShape shape) {
        Vec2 point1 = shape.m_vertices[0];

        int i = 1;

        for (; i < shape.m_count; ++i) {
            Vec2 point2 = shape.m_vertices[i];

            Vector<Integer> translated1 = camera.translateRelative(point1);
            Vector<Integer> translated2 = camera.translateRelative(point2);

            Line2D line = new Line2D.Float(translated1.x, translated1.y,
                    translated2.x, translated2.y);

            currentGraphics.draw(line);

            point1 = point2;
        }

        point1 = shape.m_vertices[i - 1];
        Vec2 point2 = shape.m_vertices[0];

        Vector<Integer> translated1 = camera.translateRelative(point1);
        Vector<Integer> translated2 = camera.translateRelative(point2);

        Line2D line = new Line2D.Float(translated1.x, translated1.y,
                translated2.x, translated2.y);

        currentGraphics.draw(line);
    }

    private void outlineShape(PolygonShape shape, Color color) {
        currentGraphics.setColor(color);
        outlineShape(shape);
    }

    private void drawPart(Body body) {
        Vector<Integer> position = camera.translate(body.getPosition());
        currentGraphics.translate(position.x, position.y);
        currentGraphics.rotate(body.getAngle());

        for (Fixture fixture = body.getFixtureList(); fixture != null; fixture = fixture
                .getNext()) {
            Shape shape = fixture.getShape();
            fillShape((PolygonShape) shape, partFillColor);
            currentGraphics.setStroke(partOutlineStroke);
            outlineShape((PolygonShape) shape, partOutlineColor);
        }

        currentGraphics.rotate(-body.getAngle());
        currentGraphics.translate(-position.x, -position.y);
    }

    private void drawGround(Body body) {
        Vector<Integer> translated = camera.translate(body.getPosition());
        Line2D line = new Line2D.Float(0, translated.y, window.getWidth(),
                translated.y);
        currentGraphics.setStroke(groundStroke);
        currentGraphics.setColor(groundColor);
        currentGraphics.draw(line);
    }

    private void drawWorld() {
        if (simulation == null) {
            return;
        }

        for (Body body = simulation.getWorld().getBodyList(); body != null; body = body
                .getNext()) {
            if (body.getUserData() instanceof PartUserData) {
                drawPart(body);
            } else {
                drawGround(body);
                groundBody = body;
            }
        }
    }

    private Vector<Integer> getGroundPosition() {
        return camera.translate(groundBody.getPosition());
    }

    private void drawUpLeftInfo() {
        currentGraphics.setFont(infoFont);

        int infoOffset = 0;
        FontMetrics fontMetrics = currentGraphics.getFontMetrics();

        float seconds = (System.currentTimeMillis() - startMillis) / 1000f;

        String timeString = "Time: " + String.format("%.1f", seconds) + "s";
        String generationString = mode == "simulation" ? "Generation #"
                + Generation.generationNumber : "Presentation";

        currentGraphics.drawString(timeString, 10, fontMetrics.getHeight()
                + infoOffset);
        currentGraphics.drawString(generationString, 10,
                fontMetrics.getHeight() * 2 + infoOffset);
    }

    private void drawRobotInfo() {
        currentGraphics.setFont(robotFont);
        currentGraphics.setColor(infoColor);

        FontMetrics fontMetrics = currentGraphics.getFontMetrics();
        Vector<Integer> groundPosition = getGroundPosition();

        for (Robot robot : simulation.getRobots()) {
            Vector<Float> position = robot.getPosition();
            Vector<Integer> translated = camera.translate(position);
            translated.y = groundPosition.y + fontMetrics.getHeight() + 10;

            String positionString = String.format("%.2f", position.x) + "m";

            currentGraphics
                    .drawString(
                            positionString,
                            translated.x
                                    - fontMetrics.stringWidth(positionString)
                                    / 2, translated.y);
        }

    }

    private void drawOrigin() {
        Vector<Integer> origin = camera.translate(new Vec2(0, 0));
        currentGraphics.fillOval(origin.x - 2, origin.y - 2, 4, 4);
    }

    private void drawLegendAndMeasures() {
        Vector<Integer> groundPosition = getGroundPosition();
        Vector<Integer> from = camera.translate(new Vec2(2, 0));
        Vector<Integer> to = camera.translate(new Vec2(3, 0));
        int offset = 50;
        int height = camera.translateRelative(20);

        Line2D legend = new Line2D.Float(from.x, groundPosition.y + offset,
                to.x, groundPosition.y + offset);

        currentGraphics.draw(legend);

        currentGraphics.setColor(new Color(250, 250, 250, 100));
        currentGraphics.setStroke(new BasicStroke(1f));

        Line2D measureOne = new Line2D.Float(from.x, groundPosition.y + offset
                - height / 2, from.x, groundPosition.y + offset + height / 2);
        Line2D measureTwo = new Line2D.Float(to.x, groundPosition.y + offset
                - height / 2, to.x, groundPosition.y + offset + height / 2);

        currentGraphics.draw(measureOne);
        currentGraphics.draw(measureTwo);

        currentGraphics.setColor(infoColor);

        FontMetrics fontMetrics = currentGraphics.getFontMetrics();

        currentGraphics.drawString("1m",
                (from.x + to.x) / 2 - fontMetrics.stringWidth("1m") / 2, from.y
                        + offset + fontMetrics.getHeight());
    }

    private void drawInfo() {
        currentGraphics.setColor(infoColor);
        currentGraphics.setStroke(infoStroke);

        drawUpLeftInfo();
        drawRobotInfo();
        drawLegendAndMeasures();
        drawOrigin();
    }

    @Override
    public void paintComponent(Graphics g) {
        if (simulation == null) {
            return;
        }

        super.paintComponent(g);

        currentGraphics = (Graphics2D) g;

        setup();
        drawBackground();
        drawWorld();
        drawInfo();
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public void actionPerformed(ActionEvent e) {
    }

}