package agh.ics.oop.window;

import java.util.Optional;

public class WindowController {
    private Bundle bundle;

    protected WindowController() {}

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public Optional<Object> getParam(String key) {
        return this.bundle.getParam(key);
    }

    public void start() {
        if (this.bundle == null)
            throw new IllegalStateException("setBundle was not called before start");
    }
}
