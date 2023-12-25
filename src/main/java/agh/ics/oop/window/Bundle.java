package agh.ics.oop.window;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Bundle {
    private final Map<String, Object> paramsMap;
    private boolean isFinal;

    public Bundle() {
        this.paramsMap = new HashMap<>();
        this.isFinal = false;
    }

    public Bundle send(String key, Object object) {
        if (!this.isFinal) {
            this.paramsMap.put(key, object);
        }
        return this;
    }

    public Bundle setFinal() {
        this.isFinal = true;
        return this;
    }

    public Optional<Object> getParam(String key) {
        return Optional.ofNullable(this.paramsMap.get(key));
    }

    public static Bundle emptyBundle = new Bundle().setFinal();
}
