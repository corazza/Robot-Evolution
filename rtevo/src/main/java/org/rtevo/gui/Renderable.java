/**
 * 
 */
package org.rtevo.gui;

import org.lwjgl.util.Point;

/**
 * @author Jan Corazza
 * 
 */
public class Renderable {
    private Point start;
    private Point end;
    private float width;

    public int getStartX() {
        return start.getX();
    }

    public int getStartY() {
        return start.getY();
    }

    public int getEndX() {
        return end.getX();
    }

    public int getEndY() {
        return end.getY();
    }

    public float getWidth() {
        return width;
    }
}
