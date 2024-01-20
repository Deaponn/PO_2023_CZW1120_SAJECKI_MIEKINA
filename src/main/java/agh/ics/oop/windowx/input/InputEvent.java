package agh.ics.oop.windowx.input;

import java.util.function.Consumer;

public class InputEvent<S> {
    private final Status status;
    private final S source;

    protected InputEvent(Status status, S source) {
        this.source = source;
        this.status = status;
    }

    public InputEvent.Status getEventStatus() {
        return this.status;
    }

    public S getEventSource() {
        return this.source;
    }

    public InputEvent<S> onPassed(Consumer<InputEvent<S>> consumer) {
        if (this.status == Status.PASSED) consumer.accept(this);
        return this;
    }

    public InputEvent<S> onInvalid(Consumer<InputEvent<S>> consumer) {
        if (this.status == Status.INVALID) consumer.accept(this);
        return this;
    }

    public static <S> InputEvent<S> passed(S source) {
        return new InputEvent<>(Status.PASSED, source);
    }

    public static <S> InputEvent<S> invalid(S source) {
        return new InputEvent<>(Status.INVALID, source);
    }

    public enum Status {
        PASSED,
        INVALID
    }
}
