package agh.ics.oop.render;

import agh.ics.oop.model.Vector2D;

public abstract class Overlay {
    public Vector2D screenPosition;

    public Overlay(Vector2D screenPosition) {
        this.screenPosition = screenPosition;
    }
}
