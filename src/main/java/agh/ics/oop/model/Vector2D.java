package agh.ics.oop.model;

import java.util.Objects;

public class Vector2D {
    private final int x;
    private final int y;

    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D v) {
        this(v.x, v.y);
    }

    public Vector2D() {
        this(0, 0);
    }

    public Vector2D add(Vector2D v) {
        return new Vector2D(this.x + v.x, this.y + v.y);
    }

    public Vector2D subtract(Vector2D v) {
        return this.add(v.opposite());
    }

    public Vector2D multiply(Vector2D v) {
        return new Vector2D(this.x * v.x, this.y * v.y);
    }

    public Vector2D scale(int k) {
        return new Vector2D(this.x * k, this.y * k);
    }

    public Vector2D opposite() {
        return new Vector2D(-this.x, -this.y);
    }

    public boolean precedes(Vector2D v) {
        return this.x <= v.x && this.y <= v.y;
    }

    public boolean follows(Vector2D v) {
        return this.x > v.x && this.y > v.y;
    }

    public Vector2D upperRight(Vector2D v) {
        return new Vector2D(Integer.max(this.x, v.x), Integer.max(this.y, v.y));
    }

    public Vector2D lowerLeft(Vector2D v) {
        return new Vector2D(Integer.min(this.x, v.x), Integer.min(this.y, v.y));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2D = (Vector2D) o;
        return x == vector2D.x && y == vector2D.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("(%s,%s)", this.x, this.y);
    }
}
