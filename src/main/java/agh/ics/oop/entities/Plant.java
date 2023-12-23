package agh.ics.oop.entities;

import agh.ics.oop.model.Vector2D;
import agh.ics.oop.model.WorldElement;
import agh.ics.oop.model.WorldEntity;

import java.util.Objects;

public class Plant implements WorldElement {
    private final Vector2D position;
    public Plant(Vector2D initialPosition) {
        position = initialPosition;
    }

    public Vector2D getPosition() { return position; }

    @Override
    public boolean isAtPosition(Vector2D other) { return Objects.equals(position, other); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return Objects.equals(getPosition(), plant.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosition());
    }
}
