package agh.ics.oop.window;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.OutOfMapBoundsException;
import agh.ics.oop.model.Vector2D;
import javafx.scene.image.Image;

public interface WorldView {
    void setGridBounds(Vector2D size);
    void setGridBounds(Boundary bounds);
    void updateViewSize();
    void put(Vector2D position, Image image) throws OutOfMapBoundsException;
    Image get(Vector2D position) throws OutOfMapBoundsException;
    void presentView();
}
