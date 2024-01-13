package agh.ics.oop.render;

public class IllegalRendererAssignment extends Exception {
    public IllegalRendererAssignment(String message, Class<?> elementClass) {
        super("renderer cannot be assigned to element " + elementClass.getCanonicalName() +
                ": " + message);
    }

    public IllegalRendererAssignment(String message, Class<?> unitClass, Class<?> unitRendererClass) {
        super("renderer " + unitRendererClass.getCanonicalName() +
                " is not assignable to element " + unitClass.getCanonicalName() + ": " + message);
    }

    public IllegalRendererAssignment(String message, Object unit, UnitRenderer<?> unitRenderer) {
        this(message, unit.getClass(), unitRenderer.getClass());
    }
}
