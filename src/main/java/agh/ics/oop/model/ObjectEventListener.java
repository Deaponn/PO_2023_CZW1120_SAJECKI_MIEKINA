package agh.ics.oop.model;

public interface ObjectEventListener<T> {
    void sendEventData(T load, String message);
}
