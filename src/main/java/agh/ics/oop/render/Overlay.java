package agh.ics.oop.render;

import agh.ics.oop.model.Vector2D;
import org.jetbrains.annotations.NotNull;

public abstract class Overlay implements Comparable<Overlay> {
    public Vector2D screenPosition;
    public int depthIndex;

    public Overlay(Vector2D screenPosition, int depthIndex) {
        this.screenPosition = screenPosition;
        this.depthIndex = depthIndex;
    }

    public abstract void updateOnFrame(WorldRenderer renderer);

    @Override
    public int compareTo(@NotNull Overlay overlay) {
        return overlay.depthIndex - this.depthIndex;
    }
}
