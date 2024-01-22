package agh.ics.oop.entities;

import agh.ics.oop.model.Vector2D;
import agh.ics.oop.model.WorldElement;
import agh.ics.oop.render.AssignRenderer;
import agh.ics.oop.render.renderer.GroundRenderer;

@AssignRenderer(renderer = GroundRenderer.class)
public class Ground extends WorldElement {
    private final boolean isPoisoned;
    public Ground(Vector2D position, boolean isPoisoned) {
        super(position);
        this.isPoisoned = isPoisoned;
    }

    public Ground(Vector2D position) {
        this(position, false);
    }

    public boolean getIsPoisoned() {
        return this.isPoisoned;
    }
}
