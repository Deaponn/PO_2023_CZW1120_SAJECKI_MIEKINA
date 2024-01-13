package agh.ics.oop.view;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.OutOfMapBoundsException;
import agh.ics.oop.model.Vector2D;
import javafx.scene.Node;
import javafx.scene.image.Image;

public interface WorldView {
    void setGridBounds(Vector2D size);
    void setGridBounds(Boundary bounds);
    void updateViewSize();

    void putImageAtGrid(Vector2D position, Image image) throws OutOfMapBoundsException;
    void putImageAtScreenCoords(Vector2D screenPosition, Image image);
    void putTextAtScreenCoords(Vector2D position, String text);
    Image getImageAtGrid(Vector2D position) throws OutOfMapBoundsException;

    void presentView();
    Node getRoot();


}
