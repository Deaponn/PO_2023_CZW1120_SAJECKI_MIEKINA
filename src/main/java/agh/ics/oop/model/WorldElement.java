package agh.ics.oop.model;

import agh.ics.oop.entities.Plant;

import java.util.Objects;

public abstract class WorldElement {
    protected Vector2D position;

    public Vector2D getPosition() {
        return this.position;
    }

    public void setPosition(Vector2D newPosition) {
        this.position = newPosition;
    }

    public boolean isAtPosition(Vector2D position) {
        return this.position.equals(position);
    }

    public WorldElement(Vector2D position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return Objects.equals(this.getPosition(), plant.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getPosition());
    }
}
