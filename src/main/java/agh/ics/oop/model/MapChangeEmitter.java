package agh.ics.oop.model;

public interface MapChangeEmitter {
    void mapChangeSubscribe(MapChangeListener listener);
    void mapChangeUnsubscribe(MapChangeListener listener);
    void mapChangeNotify(String message);
    boolean mapChangeIsSubscribed(MapChangeListener listener);
}
