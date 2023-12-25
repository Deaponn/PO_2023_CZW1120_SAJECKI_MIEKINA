package agh.ics.oop.window;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WindowController {
    private final Map<String, Object> stateMap;

    protected WindowController() {
        this.stateMap = new HashMap<>();
    }

    public void send(String key, Object object) {
        this.stateMap.put(key, object);
    }

    public Optional<Object> getState(String key) {
        return Optional.ofNullable(this.stateMap.get(key));
    }
}
