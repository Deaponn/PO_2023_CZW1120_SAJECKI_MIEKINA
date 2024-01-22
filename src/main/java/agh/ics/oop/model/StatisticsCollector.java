package agh.ics.oop.model;

import agh.ics.oop.entities.Animal;
import agh.ics.oop.entities.AnimalFactory;
import agh.ics.oop.entities.Plant;
import agh.ics.oop.resource.Exported;

import java.util.*;

public class StatisticsCollector implements MapChangeListener {
    @Exported
    private int animalsCount;
    @Exported
    private int plantsCount;
    @Exported
    private int freeSquaresCount;
    @Exported
    private Genome mostPopularGenome;
    @Exported
    private float averageEnergy;
    @Exported
    private float averageDaysLived;
    @Exported
    private float averageKidsCount;
    private int deadAnimalsCount;
    private int deadAnimalsDaysLivedSum;
    private Animal focusedAnimal = null;
    @Exported
    private Genome focusedAnimalGenome;
    @Exported
    private int focusedAnimalEnergy;
    @Exported
    private int focusedAnimalPlantsEaten;
    @Exported
    private int focusedAnimalKidsCount;
    @Exported
    private int focusedAnimalAncestorsCount;
    @Exported
    private int focusedAnimalAge;
    private Map<Vector2D, List<Animal>> animals;
    private Map<Vector2D, Plant> plants;
    private int mapWidth;
    private int mapHeight;

    public void mapChanged(WorldMap map, String message) {
        if (message.equals("step")) {
            this.animals = map.getAnimals();
            this.plants = map.getPlants();
            Boundary mapBoundary = map.getCurrentBounds();
            this.mapWidth = mapBoundary.upperRight().getX() + 1;
            this.mapHeight = mapBoundary.upperRight().getY() + 1;
            this.collectSimulationStatistics();
            this.collectAnimalStatistics();
            this.printStatistics();
        }
        // this message is of form "animal died %d" where %d is any integer, signaling its age
        if (message.contains("animal died")) {
            this.deadAnimalsCount++;
            this.deadAnimalsDaysLivedSum += Integer.parseInt(message.replaceAll("[^0-9]", ""));
        }
    }

    public void setFocusedAnimal(Animal animal) {
        this.focusedAnimal = animal;
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

    private void printStatistics() {
        System.out.printf("""
                        Animals count: %d, plants count: %d, free squares: %d,
                        most popular genome: %s,
                        average energy: %.2f, average days lived: %.2f, average kids count: %.2f
                        %n""", this.animalsCount, this.plantsCount, this.freeSquaresCount, this.mostPopularGenome,
                this.averageEnergy, this.averageDaysLived, this.averageKidsCount);
        if (this.focusedAnimal != null)
            System.out.printf("""
                        Current animal genome: %s,
                        active genome idx: %d, energy: %d,
                        plants eaten: %d, kids count: %d
                        ancestors count: %d, days %s
                        %n""", this.focusedAnimalGenome, this.focusedAnimalGenome.getActiveGeneIndex(),
                this.focusedAnimalEnergy, this.focusedAnimalPlantsEaten, this.focusedAnimalKidsCount,
                this.focusedAnimalAncestorsCount,
                (this.focusedAnimal.getAlive() ? "alive: " : "lived: ") + this.focusedAnimalAge);
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
        this.focusedAnimal = strongestAnimal;
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
}
