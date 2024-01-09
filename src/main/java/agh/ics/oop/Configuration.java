package agh.ics.oop;

import agh.ics.oop.model.MapType;

import java.beans.JavaBean;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

@JavaBean
public class Configuration implements Serializable {
    public EnumMap<Fields, Field<?>> fieldMap;

    public Configuration() {
        this.fieldMap = new EnumMap<>(
            Map.ofEntries(
                    Map.entry(
                            Fields.MAP_WIDTH,
                            Field.of(13, Integer.class)),
                    Map.entry(
                            Fields.MAP_HEIGHT,
                            Field.of(8, Integer.class)),
                    Map.entry(
                            Fields.MAP_TYPE,
                            Field.of(MapType.STANDARD, MapType.class)),
                    Map.entry(
                            Fields.GENOME_LENGTH,
                            Field.of(8, Integer.class)),
                    Map.entry(
                            Fields.RANDOM_GENOME_CHANGE_CHANCE,
                            Field.of(0f, Float.class)),
                    Map.entry(
                            Fields.STARTING_PLANTS_NUMBER,
                            Field.of(20, Integer.class)),
                    Map.entry(
                            Fields.PLANT_ENERGY,
                            Field.of(6, Integer.class)),
                    Map.entry(
                            Fields.NUMBER_OF_GROWING_PLANTS,
                            Field.of(2, Integer.class)),
                    Map.entry(
                            Fields.STARTING_ANIMALS_NUMBER,
                            Field.of(4, Integer.class)),
                    Map.entry(
                            Fields.REQUIRED_REPRODUCTION_ENERGY,
                            Field.of(6, Integer.class)),
                    Map.entry(
                            Fields.ENERGY_PASSED_TO_CHILD,
                            Field.of(3, Integer.class)),
                    Map.entry(
                            Fields.MIN_MUTATIONS_NUMBER,
                            Field.of(0, Integer.class)),
                    Map.entry(
                            Fields.MAX_MUTATIONS_NUMBER,
                            Field.of(3, Integer.class)),
                    Map.entry(
                            Fields.EQUATOR_SIZE,
                            Field.of(0.2f, Float.class)),
                    Map.entry(
                            Fields.PLANT_GROW_AT_EQUATOR_CHANCE,
                            Field.of(0.8f, Float.class)),
                    Map.entry(
                            Fields.SAVE_STEPS,
                            Field.of(false, Boolean.class))
            ));
    }

    public void set(Fields key, Object value) {
        this.getField(key).set(value);
    }

    public <V> V get(Fields key) {
        return this.<V>getField(key).get();
    }

    public <R> Stream<R> map(BiFunction<Fields, Field<?>, R> mapper) {
        return this.fieldMap.entrySet().stream()
                .map(entry -> mapper.apply(entry.getKey(), entry.getValue()));
    }

    @SuppressWarnings("unchecked")
    public <V> Field<V> getField(Fields key) {
        return (Field<V>) this.fieldMap.get(key);
    }

    @JavaBean
    public static class Field<V> implements Serializable {
        private Class<V> type;
        public V value;
        public V defaultValue;

        public Field() {}

        public void setType(Class<V> type) {
            this.type = type;
        }

        public void set(Object value) {
            this.value = this.type.cast(value);
        }

        public V get() {
            if (this.value == null) return this.defaultValue;
            return this.value;
        }

        public static <V> Field<V> of(V defaultValue, Class<V> type) {
            Field<V> field = new Field<>();
            field.value = defaultValue;
            field.setType(type);
            return field;
        }
    }

    public enum Fields {
        MAP_WIDTH,
        MAP_HEIGHT,
        // type of the map, project requirement F
        MAP_TYPE,
        GENOME_LENGTH,
        // if set to 0, simulation is standard,
        // if set to 0.2, it's "a bit of craziness", project requirement 3
        RANDOM_GENOME_CHANGE_CHANCE,
        STARTING_PLANTS_NUMBER,
        PLANT_ENERGY,
        NUMBER_OF_GROWING_PLANTS,
        STARTING_ANIMALS_NUMBER,
        REQUIRED_REPRODUCTION_ENERGY,
        // TODO: percentage or integer amount?
        //      if percentage, should the max be 200%?
        //      (for both parents to give out all the energy)
        ENERGY_PASSED_TO_CHILD,
        MIN_MUTATIONS_NUMBER,
        MAX_MUTATIONS_NUMBER,
        // default is 0.2, according to Pareto rule
        EQUATOR_SIZE,
        // default is 0.8, according to Pareto rule
        PLANT_GROW_AT_EQUATOR_CHANCE,
        // saving to external file
        SAVE_STEPS
    }
}
