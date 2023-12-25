module Project.main {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.web;

    exports agh.ics.oop to javafx.graphics;
    exports agh.ics.oop.window.controller to javafx.fxml;
    opens agh.ics.oop.window.controller to javafx.fxml;
}