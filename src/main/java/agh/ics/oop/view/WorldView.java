package agh.ics.oop.view;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.OutOfMapBoundsException;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.image.ImageSampler;
import javafx.scene.Node;

public interface WorldView<N extends Node> {
    void setGridBounds(Boundary bounds);
    void updateViewSize();

    void putImageAtGrid(Vector2D position, ImageSampler sampler) throws OutOfMapBoundsException;
    void putImageAtScreenCoords(Vector2D screenPosition, ImageSampler sampler, float scale);
    void putTextAtScreenCoords(Vector2D position, String text);

    void presentView();
    N getRoot();
}
