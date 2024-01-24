package agh.ics.oop.render;

import agh.ics.oop.model.WorldElementA;
import agh.ics.oop.view.ViewLayer;

public class WorldElementARenderer implements UnitRenderer<WorldElementA> {
    public WorldElementARenderer(WorldRenderer renderer) {}

    @Override
    public void render(WorldRenderer renderer, ViewLayer viewLayer, WorldElementA element) {
        System.out.println("Render " + element.toString());
    }
}
