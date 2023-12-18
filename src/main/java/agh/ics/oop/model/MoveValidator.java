package agh.ics.oop.model;

public interface MoveValidator {
    /**
     * Indicate if any object can move to the given position.
     *
     * @param position position checked for the movement possibility.
     * @return true if the object can move to that position.
     */
    NextMoveType moveType(Vector2D position);
}