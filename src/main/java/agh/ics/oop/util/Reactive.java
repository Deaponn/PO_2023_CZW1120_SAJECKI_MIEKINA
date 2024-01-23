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

    public <S> void bindTo(Reactive<S> reactive, Function<S, V> converter) {
        Reactive.Bind<S, V> mappedBind = new Reactive.Bind<>(this, converter);
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
        this.value = value;
        this.sendOut();
    }

    public V getValue() {
        return this.value;
    }

    private void sendOut() {
        // don't use setValue here, unless you want to have infinite cycles
        this.reactiveBindSet.forEach(bind -> bind.apply(this.value));
        this.changeConsumerSet.forEach(consumer -> consumer.accept(this.value));
    }

    private static final class Bind<S, T> implements Comparable<Bind<?, ?>> {
        private final Reactive<T> receiver;
        private final Function<S, T> mapper;

        public Bind(Reactive<T> receiver, Function<S, T> mapper) {
            this.receiver = receiver;
            this.mapper = mapper;
        }

        public void apply(S sourceValue) {
            this.receiver.value = this.mapper.apply(sourceValue);
        }

        @Override
        public int compareTo(@NotNull Reactive.Bind o) {
            return this.receiver == o.receiver ? 0 : 1;
        }
    }
}
