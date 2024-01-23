package agh.ics.oop.model;

import agh.ics.oop.entities.Animal;
import agh.ics.oop.resource.Exported;

public class Statistics {
    @Exported
    public final int animalsCount;
    @Exported
    public final int plantsCount;
    @Exported
    public final int freeSquaresCount;
    @Exported
    public final Genome mostPopularGenome;
    @Exported
    public final float averageEnergy;
    @Exported
    public final float averageDaysLived;
    @Exported
    public final float averageKidsCount;
    @Exported
    public final Genome focusedAnimalGenome;
    @Exported
    public final int focusedAnimalEnergy;
    @Exported
    public final int focusedAnimalPlantsEaten;
    @Exported
    public final int focusedAnimalKidsCount;
    @Exported
    public final int focusedAnimalAncestorsCount;
    @Exported
    public final int focusedAnimalAge;
    private final boolean isAnimalFocused;
    private boolean isFocusedAnimalAlive;

    public Statistics(StatisticsCollector collector) {
        this.animalsCount = collector.getAnimalsCount();
        this.plantsCount = collector.getPlantsCount();
        this.freeSquaresCount = collector.getFreeSquaresCount();
        this.mostPopularGenome = collector.getMostPopularGenome();
        this.averageEnergy = collector.getAverageEnergy();
        this.averageDaysLived = collector.getAverageDaysLived();
        this.averageKidsCount = collector.getAverageKidsCount();

        Animal focusedAnimal = collector.getFocusedAnimal();
        if (focusedAnimal != null) {
            this.isAnimalFocused = true;
            this.isFocusedAnimalAlive = focusedAnimal.getAlive();
            this.focusedAnimalGenome = collector.getFocusedAnimalGenome();
            this.focusedAnimalEnergy = collector.getFocusedAnimalEnergy();
            this.focusedAnimalPlantsEaten = collector.getFocusedAnimalPlantsEaten();
            this.focusedAnimalKidsCount = collector.getFocusedAnimalKidsCount();
            this.focusedAnimalAncestorsCount = collector.getFocusedAnimalAncestorsCount();
            this.focusedAnimalAge = collector.getFocusedAnimalAge();
        } else {
            this.isAnimalFocused = false;
            this.focusedAnimalGenome = GenomeFactory.defaultGenome();
            this.focusedAnimalEnergy = 0;
            this.focusedAnimalPlantsEaten = 0;
            this.focusedAnimalKidsCount = 0;
            this.focusedAnimalAncestorsCount = 0;
            this.focusedAnimalAge = 0;
        }
    }

    public String toString() {
        String mapData = String.format("""
                        Animals count: %d, plants count: %d, free squares: %d,
                        most popular genome: %s,
                        average energy: %.2f, average days lived: %.2f, average kids count: %.2f""",
                this.animalsCount, this.plantsCount, this.freeSquaresCount, this.mostPopularGenome,
                this.averageEnergy, this.averageDaysLived, this.averageKidsCount);
        String animalData = this.isAnimalFocused ? String.format("""
                        %nCurrent animal genome: %s,
                        active gene idx: %d, energy: %d,
                        plants eaten: %d, kids count: %d
                        ancestors count: %d, days %s""",
                this.focusedAnimalGenome, this.focusedAnimalGenome.getActiveGeneIndex(),
                this.focusedAnimalEnergy, this.focusedAnimalPlantsEaten, this.focusedAnimalKidsCount,
                this.focusedAnimalAncestorsCount,
                (this.isFocusedAnimalAlive ? "alive: " : "lived: ") + this.focusedAnimalAge)
                : "No animal is focused right now";
        return mapData + animalData + "\n";
    }
}
