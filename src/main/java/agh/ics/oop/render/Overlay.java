package agh.ics.oop.render;

import agh.ics.oop.model.Vector2D;
import agh.ics.oop.util.Reactive;
import org.jetbrains.annotations.NotNull;

public abstract class Overlay implements Comparable<Overlay> {
    public Vector2D screenPosition;
    public int depthIndex;
    public Reactive<Boolean> isVisible;

    public Overlay(Vector2D screenPosition, int depthIndex) {
        this.screenPosition = screenPosition;
        this.depthIndex = depthIndex;
        this.isVisible = new Reactive<>(true);
    }

    public void hide() {
        this.isVisible.setValue(false);
    }

    public void show() {
        this.isVisible.setValue(true);
    }

    public abstract void updateOnFrame(WorldRenderer renderer);

    @Override
    public int compareTo(@NotNull Overlay overlay) {
        return overlay.depthIndex - this.depthIndex;
    }
}
