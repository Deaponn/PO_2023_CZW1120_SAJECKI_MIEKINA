package agh.ics.oop.window;

import java.util.Optional;

public class WindowController {
    private Bundle bundle;
    protected Window<?> window;

    protected WindowController() {}

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
    public void setWindow(Window<?> window) {
        this.window = window;
    }

    @Deprecated
    public Optional<Object> getBundleItem(String key) {
        return this.bundle.getItem(key);
    }

    public <I> Optional<I> getBundleItem(String key, Class<I> itemClass) {
        return this.bundle.getItem(key, itemClass);
    }

    public void start() {
        if (this.bundle == null)
            throw new IllegalStateException("setBundle was not called before start");
    }
}
