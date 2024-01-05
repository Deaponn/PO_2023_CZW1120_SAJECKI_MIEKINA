package agh.ics.oop.window.controller;

import agh.ics.oop.Configuration;
import agh.ics.oop.model.MapType;
import agh.ics.oop.window.WindowController;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

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

    @Override
    public void start() {
//        this.configuration = new Configuration();
//
//        addField(setIntegerFormatter(mapWidth), configuration.mapWidth());
//        addField(setIntegerFormatter(mapHeight), configuration.mapHeight());
//        addField(setMapTypeChoiceBox(mapType), configuration.mapType());
//        addField(setIntegerFormatter(genomeLength), configuration.genomeLength());
//        addField(setFloatFormatter(randomGenomeChangeChance), configuration.randomGenomeChangeChance());
//        addField(setIntegerFormatter(startingPlantsNumber), configuration.startingPlantsNumber());
//        addField(setIntegerFormatter(plantEnergy), configuration.plantEnergy());
//        addField(setIntegerFormatter(numberOfGrowingPlants), configuration.numberOfGrowingPlants());
//        addField(setIntegerFormatter(startingAnimalsNumber), configuration.startingAnimalsNumber());
//        addField(setIntegerFormatter(requiredReproductionEnergy), configuration.requiredReproductionEnergy());
//        addField(setIntegerFormatter(energyPassedToChild), configuration.energyPassedToChild());
//        addField(setIntegerFormatter(minMutationsNumber), configuration.minMutationsNumber());
//        addField(setIntegerFormatter(maxMutationsNumber), configuration.maxMutationsNumber());
//        addField(setFloatFormatter(equatorSize), configuration.equatorSize());
//        addField(setFloatFormatter(plantGrowAtEquatorChance), configuration.plantGrowAtEquatorChance());
//        addField(saveSteps, configuration.saveSteps());
    }

    private static TextFormatter<Integer> setIntegerFormatter(TextField textField) {
        TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter());
        textField.setTextFormatter(formatter);
        return formatter;
    }

    private static TextFormatter<Float> setFloatFormatter(TextField textField) {
        TextFormatter<Float> formatter = new TextFormatter<>(new FloatStringConverter());
        textField.setTextFormatter(formatter);
        return formatter;
    }

    private static ChoiceBox<MapType> setMapTypeChoiceBox(ChoiceBox<MapType> choiceBox) {
        choiceBox.getItems().addAll(MapType.STANDARD, MapType.POISONOUS_PLANTS);
        choiceBox.setValue(MapType.STANDARD);
        return choiceBox;
    }

    private <V> void addField(TextFormatter<V> textFormatter, Property<V> field) {
        field.bind(textFormatter.valueProperty());
    }

    private void addField(TextFormatter<Integer> textFormatter, IntegerProperty field) {
        field.bind(textFormatter.valueProperty());
    }

    private void addField(TextFormatter<Float> textFormatter, FloatProperty field) {
        field.bind(textFormatter.valueProperty());
    }

    private <V> void addField(ChoiceBox<V> choiceBox, Property<V> field) {
        field.bind(choiceBox.valueProperty());
    }

    private void addField(CheckBox checkBox, Property<Boolean> field) {
        field.bind(checkBox.selectedProperty());
    }
}
