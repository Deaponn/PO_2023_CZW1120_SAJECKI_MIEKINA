package agh.ics.oop.render;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.WorldElement;
import agh.ics.oop.model.WorldMap;

import java.util.HashMap;
import java.util.Map;

public class WorldRenderer {
    private final Map<Class<?>, WorldElementRenderer<?>> registeredElementRendererMap;

    public WorldRenderer() {
        this.registeredElementRendererMap = new HashMap<>();
    }

    public <T extends WorldElement> void registerElementRenderer(WorldElementRenderer<T> worldElementRenderer) {
        Class<T> elementClass = worldElementRenderer.getElementClass();
        if (!this.registeredElementRendererMap.containsKey(elementClass))
            this.registeredElementRendererMap.put(elementClass, worldElementRenderer);
    }

    public void render(WorldMap worldMap) {
        Boundary boundary = worldMap.getCurrentBounds();
        // TODO("Complete tile rendering")
        boundary.<WorldElement>map(position -> null);
    }

    private WorldElementRenderer<?> getElementRenderer(WorldElement worldElement) {
        return this.registeredElementRendererMap.get(worldElement.getClass());
    }
}
