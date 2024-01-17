package agh.ics.oop.model;

import agh.ics.oop.render.AssignRenderer;
import agh.ics.oop.render.WorldElementARenderer;

@AssignRenderer(renderer = WorldElementARenderer.class)
public class WorldElementA extends WorldElement {
    Vector2D position;

    public WorldElementA(Vector2D position) {
        super(position);
    }

    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    @Override
    public boolean isAtPosition(Vector2D position) {
        return this.position.equals(position);
    }
}
