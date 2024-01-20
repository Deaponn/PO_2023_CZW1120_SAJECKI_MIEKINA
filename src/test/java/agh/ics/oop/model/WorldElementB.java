package agh.ics.oop.model;


import agh.ics.oop.render.AssignRenderer;
import agh.ics.oop.render.WorldElementARenderer;

// illegal as fuck
@AssignRenderer(renderer = WorldElementARenderer.class)
public class WorldElementB extends WorldElement {
    public WorldElementB(Vector2D position) {
        super(position);
    }

    @Override
    public Vector2D getPosition() {
        return null;
    }

    @Override
    public boolean isAtPosition(Vector2D position) {
        return false;
    }
}
