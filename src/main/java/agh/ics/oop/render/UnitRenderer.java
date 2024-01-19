package agh.ics.oop.render;

public interface UnitRenderer<U> {
    void render(WorldRenderer renderer, U unit);
}
