package agh.ics.oop.entities;

import agh.ics.oop.model.Vector2D;
import agh.ics.oop.model.WorldElement;
import agh.ics.oop.render.AssignRenderer;
import agh.ics.oop.render.renderer.GroundRenderer;

@AssignRenderer(renderer = GroundRenderer.class)
public class Ground implements WorldElement {
    private final Vector2D position;

    public Ground(Vector2D position) {
        this.position = position;
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
