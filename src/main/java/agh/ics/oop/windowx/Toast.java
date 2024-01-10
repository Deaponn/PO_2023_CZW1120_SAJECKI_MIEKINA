package agh.ics.oop.windowx;

import agh.ics.oop.window.Window;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Toast extends StackPane {
    private final Window<?> window;
    private final Toast.Duration duration;

    public Toast(Window<?> window, String message, Toast.Duration duration) {
        super();
        this.window = window;
        this.duration = duration;

        Text text = new Text(message);
        text.setFill(Paint.valueOf("#303030"));
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(text.getFont().getSize() * (message.length() + 2));
        rectangle.setHeight(20);
        rectangle.setArcWidth(20);
        rectangle.setArcHeight(20);
        rectangle.setFill(Paint.valueOf("#e4e4e4"));

        this.getChildren().addAll(rectangle, text);
        Pane root = this.window.getRoot();
        root.widthProperty().subscribe(width -> this.setLayoutX((double) width / 2));
        root.heightProperty().subscribe(height -> this.setLayoutY((double) height - 20));
    }

    public enum Duration {
        SHORT,
        MEDIUM,
        LONG
    }
}
