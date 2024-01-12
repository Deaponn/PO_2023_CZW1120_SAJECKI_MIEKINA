package agh.ics.oop.render;

import agh.ics.oop.model.*;
import agh.ics.oop.view.WorldView;
import javafx.scene.image.Image;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldRenderer {
    public final ImageMap imageMap;
    public final WorldView worldView;
    private final AssignmentMap assignmentMap;

    public WorldRenderer(ImageMap imageMap, WorldView worldView) {
        this.imageMap = imageMap;
        this.worldView = worldView;
        this.assignmentMap = new WorldRenderer.AssignmentMap();
    }

    public void renderView(WorldMap worldMap) {
        Boundary bounds = worldMap.getCurrentBounds();
        this.worldView.setGridBounds(bounds);
        bounds.mapAllPositions(worldMap::getElements)
                .forEach(this::tryRenderElementList);
        this.worldView.presentView();
    }

    public void renderElement(WorldElement element) throws IllegalRendererAssignment {
        this.assignmentMap.renderElement(this, element);
    }

    private void tryRenderElementList(List<WorldElement> elementList) {
        elementList.forEach(this::tryRenderElement);
    }

    private void tryRenderElement(WorldElement element) {
        try {
            this.renderElement(element);
            System.out.println("Render element @ " + element.getPosition());
        } catch (IllegalRendererAssignment e) {
            System.out.println("WorldRenderer: " + e.getMessage());
        }
    }

    public void putImage(Vector2D position, String imageKey) {
        try {
            Image image = this.imageMap.getImage(imageKey);
            if (image == null)
                return;
            this.worldView.put(position, image);
        } catch (OutOfMapBoundsException e) {
            System.out.println("Attempted to write outside of bounds: " + e.getMessage());
        }
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
