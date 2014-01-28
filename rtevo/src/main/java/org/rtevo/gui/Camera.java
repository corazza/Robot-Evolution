package org.rtevo.gui;

import org.jbox2d.common.Vec2;

/**
 * Deals with transformations for world->screen coordinates.
 * 
 * Always at the center of the window.
 */
public class Camera {
    private final Renderer renderer;
    private Vector position = new Vector(0, 0);
    private Vector lastDrag;
    private static final float pixelsPerMeter = 250;
    private final float scrollScale = 0.2f;
    private float zoom = 0.1f;
    private float dragBufferX = 0;
    private float dragBufferY = 0;

    public Camera(Renderer renderer) {
        this.renderer = renderer;

        position.x = 0;
        position.y = -200;
    }

    public Vector translate(Vec2 worldPosition) {
        Vector point = new Vector();

        point.x = (int) (worldPosition.x * pixelsPerMeter) - position.x;
        point.y = (int) (worldPosition.y * pixelsPerMeter) - position.y;

        point.x = (int) (point.x * zoom);
        point.y = (int) (point.y * zoom);

        point.x += renderer.getWidth() / 2;
        point.y += renderer.getHeight() / 2;

        return point;
    }

    public Vector translateRelative(Vec2 worldPosition) {
        Vector point = new Vector();

        point.x = (int) (worldPosition.x * pixelsPerMeter);
        point.y = (int) (worldPosition.y * pixelsPerMeter);

        point.x = (int) (point.x * zoom);
        point.y = (int) (point.y * zoom);

        return point;
    }

    public void startDragging(int x, int y) {
        lastDrag = new Vector(x, y);
    }

    public void updateDragging(int x, int y) {
        float updateX = (x - lastDrag.x) / zoom;
        float updateY = (y - lastDrag.y) / zoom;

        if ((int) updateX == 0) {
            dragBufferX += updateX;
        }

        if ((int) updateY == 0) {
            dragBufferY += updateY;
        }

        updateX += (int) dragBufferX;
        updateY += (int) dragBufferY;

        dragBufferY -= (int) dragBufferY;
        dragBufferX -= (int) dragBufferX;

        position.x -= updateX;
        position.y -= updateY;

        lastDrag.x = x;
        lastDrag.y = y;
    }

    public void scrolled(int amount) {
        float newZoom = zoom - amount * scrollScale * zoom;

        if (newZoom > 0.02 && newZoom < 1) {
            zoom = newZoom;
            return;
        }

        if (zoom <= 0.02) {
            zoom = 0.021f;
        }

        if (zoom >= 1) {
            zoom = 0.99f;
        }

    }
}
