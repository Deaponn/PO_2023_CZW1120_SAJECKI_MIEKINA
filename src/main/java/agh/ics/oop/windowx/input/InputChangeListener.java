package agh.ics.oop.windowx.input;

public interface InputChangeListener<E extends InputEvent<?>> {
    void inputChanged(E inputEvent);
}
