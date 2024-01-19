package agh.ics.oop.render;

import agh.ics.oop.model.WorldElementA;

public class WorldElementARenderer implements UnitRenderer<WorldElementA> {
    public WorldElementARenderer(WorldRenderer renderer) {}

    @Override
    public void render(WorldRenderer renderer, WorldElementA element) {
        System.out.println("Render " + element.toString());
    }
}
