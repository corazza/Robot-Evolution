package org.rtevo.common;

public class Vector<T extends Number> {
    public T x;
    public T y;

    public Vector() {

    }

    public Vector(T x, T y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x.toString() + ", " + y.toString() + ")";
    }
    
}
