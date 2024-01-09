package agh.ics.oop.window;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Bundle {
    private final Map<String, Object> itemMap;
    private boolean isFrozen;

    public Bundle() {
        this.itemMap = new HashMap<>();
        this.isFrozen = false;
    }

    public Bundle send(String key, Object object) {
        if (!this.isFrozen) {
            this.itemMap.put(key, object);
        }
        return this;
    }

    public Bundle freeze() {
        this.isFrozen = true;
        return this;
    }

    public Optional<Object> getItem(String key) {
        return Optional.ofNullable(this.itemMap.get(key));
    }

    public <I> Optional<I> getItem(String key, Class<I> itemClass) {
        return this.getItem(key).map(itemClass::cast);
    }

    public static Bundle emptyBundle = new Bundle().freeze();
}
