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
        try {
            AssignRenderer assignRenderer = unitClass.getAnnotation(AssignRenderer.class);
            return assignRenderer.renderer();
        } catch (NullPointerException e) {
            // assign a renderer to the element class (@AssignRenderer)
            throw new IllegalRendererAssignment("no assigned renderer found", unitClass);
        }
    }

    private <U> void tryRegisterUnitRenderer(U unit)
            throws IllegalRendererAssignment {
        Class<?> elementClass = unit.getClass();
        Class<? extends UnitRenderer<?>> elementRendererClass = this.getAssignedRendererClass(elementClass);
        try {
            this.registeredUnitRendererMap.put(unit.getClass(), elementRendererClass
                    .getConstructor()
                    .newInstance());
        } catch (NoSuchMethodException e) {
            // you need to implement 0-parameter renderer constructor.
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
    public <U> UnitRenderer<U> getUnitRenderer(U unit)
            throws IllegalRendererAssignment {
        UnitRenderer<?> unitRenderer = this.registeredUnitRendererMap.get(unit.getClass());
        if (unitRenderer == null) {
            this.tryRegisterUnitRenderer(unit);
            unitRenderer = this.getUnitRenderer(unit);
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
        UnitRenderer<U> unitRenderer = this.getUnitRenderer(unit);
        try {
            unitRenderer.render(renderer, unit);
        } catch (ClassCastException e) {
            throw new IllegalRendererAssignment("illegal unit class supplied",
                    unit, unitRenderer);
        }
    }
}
