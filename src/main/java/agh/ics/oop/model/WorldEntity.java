package agh.ics.oop.model;

public abstract class WorldEntity extends WorldElement {
    protected MapDirection mapDirection;

    public MapDirection getDirection() {
        return this.mapDirection;
    }

    public void rotateBy(MoveDirection moveDirection) {
        this.mapDirection = mapDirection.rotateBy(moveDirection);
    }

    protected WorldEntity(Vector2D position, MapDirection mapDirection) {
        super(position);
        this.mapDirection = mapDirection;
    }
}
