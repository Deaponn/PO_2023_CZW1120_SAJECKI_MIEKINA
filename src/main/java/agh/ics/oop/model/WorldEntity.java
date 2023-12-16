package agh.ics.oop.model;

public interface WorldEntity extends WorldElement {
    MapDirection getDirection();
    void rotateBy(MoveDirection moveDirection);
}
