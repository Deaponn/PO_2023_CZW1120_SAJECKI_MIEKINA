package agh.ics.oop.model;

public class OutOfMapBoundsException extends Exception {
    public OutOfMapBoundsException(Vector2D vector) {
        super("Position " + vector.toString() + " is out of bounds");
    }
}
