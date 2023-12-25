package agh.ics.oop;

import agh.ics.oop.window.LayoutPath;
import agh.ics.oop.window.Window;
import agh.ics.oop.window.controller.Launcher;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    public static void run(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Window<Launcher> launcherWindow = new Window<>(
                "Shiginima Launcher",
                LayoutPath.LAUCHER.path
        );

        launcherWindow.show();
    }
}
