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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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
class Renderer extends JPanel implements ActionListener {
    // TODO correctly render, with scaling, movable camera, and (0, 0) in the
    // middle

    private Window window;
    private Simulation simulation;
    private Graphics2D currentGraphics;
    private Camera camera;
    private Vector startDrag;

    // rendering
    private static final Color partFillColor = new Color(200, 200, 205, 100);
    private static final Color partOutlineColor = new Color(200, 220, 200, 200);
    private static final Color groundFillColor = new Color(10, 10, 210, 50);
    private static final Color groundOutlineColor = new Color(10, 10, 210, 150);
    private static final Color backgroundColor = new Color(50, 50, 50);
    private static final Color defaultColor = new Color(250, 250, 250, 100);

    private static final BasicStroke partOutlineStroke = new BasicStroke(2.5f);
    private static final BasicStroke groundOutlineStroke = new BasicStroke(1.5f);
    private static final BasicStroke defaultStroke = new BasicStroke(1);
    private static final Font font = new Font(Font.SERIF, Font.PLAIN, 12);
    
    private static final float pPM = 200.0f;

    public Renderer(Window window) {
        this.window = window;
        camera = new Camera(this);

        addKeyListener(new RendererKeyAdapter());
        addMouseListener(new RendererMouseAdapter());
        addMouseMotionListener(new RendererMouseMotionAdapter());
        addMouseWheelListener(new RendererMouseWheelListener());

        setFocusable(true);

        initUI();
    }

    private class RendererKeyAdapter extends KeyAdapter {
        public void keyReleased(KeyEvent e) {
            System.out.println("rel: " + KeyEvent.getKeyText(e.getKeyCode()));
        }

        public void keyPressed(KeyEvent e) {
            System.out.println("pre: " + KeyEvent.getKeyText(e.getKeyCode()));
        }
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
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            camera.scrolled(e.getWheelRotation());
        }

    }

    private void initUI() {
        setDoubleBuffered(true);
    }

    private void setup() {
        currentGraphics.setColor(defaultColor);
        currentGraphics.setStroke(defaultStroke);

        currentGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        currentGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        currentGraphics.setFont(font);
    }

    private void drawBackground() {
        currentGraphics.setColor(backgroundColor);
        currentGraphics.fillRect(0, 0, window.getWidth(), window.getHeight());
    }

    private void fillShape(PolygonShape shape) {
        GeneralPath groundPath = new GeneralPath();
        Vec2 point = shape.getVertex(0);
        Vector translated = camera.translateRelative(point);
//        Vector translated = new Vector((int) (point.x * pPM), (int) (point.y * pPM));

        String text = "The quick brown fox jumped over the lazy dog";

        // currentGraphics.drawString(text, 20, 30);

        groundPath.moveTo(translated.x, translated.y);

        for (int i = 1; i < shape.getVertexCount(); ++i) {
            point = shape.getVertex(i);
            translated = camera.translateRelative(point);
//            translated = new Vector((int) (point.x * pPM), (int) (point.y * pPM));
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
        Vec2 point1 = shape.getVertex(0);

        int i = 1;

        for (; i < shape.getVertexCount(); ++i) {
            Vec2 point2 = shape.getVertex(i);

            Vector translated1 = camera.translateRelative(point1);
            Vector translated2 = camera.translateRelative(point2);

//            Vector translated1 = new Vector((int) (point1.x * pPM), (int) (point1.y * pPM));
//            Vector translated2 = new Vector((int) (point2.x * pPM), (int) (point2.y * pPM));
            
            Line2D line = new Line2D.Float(translated1.x, translated1.y,
                    translated2.x, translated2.y);

            currentGraphics.draw(line);

            point1 = point2;
        }

        point1 = shape.getVertex(i - 1);
        Vec2 point2 = shape.getVertex(0);

        Vector translated1 = camera.translateRelative(point1);
        Vector translated2 = camera.translateRelative(point2);

//        Vector translated1 = new Vector((int) (point1.x * pPM), (int) (point1.y * pPM));
//        Vector translated2 = new Vector((int) (point2.x * pPM), (int) (point2.y * pPM));

        Line2D line = new Line2D.Float(translated1.x, translated1.y,
                translated2.x, translated2.y);

        currentGraphics.draw(line);
    }

    private void outlineShape(PolygonShape shape, Color color) {
        currentGraphics.setColor(color);
        outlineShape(shape);
    }

    private void drawBody(Body body) {
        // setup the transforms
         Vector position = camera.translate(body.getPosition());
//        Vector position = new Vector((int) (body.getPosition().x * pPM) + 512,
//                (int) (body.getPosition().y * pPM) + 400);
        currentGraphics.translate(position.x, position.y);
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
                currentGraphics.setStroke(groundOutlineStroke);
                outlineShape(shape, groundOutlineColor);
            }
        }

        // clean up
        currentGraphics.rotate(-body.getAngle());
        currentGraphics.translate(-position.x, -position.y);
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

    public void actionPerformed(ActionEvent e) {
    }

}