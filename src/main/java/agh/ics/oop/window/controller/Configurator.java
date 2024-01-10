package agh.ics.oop.window.controller;

import agh.ics.oop.Configuration;
import agh.ics.oop.model.MapType;
import agh.ics.oop.window.WindowController;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

import static agh.ics.oop.Configuration.Fields.*;

public class Configurator extends WindowController {
    @FXML
    public TextField mapWidth;
    @FXML
    public TextField mapHeight;
    @FXML
    public ChoiceBox<MapType> mapType;
    @FXML
    public TextField genomeLength;
    @FXML
    public TextField randomGenomeChangeChance;
    @FXML
    public TextField startingPlantsNumber;
    @FXML
    public TextField plantEnergy;
    @FXML
    public TextField numberOfGrowingPlants;
    @FXML
    public TextField startingAnimalsNumber;
    @FXML
    public TextField requiredReproductionEnergy;
    @FXML
    public TextField energyPassedToChild;
    @FXML
    public TextField minMutationsNumber;
    @FXML
    public TextField maxMutationsNumber;
    @FXML
    // default is 0.2, according to Pareto rule
    public TextField equatorSize;
    @FXML
    // default is 0.8, according to Pareto rule
    public TextField plantGrowAtEquatorChance;
    @FXML
    public CheckBox saveSteps;

    private Configuration configuration;

    @Override
    public void start() {
        super.start();

        this.configuration = this.getBundleItem("configuration", Configuration.class).orElseThrow();

        this.addIntegerField(MAP_WIDTH, this.mapWidth);
        this.addIntegerField(MAP_HEIGHT, this.mapHeight);
        this.addEnumField(MAP_TYPE, this.mapType, MapType.class);
        this.addIntegerField(GENOME_LENGTH, this.genomeLength);
        this.addFloatField(RANDOM_GENOME_CHANGE_CHANCE, this.randomGenomeChangeChance);
        this.addIntegerField(STARTING_PLANTS_NUMBER, this.startingPlantsNumber);
        this.addIntegerField(PLANT_ENERGY, this.plantEnergy);
        this.addIntegerField(NUMBER_OF_GROWING_PLANTS, this.numberOfGrowingPlants);
        this.addIntegerField(STARTING_ANIMALS_NUMBER, this.startingAnimalsNumber);
        this.addIntegerField(MIN_REPRODUCE_ENERGY, this.requiredReproductionEnergy);
        this.addIntegerField(ENERGY_PASSED, this.energyPassedToChild);
        this.addIntegerField(MIN_MUTATIONS, this.minMutationsNumber);
        this.addIntegerField(MAX_MUTATIONS, this.maxMutationsNumber);
        this.addFloatField(EQUATOR_SIZE, this.equatorSize);
        this.addFloatField(PLANT_GROW_AT_EQUATOR_CHANCE, this.plantGrowAtEquatorChance);
        this.addBooleanField(SAVE_STEPS, this.saveSteps);
    }

    private static <V> ChangeListener<V> createConfigurationFieldListener(
            Configuration.Field<V> configurationField) {
        return (observable, previousValue, newValue) -> {
            try {
                configurationField.set(newValue);
            } catch (IllegalArgumentException e) {
                System.out.println("TODO: add toast: " + e);
            }
        };
    }

    private <V> void configureValueProperty(
            Property<V> valueProperty,
            Configuration.Field<V> configurationField) {
        valueProperty.addListener(Configurator.createConfigurationFieldListener(configurationField));
        valueProperty.setValue(configurationField.get());
    }

    private void addIntegerField(Configuration.Fields key, TextField textField) {
        this.addAnyField(key, textField, new IntegerStringConverter());
    }

    private void addFloatField(Configuration.Fields key, TextField textField) {
        this.addAnyField(key, textField, new FloatStringConverter());
    }

    private <V> void addAnyField(
            Configuration.Fields key, TextField textField, StringConverter<V> stringConverter) {
        Configuration.Field<V> configurationField = this.configuration.getField(key);
        TextFormatter<V> formatter = new TextFormatter<>(stringConverter);
        textField.setTextFormatter(formatter);
        this.configureValueProperty(formatter.valueProperty(), configurationField);
    }

    private <T extends Enum<T>> void addEnumField(
            Configuration.Fields key,
            ChoiceBox<T> choiceBox,
            Class<T> enumClass) {
        Configuration.Field<T> configurationField = this.configuration.getField(key);
        T[] enumValues = enumClass.getEnumConstants();
        if (enumValues == null) throw new RuntimeException("not an enum");
        choiceBox.getItems().addAll(enumValues);
        try {
            choiceBox.setValue(enumValues[0]);
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("empty enum");
        }
        this.configureValueProperty(choiceBox.valueProperty(), configurationField);
    }

    private void addBooleanField(
            Configuration.Fields key, CheckBox checkBox) {
        Configuration.Field<Boolean> configurationField = this.configuration.getField(key);
        this.configureValueProperty(checkBox.selectedProperty(), configurationField);
    }

    public static final String configurationPath = "res/save/save0.xml";
}
