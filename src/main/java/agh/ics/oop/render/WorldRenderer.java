package agh.ics.oop.render;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.WorldElement;
import agh.ics.oop.model.WorldMap;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class WorldRenderer {
    private final ImageResourceAtlas imageResourceAtlas;
    private final AssignmentMap assignmentMap;

    public WorldRenderer(ImageResourceAtlas imageResourceAtlas) {
        this.imageResourceAtlas = imageResourceAtlas;
        this.assignmentMap = new WorldRenderer.AssignmentMap();
    }

    public void render(WorldMap worldMap) {
        Boundary boundary = worldMap.getCurrentBounds();
        // TODO("Complete tile rendering")
        boundary.mapAllPositions(worldMap::getElement);
    }

    private static class AssignmentMap {
        private final Map<Class<?>, WorldElementRenderer<?>> registeredElementRendererMap;

        public AssignmentMap() {
            this.registeredElementRendererMap = new HashMap<>();
        }

        private Class<? extends WorldElementRenderer<?>> getAssignedRenderer(Class<?> elementClass)
                throws IllegalRendererAssignment {
            try {
                AssignRenderer assignRenderer = elementClass.getAnnotation(AssignRenderer.class);
                return assignRenderer.renderer();
            } catch (NullPointerException e) {
                // assign a renderer to the element class (@AssignRenderer)
                throw new IllegalRendererAssignment("no assigned renderer found", elementClass);
            }
        }

        private <T extends WorldElement> void tryRegisterElementRenderer(T element)
                throws IllegalRendererAssignment {
            Class<?> elementClass = element.getClass();
            Class<? extends WorldElementRenderer<?>> elementRendererClass = this.getAssignedRenderer(elementClass);
            try {
                this.registeredElementRendererMap.put(element.getClass(), elementRendererClass
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
        public <T extends WorldElement> WorldElementRenderer<T> getElementRenderer(T element)
                throws IllegalRendererAssignment {
            WorldElementRenderer<?> elementRenderer = this.registeredElementRendererMap.get(element.getClass());
            if (elementRenderer == null) {
                this.tryRegisterElementRenderer(element);
                elementRenderer = this.getElementRenderer(element);
            }
            try {
                return (WorldElementRenderer<T>) elementRenderer;
            } catch (ClassCastException e) {
                throw new IllegalRendererAssignment("illegal element class supplied",
                        element, elementRenderer);
            }
        }

        public <T extends WorldElement> void renderElement(WorldRenderer renderer, T element)
                throws IllegalRendererAssignment {
            WorldElementRenderer<T> elementRenderer = this.getElementRenderer(element);
            try {
                elementRenderer.render(renderer, element);
            } catch (ClassCastException e) {
                throw new IllegalRendererAssignment("illegal element class supplied",
                        element, elementRenderer);
            }
        }
    }
}
