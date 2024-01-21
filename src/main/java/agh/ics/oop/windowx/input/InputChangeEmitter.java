package agh.ics.oop.windowx.input;

public interface InputChangeEmitter<S, E extends InputEvent<S>> {
    void inputChangeSubscribe(InputChangeListener<E> listener);
    void inputChangeUnsubscribe(InputChangeListener<E> listener);
    void inputChangeNotify(E inputEvent);
}
