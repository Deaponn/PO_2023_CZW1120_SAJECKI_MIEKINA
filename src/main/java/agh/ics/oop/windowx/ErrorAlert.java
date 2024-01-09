package agh.ics.oop.windowx;

import agh.ics.oop.resource.Resources;
import agh.ics.oop.window.Window;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class ErrorAlert extends Alert {
    private final Window<?> window;

    public ErrorAlert(Window<?> window, String title, String headerText, String contentText) {
        super(AlertType.ERROR);
        this.window = window;

        this.setTitle(title);
        this.setHeaderText(headerText);
        this.setContentText(contentText);
        this.setErrorGraphic();
    }

    public void setOnAcknowledge(Consumer<Window<?>> action) {
        this.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> action.accept(this.window));
    }

    public void setCloseWindow() {
        this.setOnAcknowledge(Window::close);
    }

    public void setDoNothing() {
        this.setOnAcknowledge(window -> {});
    }

    private void setErrorGraphic() {
        try (InputStream imageInputStream = Resources.getResourceInputStream(
                ErrorAlert.class, "error.png").orElseThrow()) {
            Image image = new Image(imageInputStream);
            ImageView errorImageView = new ImageView(image);
            errorImageView.setFitWidth(48);
            errorImageView.setFitHeight(48);
            this.setGraphic(errorImageView);
        } catch (IOException e) {
            System.out.println("Could not find error graphic.");
        }
    }
}
