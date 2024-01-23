package agh.ics.oop.render;

import agh.ics.oop.view.ViewLayer;

public interface UnitRenderer<U> {
    void render(WorldRenderer renderer, ViewLayer viewLayer, U unit);
}
