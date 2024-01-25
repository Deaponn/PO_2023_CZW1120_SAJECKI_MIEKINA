package agh.ics.oop.model;

import agh.ics.oop.entities.Animal;
import agh.ics.oop.entities.AnimalFactory;
import agh.ics.oop.entities.Plant;
import agh.ics.oop.util.Reactive;

import java.util.*;

public class StatisticsCollector implements ObjectEventListener<WorldMap>, ObjectEventEmitter<Statistics> {
    private final Reactive<Integer> animalsCount = new Reactive<>(0);
    private final Reactive<Integer> plantsCount = new Reactive<>(0);
    private final Reactive<Integer> freeSquaresCount = new Reactive<>(0);
    private final Reactive<Genome> mostPopularGenome = new Reactive<>(GenomeFactory.defaultGenome());
    private final Reactive<Float> averageEnergy = new Reactive<>(0f);
    private final Reactive<Float> averageDaysLived = new Reactive<>(0f);
    private final Reactive<Float> averageKidsCount = new Reactive<>(0f);
    private int deadAnimalsCount;
    private int deadAnimalsDaysLivedSum;
    private Animal focusedAnimal = null;
    private final Reactive<Boolean> isAnimalFocused = new Reactive<>(false);
    private final Reactive<Genome> focusedAnimalGenome = new Reactive<>(GenomeFactory.defaultGenome());
    private final Reactive<Integer> focusedAnimalActiveGene = new Reactive<>(0);
    private final Reactive<Integer> focusedAnimalEnergy = new Reactive<>(0);
    private final Reactive<Integer> focusedAnimalPlantsEaten = new Reactive<>(0);
    private final Reactive<Integer> focusedAnimalKidsCount = new Reactive<>(0);
    private final Reactive<Integer> focusedAnimalAncestorsCount = new Reactive<>(0);
    private final Reactive<Integer> focusedAnimalAge = new Reactive<>(0);
    private final Reactive<Boolean> focusedAnimalIsAlive = new Reactive<>(false);
    private Map<Vector2D, List<Animal>> animals;
    private Map<Vector2D, Plant> plants;
    private int mapWidth;
    private int mapHeight;
    private final List<ObjectEventListener<Statistics>> subscribers = new LinkedList<>();

    public Statistics createSnapshot() {
        return new Statistics(this);
    }

    public void subscribeTo(WorldMap map) {
        this.animals = map.getAnimals();
        this.plants = map.getPlants();
        Boundary mapBoundary = map.getCurrentBounds();
        this.mapWidth = mapBoundary.upperRight().getX() + 1;
        this.mapHeight = mapBoundary.upperRight().getY() + 1;
        map.addEventSubscriber(this);
    }

    public void setFocusedAnimal(Animal animal) {
        this.focusedAnimal = animal;
        this.isAnimalFocused.setValue(true);
        notifySubscribers("animal set");
    }

    public void unsetFocusedAnimal() {
        this.focusedAnimal = null;
        this.isAnimalFocused.setValue(false);
        notifySubscribers("animal unset");
    }

    public void sendEventData(WorldMap map, String message) {
        if (message.equals("step")) {
            this.collectSimulationStatistics();
            this.collectAnimalStatistics();
            notifySubscribers("statistics updated");
        }
        // this message is of form "animal died %d" where %d is any integer, signaling its age
        if (message.contains("animal died")) {
            this.deadAnimalsCount++;
            this.deadAnimalsDaysLivedSum += Integer.parseInt(message.replaceAll("[^0-9]", ""));
        }
    }

    private void collectSimulationStatistics() {
        this.collectAnimalsCount();
        this.collectPlantsCount();
        this.collectFreeSquaresCount();
        this.collectMostPopularGenome();
        this.collectAverageEnergy();
        this.collectAverageDaysLived();
        this.collectAverageKidsCount();
    }

    private void collectAnimalStatistics() {
        if (this.focusedAnimal == null || !this.focusedAnimal.getAlive()) return;
        this.focusedAnimalGenome.setValue(this.focusedAnimal.getGenome());
        this.focusedAnimalActiveGene.setValue(this.focusedAnimal.getGenome().getActiveGeneIndex());
        this.focusedAnimalEnergy.setValue(this.focusedAnimal.getEnergy());
        this.focusedAnimalPlantsEaten.setValue(this.focusedAnimal.getPlantsEaten());
        this.focusedAnimalKidsCount.setValue(this.focusedAnimal.kidsCount());
        this.focusedAnimalAncestorsCount.setValue(this.focusedAnimal.ancestorsCount());
        this.focusedAnimalAge.setValue(this.focusedAnimal.getAge());
        this.focusedAnimalIsAlive.setValue(this.focusedAnimal.getAlive());
    }

