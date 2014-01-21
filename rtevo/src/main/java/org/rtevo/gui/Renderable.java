/**
 * 
 */
package org.rtevo.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

/**
 * @author Jan Corazza
 * 
 */
public class Renderable {
    Body body;

    public Renderable(Body body) {
        this.body = body;
    }

    public void draw(Graphics2D g2d) {
        g2d.translate(0, 0);

        g2d.setColor(new Color(250, 250, 250));

        // System.out.println("position (" + getPosition().x + ", "
        // + getPosition().y + "), angle " + getAngle());

        g2d.translate(body.getPosition().x, body.getPosition().y);
        g2d.rotate(body.getAngle());

        g2d.fillRect(0, 0, 10, 10);

        g2d.rotate(-body.getAngle());
    }

    @Override
    public String toString() {
        return body.getPosition().toString();
    }

}
