package agh.ics.oop.util;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;

public class Reactive<V> {
    private V value;
    private final SortedSet<Bind<V, ?>> reactiveBindSet;
    private final Set<Consumer<V>> changeConsumerSet;

    public Reactive(V initialValue) {
        this.value = initialValue;
        this.reactiveBindSet = new TreeSet<>();
        this.changeConsumerSet = new HashSet<>();
    }

    public void bindTo(Reactive<V> reactive) {
        Reactive.Bind<V, V> identityBind = new Reactive.Bind<>(this, (v) -> v);
        reactive.reactiveBindSet.add(identityBind);
    }

    public <S> void bindTo(Reactive<S> reactive, Function<S, V> mapper) {
        Reactive.Bind<S, V> mappedBind = new Reactive.Bind<>(this, mapper);
        reactive.reactiveBindSet.add(mappedBind);
    }

    public <S> void bindTo(Reactive<S> reactive, Function<S, V> mapper, ReactivePropagate propagate) {
        Reactive.Bind<S, V> mappedBind = new Reactive.Bind<>(this, mapper, propagate);
        reactive.reactiveBindSet.add(mappedBind);
    }

    public void unbindFrom(Reactive<V> reactive) {
        Reactive.Bind<V, ?> anyBind = new Reactive.Bind<>(this, (v) -> v);
        reactive.reactiveBindSet.remove(anyBind);
    }

    public void addOnChange(Consumer<V> consumer) {
        this.changeConsumerSet.add(consumer);
    }

    public void setValue(V value) {
        this.acceptNewValue(value);
        this.emitValueToAll();
    }

    private void acceptNewValue(V value) {
        if (!this.value.equals(value)) {
            this.value = value;
        }
    }

    private void emitValueToReactives() {
        this.reactiveBindSet.forEach(bind -> bind.apply(this.value));
    }

    private void emitValueToListeners() {
        this.changeConsumerSet.forEach(consumer -> consumer.accept(this.value));
    }

    private void emitValueToAll() {
        this.emitValueToReactives();
        this.emitValueToListeners();
    }

    public V getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    private static final class Bind<S, T> implements Comparable<Bind<?, ?>> {
        private final Reactive<T> receiver;
        private final ReactivePropagate propagate;
        private final Function<S, T> mapper;

        public Bind(Reactive<T> receiver, Function<S, T> mapper, ReactivePropagate propagate) {
            this.receiver = receiver;
            this.propagate = propagate;
            this.mapper = mapper;
        }

        public Bind(Reactive<T> receiver, Function<S, T> mapper) {
            this(receiver, mapper, ReactivePropagate.NONE);
        }

        public void apply(S sourceValue) {
            // don't use setValue here, unless you want to have infinite cycles
            T targetValue = this.mapper.apply(sourceValue);
            this.receiver.acceptNewValue(targetValue);
            switch (this.propagate) {
                case REACTIVE_ONLY -> this.receiver.emitValueToReactives();
                case LISTENER_ONLY -> this.receiver.emitValueToListeners();
                case ALL -> this.receiver.emitValueToAll();
            }
        }

        @Override
        public int compareTo(@NotNull Reactive.Bind o) {
            return this.receiver == o.receiver ? 0 : 1;
        }
    }
}
