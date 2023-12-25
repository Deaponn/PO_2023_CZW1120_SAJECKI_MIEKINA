package agh.ics.oop.window;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Window<T> {
    private final Stage stage;
    private final String title;
    private final String layoutPath;

    private Pane viewRoot;
    private T viewController;

    public Window(Stage stage, String title, String layoutPath) {
        this.stage = stage;
        this.title = title;
        this.layoutPath = layoutPath;

        this.init();
    }

    public Window(String title, String layoutPath) {
        this(new Stage(), title, layoutPath);
    }

    private void init() {
        this.stage.setTitle(this.title);

        try {
            this.loadView();
            this.setView(this.viewRoot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getFXMLResourceURL());

        this.viewRoot = loader.load();
        this.viewController = loader.getController();
    }

    private void setView(Pane viewRoot) {
        Scene scene = new Scene(viewRoot);
        this.stage.setScene(scene);
        this.stage.minWidthProperty().bind(viewRoot.minWidthProperty());
        this.stage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    public T getViewController() {
        return this.viewController;
    }

    private URL getFXMLResourceURL() {
        return this.getClass()
                .getClassLoader()
                .getResource(this.layoutPath);
    }

    public void show() {
        this.stage.show();
    }
}
