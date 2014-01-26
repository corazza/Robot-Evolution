/**
 * 
 */
package org.rtevo.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.rtevo.simulation.PartUserData;
import org.rtevo.simulation.Simulation;

/**
 * 
 */
@SuppressWarnings("serial")
class Renderer extends JPanel {
    private Window window;
    private Simulation simulation;
    private Graphics2D currentGraphics;

    // physics->visual transformations
    private static final float pixelsPerMeter = 50;

    // rendering
    private static final Color partFillColor = new Color(200, 200, 205, 100);
    private static final Color partOutlineColor = new Color(200, 220, 200, 200);
    private static final Color groundFillColor = new Color(10, 10, 210, 50);
    private static final Color groundOutlineColor = new Color(10, 10, 210, 150);
    private static final Color backgroundColor = new Color(50, 50, 50);
    private static final Color defaultColor = new Color(250, 250, 250, 100);

    private static final BasicStroke partOutlineStroke = new BasicStroke(1.5f);
    private static final BasicStroke defaultStroke = new BasicStroke(1);

    public Renderer(Window window) {
        this.window = window;

        initUI();
    }

    private void initUI() {
        setDoubleBuffered(true);
    }

    private float metersToPixels(float a) {
        return pixelsPerMeter * a;
    }

    private void setup() {
        currentGraphics.setColor(defaultColor);
        currentGraphics.setStroke(defaultStroke);

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

        groundPath.moveTo(metersToPixels(point.x), metersToPixels(point.y));

        for (int i = 1; i < shape.getVertexCount(); ++i) {
            point = shape.getVertex(i);
            groundPath.lineTo(metersToPixels(point.x), metersToPixels(point.y));
        }

        groundPath.closePath();
        currentGraphics.fill(groundPath);
    }

    private void fillShape(PolygonShape shape, Color color) {
        currentGraphics.setColor(color);
        fillShape(shape);
    }

    private void outlineShape(PolygonShape shape) {
        Vec2 point1 = shape.getVertex(0);

        int i = 1;

        for (; i < shape.getVertexCount(); ++i) {
            Vec2 point2 = shape.getVertex(i);

            currentGraphics.draw(new Line2D.Float(metersToPixels(point1.x),
                    metersToPixels(point1.y), metersToPixels(point2.x),
                    metersToPixels(point2.y)));

            point1 = point2;
        }

        point1 = shape.getVertex(i - 1);
        Vec2 point2 = shape.getVertex(0);

        currentGraphics.draw(new Line2D.Float(metersToPixels(point1.x),
                metersToPixels(point1.y), metersToPixels(point2.x),
                metersToPixels(point2.y)));

    }

    private void outlineShape(PolygonShape shape, Color color) {
        currentGraphics.setColor(color);
        outlineShape(shape);
    }

    private void drawBody(Body body) {
        // setup the transforms
        currentGraphics.translate(metersToPixels(body.getPosition().x),
                metersToPixels(body.getPosition().y));
        currentGraphics.rotate(body.getAngle());

        // do the actual rendering
        for (Fixture fixture = body.getFixtureList(); fixture != null; fixture = fixture
                .getNext()) {
            PolygonShape shape = (PolygonShape) fixture.getShape();

            if (body.getUserData() instanceof PartUserData) {
                fillShape(shape, partFillColor);
                currentGraphics.setStroke(partOutlineStroke);
                outlineShape(shape, partOutlineColor);
            } else {
                fillShape(shape, groundFillColor);
                outlineShape(shape, groundOutlineColor);
            }
        }

        // clean up
        currentGraphics.rotate(-body.getAngle());
        currentGraphics.translate(-metersToPixels(body.getPosition().x),
                -metersToPixels(body.getPosition().y));
        currentGraphics.setColor(defaultColor);
        currentGraphics.setStroke(defaultStroke);
    }

    private void drawWorld() {
        if (simulation == null) {
            return;
        }

        for (Body body = simulation.getWorld().getBodyList(); body != null; body = body
                .getNext()) {
            drawBody(body);
        }
    }

    private void drawInfo() {
        Font font = new Font("Serif", Font.BOLD, 40);
        currentGraphics.setFont(font);
    }

    @Override
    public void paintComponent(Graphics g) {
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

}