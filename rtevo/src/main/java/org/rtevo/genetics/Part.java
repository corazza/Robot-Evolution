/**
 * 
 */
package org.rtevo.genetics;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.rtevo.util.RandUtil;

/**
 *
 */
public class Part {
	//performance of shapes
   	public static final float minWidth = 0.5f;
    public static final float maxWidth = 2f;
    public static final float minHeight = 0.5f;
    public static final float maxHeight = 2f;

    public float width;
    public float height;

    // joints connected to this shape
    public ArrayList<PartJoint> partJoints = new ArrayList<PartJoint>();
    
    //randomization performances
    public static Part random() {
        Part part = new Part();
        part.width = RandUtil.random(Part.minWidth, Part.maxWidth);
        part.height = RandUtil.random(Part.minHeight, Part.maxHeight);

        return part;
    }
    
    //getting anchor
    public Vec2 getAnchor(float percent) {
        float x = 0;
        float y = 0;

        float o = 2 * width + 2 * height;
        float d = percent * o;

        if (d < height) {
            x = 0;
            y = d;
        } else if (d < height + width) {
            x = d - height;
            y = height;
        } else if (d < 2 * height + width) {
            x = width;
            y = height - (d - height - width);
        } else {
            x = width - (d - height * 2 - width);
            y = 0;
        }

        x -= width / 2;
        y -= height / 2;

        // return new Vec2(width, -height);
        return new Vec2(x, y);
    }

    @Override
    public String toString() {
        return "Shape [width=" + width + ", height=" + height + "]";
    }

}
