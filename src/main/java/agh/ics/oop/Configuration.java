package agh.ics.oop;

import agh.ics.oop.model.MapType;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

public class Configuration implements Serializable {
    public EnumMap<Fields, Object> fieldMap;

    public Configuration() {
        this.fieldMap = new EnumMap<>(Fields.class);
    }

    public void set(Fields key, Object value) {
        this.fieldMap.put(key, value);
    }

    public Object get(Fields key) {
        Object value = this.fieldMap.get(key);
        if (value == null) return Configuration.getDefault(key);
        return value;
    }

    private static Object getDefault(Fields key) {
        return Configuration.defaultFieldMap.get(key);
    }

    private static final EnumMap<Fields, Object> defaultFieldMap = new EnumMap<>(
            Map.ofEntries(
                    Map.entry(Fields.MAP_WIDTH, 13),
                    Map.entry(Fields.MAP_HEIGHT, 8),
                    Map.entry(Fields.MAP_TYPE, MapType.STANDARD),
                    Map.entry(Fields.GENOME_LENGTH, 8),
                    Map.entry(Fields.RANDOM_GENOME_CHANGE_CHANCE, 0f),
                    Map.entry(Fields.STARTING_PLANTS_NUMBER, 20),
                    Map.entry(Fields.PLANT_ENERGY, 6),
                    Map.entry(Fields.NUMBER_OF_GROWING_PLANTS, 2),
                    Map.entry(Fields.STARTING_ANIMALS_NUMBER, 4),
                    Map.entry(Fields.REQUIRED_REPRODUCTION_ENERGY, 6),
                    Map.entry(Fields.ENERGY_PASSED_TO_CHILD, 3),
                    Map.entry(Fields.MIN_MUTATIONS_NUMBER, 0),
                    Map.entry(Fields.MAX_MUTATIONS_NUMBER, 3),
                    Map.entry(Fields.EQUATOR_SIZE, 0.2f),
                    Map.entry(Fields.PLANT_GROW_AT_EQUATOR_CHANCE, 0.8f),
                    Map.entry(Fields.SAVE_STEPS, false)
            ));

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
