package agh.ics.oop.view;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.image.ImageAtlasSampler;
import agh.ics.oop.render.image.ImageSampler;
import javafx.scene.Node;

import java.util.Collection;

public interface View<N extends Node> {
    void withViewLayers(Collection<ViewLayer> viewLayerCollection);
    void setGridBounds(Boundary bounds);
    void updateViewSize();
    Vector2D getViewSize();

    void putImageAtGrid(Vector2D position, ImageSampler sampler, ViewLayer viewLayer);
    void putImageAtScreenCoords(Vector2D screenPosition, ImageSampler sampler, ViewLayer viewLayer, float scale);
    void putTextAtScreenCoords(Vector2D position, ImageAtlasSampler sampler, ViewLayer viewLayer, float scale, String text);
    Vector2D getGridIndex(Vector2D position);

    void presentView();
    N getRoot();
}
