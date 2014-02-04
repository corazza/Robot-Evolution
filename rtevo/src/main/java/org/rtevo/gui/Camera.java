package org.rtevo.gui;

import org.jbox2d.common.Vec2;
import org.rtevo.common.Vector;

/**
 * Deals with transformations for world->screen coordinates.
 * 
 * Always at the center of the window.
 */
public class Camera {
    private final Renderer renderer;
    private Vector<Integer> position = new Vector<Integer>(0, 0);
    private Vector<Integer> lastDrag = new Vector<Integer>(0, 0);
    private static final double pixelsPerMeter = 250;
    private final double scrollScale = 0.2;
    private double zoom = 0.15;

    public Camera(Renderer renderer) {
        this.renderer = renderer;
    }

    public Vector<Integer> translate(Vector<Float> toTranslate) {
        Vector<Integer> point = new Vector<Integer>();

        point.x = (int) ((toTranslate.x * pixelsPerMeter - position.x) * zoom);
        point.y = (int) ((toTranslate.y * pixelsPerMeter - position.y) * zoom);

        point.x += renderer.getWidth() / 2;
        point.y += renderer.getHeight() / 2;

        return point;
    }

    public Vector<Integer> translate(Vec2 worldPosition) {
        Vector<Float> position = new Vector<Float>();
        position.x = worldPosition.x;
        position.y = worldPosition.y;
        return translate(position);
    }

    public Vector<Integer> translateRelative(Vec2 worldPosition) {
        Vector<Integer> point = new Vector<Integer>();

        point.x = (int) (worldPosition.x * pixelsPerMeter * zoom);
        point.y = (int) (worldPosition.y * pixelsPerMeter * zoom);

        return point;
    }

    public float translateRelative(float a) {
        return (float) (a * zoom);
    }

    public int translateRelative(int a) {
        return (int) (a * zoom);
    }

    public void startDragging(int x, int y) {
        lastDrag = new Vector<Integer>(x, y);
    }

    public void updateDragging(int x, int y) {
        position.x -= (int) ((x - lastDrag.x) / zoom);
        position.y -= (int) ((y - lastDrag.y) / zoom);

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
