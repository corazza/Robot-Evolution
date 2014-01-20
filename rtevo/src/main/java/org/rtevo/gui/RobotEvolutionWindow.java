package org.rtevo.gui;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.Point;
import org.lwjgl.util.vector.Vector2f;
import org.rtevo.simulation.Simulation;

public class RobotEvolutionWindow {
    Simulation simulation;
    private int width = 1024;
    private int height = 512;

    public RobotEvolutionWindow() {
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.setTitle("Robot Evolution");
            Display.setVSyncEnabled(true);
            Display.create();

            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, width, 0, height, 1, -1);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void drawLine(Point startPoint, Point endPoint, Color color,
            float alpha, float width) {

        Vector2f start = new Vector2f(startPoint.getX(), startPoint.getY());
        Vector2f end = new Vector2f(endPoint.getX(), endPoint.getY());

        float dx = startPoint.getX() - endPoint.getX();
        float dy = startPoint.getY() - endPoint.getY();

        Vector2f rightSide = new Vector2f(dy, -dx);
        if (rightSide.length() > 0) {
            rightSide.normalise();
            rightSide.scale(width / 2);
        }

        Vector2f leftSide = new Vector2f(-dy, dx);
        if (leftSide.length() > 0) {
            leftSide.normalise();
            leftSide.scale(width / 2);
        }

        Vector2f one = new Vector2f();
        Vector2f.add(leftSide, start, one);

        Vector2f two = new Vector2f();
        Vector2f.add(rightSide, start, two);

        Vector2f three = new Vector2f();
        Vector2f.add(rightSide, end, three);

        Vector2f four = new Vector2f();
        Vector2f.add(leftSide, end, four);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        GL11.glVertex2f(one.x, one.y);
        GL11.glVertex2f(two.x, two.y);
        GL11.glVertex2f(three.x, three.y);
        GL11.glVertex2f(four.x, four.y);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnd();
    }

    public void updateDisplay() {
        if (simulation != null) {
            // TODO render simulation

            // clear the screen and depth buffer
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            
            
            drawLine(new Point(50, 50), new Point(100, 100), new Color(255,
                    255, 255), (float) 0.5, 5);

            Display.update();
        }
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public void destroy() {
        Display.destroy();
    }

    public boolean isCloseRequested() {
        return Display.isCloseRequested();
    }

}
