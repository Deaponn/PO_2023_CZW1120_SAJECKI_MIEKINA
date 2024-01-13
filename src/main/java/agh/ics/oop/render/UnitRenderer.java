package agh.ics.oop.render;

public abstract class UnitRenderer<U> {
    public UnitRenderer() {}
    public abstract void render(WorldRenderer renderer, U unit);
}
