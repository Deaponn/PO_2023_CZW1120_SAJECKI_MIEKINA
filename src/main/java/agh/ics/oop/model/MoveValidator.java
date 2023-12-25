package agh.ics.oop.model;

public interface MoveValidator {
    /**
     * Indicate if any object can move to the given position.
     *
     * @param position position checked for the movement possibility.
     * @return type of move that has to be done to the animal.
     */
    NextMoveType moveType(Vector2D position, MapDirection direction);
}