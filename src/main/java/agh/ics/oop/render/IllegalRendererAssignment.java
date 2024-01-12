package agh.ics.oop.render;

import agh.ics.oop.model.WorldElement;

public class IllegalRendererAssignment extends Exception {
    public IllegalRendererAssignment(String message, Class<?> elementClass) {
        super("renderer cannot be assigned to element " + elementClass.getCanonicalName() +
                ": " + message);
    }

    public IllegalRendererAssignment(String message, Class<?> elementClass, Class<?> elementRendererClass) {
        super("renderer " + elementRendererClass.getCanonicalName() +
                " is not assignable to element " + elementClass.getCanonicalName() + ": " + message);
    }

    public IllegalRendererAssignment(String message, WorldElement element, WorldElementRenderer<?> elementRenderer) {
        this(message, element.getClass(), elementRenderer.getClass());
    }
}
