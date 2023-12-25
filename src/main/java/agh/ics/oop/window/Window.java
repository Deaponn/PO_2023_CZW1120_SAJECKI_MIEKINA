package agh.ics.oop.window;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Window<T extends WindowController> {
    private final Stage stage;
    private final String title;
    private final String layoutPath;
    private boolean isInit;

    private Pane root;
    private T controller;

    public Window(Stage stage, String title, String layoutPath) {
        this.stage = stage;
        this.title = title;
        this.layoutPath = layoutPath;
        this.isInit = false;
    }

    public Window(String title, String layoutPath) {
        this(new Stage(), title, layoutPath);
    }

    public void start(Bundle bundle) {
        this.init();
        this.controller.setBundle(bundle);
        this.controller.start();
        this.show();
    }

    public void start() {
        this.start(Bundle.emptyBundle);
    }

    private void init() {
        if (!this.isInit) {
            this.isInit = true;
            this.stage.setTitle(this.title);

            try {
                this.loadView();
                this.setView(this.root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getFXMLResourceURL());

        this.root = loader.load();
        this.controller = loader.getController();
    }

    private void setView(Pane viewRoot) {
        Scene scene = new Scene(viewRoot);
        this.stage.setScene(scene);
        this.stage.minWidthProperty().bind(viewRoot.minWidthProperty());
        this.stage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    public T getController() {
        return this.controller;
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
