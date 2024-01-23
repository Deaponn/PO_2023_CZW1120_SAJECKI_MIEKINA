package agh.ics.oop.model;

public interface ObjectEventEmitter<T> {
    void addEventSubscriber(ObjectEventListener<T> listener);
    void removeEventSubscriber(ObjectEventListener<T> listener);
    void notifySubscribers(String message);
    boolean isListenerSubscribed(ObjectEventListener<T> listener);
}
