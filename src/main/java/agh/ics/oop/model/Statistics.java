package agh.ics.oop.model;

import agh.ics.oop.resource.Exported;

public class Statistics {
    @Exported
    public final int animalsCount;
    @Exported
    public final int plantsCount;
    @Exported
    public final int freeSquaresCount;
    @Exported
    public final String mostPopularGenome;
    @Exported
    public final float averageEnergy;
    @Exported
    public final float averageDaysLived;
    @Exported
    public final float averageKidsCount;
    @Exported
    public final String focusedAnimalGenome;
    @Exported
    public final int focusedAnimalActiveGene;
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
    private final boolean isFocusedAnimalAlive;

    public Statistics(StatisticsCollector collector) {
        this.animalsCount = collector.getAnimalsCount();
        this.plantsCount = collector.getPlantsCount();
        this.freeSquaresCount = collector.getFreeSquaresCount();
        this.mostPopularGenome = collector.getMostPopularGenome().toOrdinalString();
        this.averageEnergy = collector.getAverageEnergy();
        this.averageDaysLived = collector.getAverageDaysLived();
        this.averageKidsCount = collector.getAverageKidsCount();

        boolean isAnimalFocused = collector.getIsAnimalFocused();
        if (isAnimalFocused) {
            this.isAnimalFocused = true;
            this.focusedAnimalGenome = collector.getFocusedAnimalGenome().toOrdinalString();
            this.focusedAnimalActiveGene = collector.getFocusedAnimalActiveGene();
            this.focusedAnimalEnergy = collector.getFocusedAnimalEnergy();
            this.focusedAnimalPlantsEaten = collector.getFocusedAnimalPlantsEaten();
            this.focusedAnimalKidsCount = collector.getFocusedAnimalKidsCount();
            this.focusedAnimalAncestorsCount = collector.getFocusedAnimalAncestorsCount();
            this.focusedAnimalAge = collector.getFocusedAnimalAge();
            this.isFocusedAnimalAlive = collector.getFocusedAnimalIsAlive();
        } else {
            this.isAnimalFocused = false;
            this.focusedAnimalGenome = null;
            this.focusedAnimalActiveGene = 0;
            this.focusedAnimalEnergy = 0;
            this.focusedAnimalPlantsEaten = 0;
            this.focusedAnimalKidsCount = 0;
            this.focusedAnimalAncestorsCount = 0;
            this.focusedAnimalAge = 0;
            this.isFocusedAnimalAlive = false;
        }
    }

    public String toString() {
        String mapData = String.format("""
                        animals: %d
                        plants: %d
                        free squares: %d
                        pop genome: \n%s
                        avg energy: %.2f
                        avg days lived: %.2f
                        avg kids: %.2f""",
                this.animalsCount, this.plantsCount, this.freeSquaresCount, this.mostPopularGenome,
                this.averageEnergy, this.averageDaysLived, this.averageKidsCount);
        String animalData = this.isAnimalFocused ? String.format("""
                        \n========
                        animal genome: \n%s
                        act gene idx: %d
                        energy: %d
                        plants eaten: %d
                        kids: %d
                        ancestors: %d
                        days %s""",
                this.focusedAnimalGenome, this.focusedAnimalActiveGene,
                this.focusedAnimalEnergy, this.focusedAnimalPlantsEaten, this.focusedAnimalKidsCount,
                this.focusedAnimalAncestorsCount,
                (this.isFocusedAnimalAlive ? "alive: " : "lived: ") + this.focusedAnimalAge)
                : "\n========\nNO ANIMAL SELECTED";
        return mapData + animalData + "\n";
    }
}
