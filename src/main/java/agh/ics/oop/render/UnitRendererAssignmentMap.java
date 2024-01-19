package agh.ics.oop.render;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class UnitRendererAssignmentMap {
    private final Map<Class<?>, UnitRenderer<?>> registeredUnitRendererMap;

    public UnitRendererAssignmentMap() {
        this.registeredUnitRendererMap = new HashMap<>();
    }

    private Class<? extends UnitRenderer<?>> getAssignedRendererClass(Class<?> unitClass)
            throws IllegalRendererAssignment {
        return this.getAssignedRendererClass(unitClass, false);
    }

    private Class<? extends UnitRenderer<?>> getAssignedRendererClass(Class<?> unitClass, boolean isSuperclass)
            throws IllegalRendererAssignment {
        AssignRenderer assignRenderer = unitClass.getAnnotation(AssignRenderer.class);
        if (assignRenderer != null && !(isSuperclass && assignRenderer.restrictInherit()))
            return assignRenderer.renderer();

        // try searching in a superclass
        Class<?> unitSuperClass = unitClass.getSuperclass();
        if (unitSuperClass != null)
            return this.getAssignedRendererClass(unitSuperClass, true);
        // assign a renderer to the element class (@AssignRenderer)
        throw new IllegalRendererAssignment("no assigned renderer found", unitClass);
    }

    private <U> void tryRegisterUnitRenderer(WorldRenderer renderer, U unit)
            throws IllegalRendererAssignment {
        Class<?> elementClass = unit.getClass();
        Class<? extends UnitRenderer<?>> elementRendererClass = this.getAssignedRendererClass(elementClass);
        try {
            this.registeredUnitRendererMap.put(unit.getClass(), elementRendererClass
                    .getConstructor(WorldRenderer.class)
                    .newInstance(renderer));
        } catch (NoSuchMethodException e) {
            // you need to implement 1-parameter renderer constructor.
            throw new IllegalRendererAssignment("renderer constructor not found",
                    elementClass, elementRendererClass);
        } catch (InvocationTargetException e) {
            // on exception thrown inside renderer constructor.
            throw new IllegalRendererAssignment("renderer constructor exception",
                    elementClass, elementRendererClass);
        } catch (InstantiationException e) {
            // render class is an abstract class or an interface.
            throw new IllegalRendererAssignment("renderer is abstract, cannot be instantiated",
                    elementClass, elementRendererClass);
        } catch (IllegalAccessException e) {
            // renderer constructor is not visible
            throw new IllegalRendererAssignment("renderer constructor is not public",
                    elementClass, elementRendererClass);
        }
    }

    @SuppressWarnings("unchecked")
    public <U> UnitRenderer<U> getUnitRenderer(WorldRenderer renderer, U unit)
            throws IllegalRendererAssignment {
        UnitRenderer<?> unitRenderer = this.registeredUnitRendererMap.get(unit.getClass());
        if (unitRenderer == null) {
            this.tryRegisterUnitRenderer(renderer, unit);
            unitRenderer = this.getUnitRenderer(renderer, unit);
        }
        try {
            return (UnitRenderer<U>) unitRenderer;
        } catch (ClassCastException e) {
            throw new IllegalRendererAssignment("illegal unit class supplied",
                    unit, unitRenderer);
        }
    }

    public <U> void renderUnit(WorldRenderer renderer, U unit)
            throws IllegalRendererAssignment {
        UnitRenderer<U> unitRenderer = this.getUnitRenderer(renderer, unit);
        try {
            unitRenderer.render(renderer, unit);
        } catch (ClassCastException e) {
            throw new IllegalRendererAssignment("illegal unit class supplied",
                    unit, unitRenderer);
        }
    }
}
