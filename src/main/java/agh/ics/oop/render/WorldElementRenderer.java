package agh.ics.oop.render;

import agh.ics.oop.model.WorldElement;

public abstract class WorldElementRenderer<T extends WorldElement> {
    public WorldElementRenderer() {}
    public abstract void render(WorldRenderer renderer, T element);
}
