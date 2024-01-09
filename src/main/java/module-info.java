module Project.main {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.web;

    opens agh.ics.oop;
    opens agh.ics.oop.model;

    exports agh.ics.oop.resource;
    exports agh.ics.oop.window;
    exports agh.ics.oop.windowx;

    exports agh.ics.oop to javafx.graphics;
    exports agh.ics.oop.window.controller to javafx.fxml;
    opens agh.ics.oop.window.controller to javafx.fxml;
}