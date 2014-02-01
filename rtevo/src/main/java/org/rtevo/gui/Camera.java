package org.rtevo.gui;

import org.jbox2d.common.Vec2;

/**
 * Deals with transformations for world->screen coordinates.
 * 
 * Always at the center of the window.
 */
public class Camera {
    private final Renderer renderer;
    private Vector position = new Vector();
    private Vector lastDrag = new Vector();
    private static final double pixelsPerMeter = 250;
    private final double scrollScale = 0.2;
    private double zoom = 0.15;

    public Camera(Renderer renderer) {
        this.renderer = renderer;
    }

    public Vector translate(Vec2 worldPosition) {
        Vector point = new Vector();

        point.x = (int) ((worldPosition.x * pixelsPerMeter - position.x) * zoom);
        point.y = (int) ((worldPosition.y * pixelsPerMeter - position.y) * zoom);

        point.x += renderer.getWidth() / 2;
        point.y += renderer.getHeight() / 2;

        return point;
    }

    public Vector translateRelative(Vec2 worldPosition) {
        Vector point = new Vector();

        point.x = (int) (worldPosition.x * pixelsPerMeter * zoom);
        point.y = (int) (worldPosition.y * pixelsPerMeter * zoom);

        return point;
    }

    public void startDragging(int x, int y) {
        lastDrag = new Vector(x, y);
    }

    public void updateDragging(int x, int y) {
        position.x -= (x - lastDrag.x) / zoom;
        position.y -= (y - lastDrag.y) / zoom;

        lastDrag.x = x;
        lastDrag.y = y;
    }

    public void scrolled(int amount) {
        float newZoom = (float) (zoom - amount * scrollScale * zoom);
        if (newZoom > 0.02 && newZoom < 1) {
            zoom = newZoom;
        }
    }

}
