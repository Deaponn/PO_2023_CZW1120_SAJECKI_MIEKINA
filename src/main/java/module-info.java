module Project.main {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.web;
    requires org.jetbrains.annotations;

    opens agh.ics.oop;
    opens agh.ics.oop.model;

    exports agh.ics.oop;
    exports agh.ics.oop.util;
    exports agh.ics.oop.loop;
    exports agh.ics.oop.model;
    exports agh.ics.oop.entities;
    exports agh.ics.oop.resource;
    exports agh.ics.oop.window;
    exports agh.ics.oop.windowx;
    exports agh.ics.oop.windowx.input;
    exports agh.ics.oop.render.image;

    opens agh.ics.oop.window.controller to javafx.fxml;

    exports agh.ics.oop.window.controller to javafx.fxml;
    exports agh.ics.oop.view to javafx.graphics;
}