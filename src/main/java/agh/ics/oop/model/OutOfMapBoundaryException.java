package agh.ics.oop.model;

public class OutOfMapBoundaryException extends Exception {
    public OutOfMapBoundaryException(Vector2D vector) {
        super("Position " + vector.toString() + " is out of bounds");
    }
}
