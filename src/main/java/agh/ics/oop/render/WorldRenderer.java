package agh.ics.oop.render;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.WorldElement;
import agh.ics.oop.model.WorldMap;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class WorldRenderer {
    private final Map<Class<?>, WorldElementRenderer<?>> registeredElementRendererMap;
    private final ImageResourceAtlas imageResourceAtlas;

    public WorldRenderer(ImageResourceAtlas imageResourceAtlas) {
        this.registeredElementRendererMap = new HashMap<>();
        this.imageResourceAtlas = imageResourceAtlas;
    }

    private Class<? extends WorldElementRenderer<?>> getAssignedRenderer(Class<?> elementClass) {
        AssignRenderer assignRenderer = elementClass.getAnnotation(AssignRenderer.class);
        return assignRenderer.renderer();
    }


    @SuppressWarnings("unchecked")
    private <T extends WorldElement> WorldElementRenderer<T> tryRegisterElementRenderer(T element)
            throws IllegalRendererAssignment {
        Class<?> elementClass = element.getClass();
        Class<? extends WorldElementRenderer<?>> elementRendererClass = this.getAssignedRenderer(elementClass);
        try {

            WorldElementRenderer<T> elementRenderer =
                    ((Class<WorldElementRenderer<T>>) elementRendererClass)
                            .getConstructor()
                            .newInstance();
            this.registeredElementRendererMap.put(element.getClass(), elementRenderer);
            return elementRenderer;
        } catch (ClassCastException e) {
            throw new IllegalRendererAssignment("illegal element class supplied",
                    elementClass, elementRendererClass);
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
    public <T extends WorldElement> WorldElementRenderer<T> getElementRenderer(T element)
            throws IllegalRendererAssignment {
        WorldElementRenderer<?> elementRenderer = this.registeredElementRendererMap.get(element.getClass());
        if (elementRenderer == null) {
            elementRenderer = this.tryRegisterElementRenderer(element);
        }
        try {
            return (WorldElementRenderer<T>) elementRenderer;
        } catch (ClassCastException e) {
            throw new IllegalRendererAssignment("illegal element class supplied",
                    element, elementRenderer);
        }
    }

    public void render(WorldMap worldMap) {
        Boundary boundary = worldMap.getCurrentBounds();
        // TODO("Complete tile rendering")
        boundary.mapAllPositions(worldMap::getElement);
    }
}
