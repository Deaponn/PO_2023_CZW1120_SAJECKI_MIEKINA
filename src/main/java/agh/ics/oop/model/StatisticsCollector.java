package agh.ics.oop.model;

import agh.ics.oop.entities.Animal;
import agh.ics.oop.entities.AnimalFactory;
import agh.ics.oop.entities.Plant;

import java.util.*;

public class StatisticsCollector implements ObjectEventListener<WorldMap>, ObjectEventEmitter<Statistics> {
    private int animalsCount;
    private int plantsCount;
    private int freeSquaresCount;
    private Genome mostPopularGenome;
    private float averageEnergy;
    private float averageDaysLived;
    private float averageKidsCount;
    private int deadAnimalsCount;
    private int deadAnimalsDaysLivedSum;
    private Animal focusedAnimal = null;
    private Genome focusedAnimalGenome;
    private int focusedAnimalEnergy;
    private int focusedAnimalPlantsEaten;
    private int focusedAnimalKidsCount;
    private int focusedAnimalAncestorsCount;
    private int focusedAnimalAge;
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
        this.focusedAnimalGenome = this.focusedAnimal.getGenome();
        this.focusedAnimalEnergy = this.focusedAnimal.getEnergy();
        this.focusedAnimalPlantsEaten = this.focusedAnimal.getPlantsEaten();
        this.focusedAnimalKidsCount = this.focusedAnimal.kidsCount();
        this.focusedAnimalAncestorsCount = this.focusedAnimal.ancestorsCount();
        this.focusedAnimalAge = this.focusedAnimal.getAge();
    }

    private void collectAnimalsCount() {
        this.animalsCount = this.animals.values().stream().mapToInt(List::size).sum();
    }

    private void collectPlantsCount() {
        this.plantsCount = this.plants.values().size();
    }

    private void collectFreeSquaresCount() {
        Set<Vector2D> takenFields = new HashSet<>(this.animals.keySet());
        takenFields.addAll(this.plants.keySet());
        this.freeSquaresCount = this.mapWidth * this.mapHeight - takenFields.size();
    }

    private void collectMostPopularGenome() {
        if (this.animals.isEmpty()) {
            this.mostPopularGenome = null;
            return;
        }
        // no clue how to determine which genome is most popular, as every single one is most often unique
        // thus, displaying the strongest animal's genome
        Animal strongestAnimal = AnimalFactory.defaultAnimal();
        for (List<Animal> animalsList : this.animals.values()) {
            for (Animal animal : animalsList) {
                if (Animal.compare(animal, strongestAnimal) > 0) strongestAnimal = animal;
            }
        }
        this.mostPopularGenome = strongestAnimal.getGenome();
        // TODO: SUPER IMPORTANT TO REMOVE, THIS IS ONLY TO TEST FOCUSED ANIMAL TRACKING
        setFocusedAnimal(strongestAnimal);
    }

    private void collectAverageEnergy() {
        int energySum = 0;
        for (List<Animal> animalsList : this.animals.values()) {
            for (Animal animal : animalsList) energySum += animal.getEnergy();
        }
        this.averageEnergy = (float) energySum / this.animalsCount;
    }

    private void collectAverageDaysLived() {
        this.averageDaysLived = (float) this.deadAnimalsDaysLivedSum / this.deadAnimalsCount;
    }

    private void collectAverageKidsCount() {
        int kidsCountSum = 0;
        for (List<Animal> animalsList : this.animals.values()) {
            for (Animal animal : animalsList) kidsCountSum += animal.kidsCount();
        }
        this.averageKidsCount = (float) kidsCountSum / this.animalsCount;
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
        return animalsCount;
    }

    public int getPlantsCount() {
        return plantsCount;
    }

    public int getFreeSquaresCount() {
        return freeSquaresCount;
    }

    public Genome getMostPopularGenome() {
        return mostPopularGenome;
    }

    public float getAverageEnergy() {
        return averageEnergy;
    }

    public float getAverageDaysLived() {
        return averageDaysLived;
    }

    public float getAverageKidsCount() {
        return averageKidsCount;
    }

    public Animal getFocusedAnimal() {
        return focusedAnimal;
    }

    public Genome getFocusedAnimalGenome() {
        return focusedAnimalGenome;
    }

    public int getFocusedAnimalEnergy() {
        return focusedAnimalEnergy;
    }

    public int getFocusedAnimalPlantsEaten() {
        return focusedAnimalPlantsEaten;
    }

    public int getFocusedAnimalKidsCount() {
        return focusedAnimalKidsCount;
    }

    public int getFocusedAnimalAncestorsCount() {
        return focusedAnimalAncestorsCount;
    }

    public int getFocusedAnimalAge() {
        return focusedAnimalAge;
    }
}
