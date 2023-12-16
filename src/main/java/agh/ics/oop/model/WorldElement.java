package agh.ics.oop.model;

public interface WorldElement {
    Vector2D getPosition();
    boolean isAtPosition(Vector2D position);
}
