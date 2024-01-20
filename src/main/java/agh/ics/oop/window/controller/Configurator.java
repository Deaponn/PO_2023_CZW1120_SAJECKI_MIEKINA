package agh.ics.oop.window.controller;

import agh.ics.oop.Configuration;
import agh.ics.oop.model.MapType;
import agh.ics.oop.window.WindowController;
import agh.ics.oop.windowx.Toast;
import agh.ics.oop.windowx.input.*;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import static agh.ics.oop.Configuration.Fields.*;

public class Configurator
        extends WindowController
        implements InputChangeListener<InputEvent<InputField<?, Configuration.Fields>>> {
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

    private ConfigurationDialogMediator dialogMediator;

    @Override
    public void start() {
        super.start();

        Configuration configuration = this.getBundleItem("configuration", Configuration.class)
                .orElseThrow();
        this.dialogMediator = new ConfigurationDialogMediator(configuration);
        this.dialogMediator.inputChangeSubscribe(this);

        this.addIntegerField(MAP_WIDTH, this.mapWidth);
        this.addIntegerField(MAP_HEIGHT, this.mapHeight);
        this.addEnumField(MAP_TYPE, this.mapType, MapType.STANDARD);
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

    private void addIntegerField(Configuration.Fields key, TextField textField) {
        new IntegerInputField<>(key, textField, 0, this.dialogMediator);
    }

    private void addFloatField(Configuration.Fields key, TextField textField) {
        new FloatInputField<>(key, textField, 0f, this.dialogMediator);
    }

    private <T extends Enum<T>> void addEnumField(
            Configuration.Fields key,
            ChoiceBox<T> choiceBox,
            T initialValue) {
        new EnumInputField<>(key, choiceBox, initialValue, this.dialogMediator);
    }

    private void addBooleanField(
            Configuration.Fields key, CheckBox checkBox) {
        new BooleanInputField<>(key, checkBox, false, this.dialogMediator);
    }

    public static final String configurationPath = "res/save/save0.xml";

    @Override
    public void inputChanged(InputEvent<InputField<?, Configuration.Fields>> inputEvent) {
        inputEvent
                .onPassed(event ->
                        this.window.showToast("Saved to disk",
                                Toast.Duration.SHORT))
                .onInvalid(event ->
                        this.window.showToast("Incorrect value in " +
                                        event.getEventSource().getFieldID(),
                                Toast.Duration.LONG));
    }
}
