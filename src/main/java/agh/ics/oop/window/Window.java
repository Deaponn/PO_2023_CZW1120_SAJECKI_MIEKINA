package agh.ics.oop.window;

import agh.ics.oop.resource.Resources;
import agh.ics.oop.windowx.ErrorAlert;
import agh.ics.oop.windowx.Toast;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;

public class Window<T extends WindowController> {
    private final Stage stage;
    private final String title;
    private final String layoutPath;
    private boolean isInit;
    private boolean isClosed;

    private Pane root;
    private T controller;

    public Window(Stage stage, String title, String layoutPath) {
        this.stage = stage;
        this.title = title;
        this.layoutPath = layoutPath;
        this.isInit = false;
        this.isClosed = false;
    }

    public Window(String title, String layoutPath) {
        this(new Stage(), title, layoutPath);
    }

    public void start(Bundle bundle) {
        this.init();
        this.controller.setBundle(bundle);
        this.controller.setWindow(this);
        this.controller.start();
        this.setOnClose(event -> isClosed = true);
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
        URL layoutURL = Resources.getResourceURL(Window.class, this.layoutPath)
                        .orElseThrow();

        loader.setLocation(layoutURL);

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

    public Pane getRoot() {
        return this.root;
    }

    public void addNode(Node node) {
        this.root.getChildren().add(node);
    }

    public void removeNode(Node node) {
        this.root.getChildren().remove(node);
    }

    public void show() {
        this.stage.show();
    }

    public void close() {
        this.stage.close();
        this.isClosed = true;
    }

    public boolean isClosed() {
        return this.isClosed;
    }

    public void setOnClose(EventHandler<WindowEvent> eventHandler) {
        this.stage.setOnHiding(eventHandler);
    }

    // for me, the above setOnClose wasn't working when closing the window with upper right X mark
    public void setStageOnCloseRequest(EventHandler<WindowEvent> eventHandler) {
        this.stage.setOnCloseRequest(eventHandler);
    }

    public ErrorAlert showErrorAlert(String title, String headerText, String contentText) {
        return new ErrorAlert(this, title, headerText, contentText);
    }

    public void showToast(String message, Toast.Duration duration) {
        Toast toast = new Toast(this, message, duration);
        this.addNode(toast);
    }
}