    private void collectAnimalsCount() {
        this.animalsCount.setValue(this.animals.values().stream().mapToInt(List::size).sum());
    }

    private void collectPlantsCount() {
        this.plantsCount.setValue(this.plants.values().size());
    }

    private void collectFreeSquaresCount() {
        Set<Vector2D> takenFields = new HashSet<>(this.animals.keySet());
        takenFields.addAll(this.plants.keySet());
        this.freeSquaresCount.setValue(this.mapWidth * this.mapHeight - takenFields.size());
    }

    private void collectMostPopularGenome() {
        if (this.animals.isEmpty()) {
            this.mostPopularGenome.setValue(null);
            return;
        }
        // no clue how to determine which genome is most popular, as every single one is most often unique
        // thus, displaying the strongest animal's genome
        Animal strongestAnimal = AnimalFactory.defaultAnimal();
        for (List<Animal> animalsList : this.animals.values()) {
            for (Animal animal : animalsList) {
                if (Animal.compare(animal, strongestAnimal) < 0) strongestAnimal = animal;
            }
        }
        this.mostPopularGenome.setValue(strongestAnimal.getGenome());
    }

    private void collectAverageEnergy() {
        int energySum = 0;
        for (List<Animal> animalsList : this.animals.values()) {
            for (Animal animal : animalsList) energySum += animal.getEnergy();
        }
        this.averageEnergy.setValue((float) energySum / this.animalsCount.getValue());
    }

    private void collectAverageDaysLived() {
        this.averageDaysLived.setValue((float) this.deadAnimalsDaysLivedSum / this.deadAnimalsCount);
    }

    private void collectAverageKidsCount() {
        int kidsCountSum = 0;
        for (List<Animal> animalsList : this.animals.values()) {
            for (Animal animal : animalsList) kidsCountSum += animal.kidsCount();
        }
        this.averageKidsCount.setValue((float) kidsCountSum / this.animalsCount.getValue());
    }

    @Override
    public boolean isListenerSubscribed(ObjectEventListener<Statistics> listener) {
        return this.subscribers.contains(listener);
    }

    @Override
    public void notifySubscribers(String message) {
        Statistics statistics = this.createSnapshot();
        for (ObjectEventListener<Statistics> subscriber : this.subscribers) {
            subscriber.sendEventData(statistics, message);
        }
    }

    @Override
    public void addEventSubscriber(ObjectEventListener<Statistics> listener) {
        if (this.isListenerSubscribed(listener)) return;
        this.subscribers.add(listener);
    }

    @Override
    public void removeEventSubscriber(ObjectEventListener<Statistics> listener) {
        if (this.isListenerSubscribed(listener)) this.subscribers.remove(listener);
    }

    public int getAnimalsCount() {
        return animalsCount.getValue();
    }

    public int getPlantsCount() {
        return plantsCount.getValue();
    }

    public int getFreeSquaresCount() {
        return freeSquaresCount.getValue();
    }

    public Genome getMostPopularGenome() {
        return mostPopularGenome.getValue();
    }

    public float getAverageEnergy() {
        return averageEnergy.getValue();
    }

    public float getAverageDaysLived() {
        return averageDaysLived.getValue();
    }

    public float getAverageKidsCount() {
        return averageKidsCount.getValue();
    }

    public boolean getIsAnimalFocused() {
        return this.isAnimalFocused.getValue();
    }

    public Genome getFocusedAnimalGenome() {
        return focusedAnimalGenome.getValue();
    }

    public int getFocusedAnimalActiveGene() {
        return this.focusedAnimalActiveGene.getValue();
    }

    public int getFocusedAnimalEnergy() {
        return focusedAnimalEnergy.getValue();
    }

    public int getFocusedAnimalPlantsEaten() {
        return focusedAnimalPlantsEaten.getValue();
    }

    public int getFocusedAnimalKidsCount() {
        return focusedAnimalKidsCount.getValue();
    }

    public int getFocusedAnimalAncestorsCount() {
        return focusedAnimalAncestorsCount.getValue();
    }

    public int getFocusedAnimalAge() {
        return focusedAnimalAge.getValue();
    }

    public boolean getFocusedAnimalIsAlive() {
        return this.focusedAnimalIsAlive.getValue();
    }
}
