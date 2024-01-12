package agh.ics.oop.entities;

import agh.ics.oop.model.*;
import agh.ics.oop.render.AssignRenderer;
import agh.ics.oop.render.renderer.AnimalRenderer;

import java.util.Objects;
import java.util.Random;

@AssignRenderer(renderer = AnimalRenderer.class)
public class Animal extends WorldEntity implements EnergyHolder {
    // prevents updating multiple times:
    // when HashMap key (2, 2) is updated,
    // animal moves to (2, 3), and then HashMap key (2, 3) is updated,
    // it won't double move already moved Animal
    private boolean wasUpdated = false;
    private boolean isAlive = true;
    private final int minReproduceEnergy;
    private final int energyPassed;
    private int energy;
    private final AnimalStatistics statistics;
    private final Genome genome;

    public Animal(Vector2D position, int minReproduceEnergy, int energyPassed, int energy, Genome genome) {
        super(position, MapDirection.randomDirection());
        this.minReproduceEnergy = minReproduceEnergy;
        this.energyPassed = energyPassed;
        this.energy = energy;
        this.statistics = new AnimalStatistics();
        this.genome = genome;
    }

    public void update() {
        if (this.wasUpdated || !this.isAlive) return;
        this.wasUpdated = true;
        this.statistics.addAge();
        this.drainEnergy(1);
        this.rotateBy(genome.getActiveGene());
        this.genome.updateActiveGene();
    }

    public void refreshUpdateStatus() { wasUpdated = false; }

    public void eat(EnergyHolder energyHolder) {
        this.supplyEnergy(energyHolder, energyHolder.getEnergy());
        this.statistics.addEaten();
    }

    @Override
    public int drainEnergy(int energy) {
        int drainedEnergy = Math.min(energy, this.energy);
        this.energy -= drainedEnergy;
        if (this.energy == 0) this.isAlive = false;
        return drainedEnergy;
    }

    @Override
    public int supplyEnergy(EnergyHolder energyHolder, int energy) {
        int gainedEnergy = energyHolder.drainEnergy(energy);
        this.energy += gainedEnergy;
        return gainedEnergy;
    }

    @Override
    public int getEnergy() { return this.energy; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return isAlive == animal.isAlive && energy == animal.energy && Objects.equals(getPosition(), animal.getPosition()) && getDirection() == animal.getDirection() && Objects.equals(genome, animal.genome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosition(), getDirection(), isAlive, energy, genome);
    }

    static private final Random random = new Random();
}
