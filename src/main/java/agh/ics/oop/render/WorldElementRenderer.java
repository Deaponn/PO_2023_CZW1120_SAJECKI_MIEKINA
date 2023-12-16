package agh.ics.oop.render;

import agh.ics.oop.model.WorldElement;

public abstract class WorldElementRenderer<T extends WorldElement> {
    private final Class<T> elementClass;

    public WorldElementRenderer(Class<T> elementClass) {
        this.elementClass = elementClass;
    }

    public abstract int render(T element);

    public final Class<T> getElementClass() {
        return this.elementClass;
    }
}
