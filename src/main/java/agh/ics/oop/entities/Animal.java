package agh.ics.oop.entities;

import agh.ics.oop.model.*;
import agh.ics.oop.render.AssignRenderer;
import agh.ics.oop.render.renderer.AnimalRenderer;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@AssignRenderer(renderer = AnimalRenderer.class)
public class Animal extends WorldEntity implements EnergyHolder, Comparable<Animal> {
    // prevents updating multiple times:
    // when HashMap key (2, 2) is updated,
    // animal moves to (2, 3), and then HashMap key (2, 3) is updated,
    // it won't double move already moved Animal
    private boolean wasUpdated = false;
    private boolean isAlive = true;
    private int energy;
    private final AnimalStatistics statistics;
    private final Genome genome;

    public Animal(Vector2D position, int energy, Genome genome) {
        super(position, MapDirection.randomDirection());
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

    public void addKid(Animal kid) { this.statistics.addKid(kid); }

    public int kidsCount() { return this.statistics.countKids(); }

    public int ancestorsCount() {
        return constructAncestorsList(new LinkedList<>()).size();
    }

    public List<Animal> constructAncestorsList(List<Animal> seenAlready) {
        this.statistics.addAncestorsToList(seenAlready);
        return seenAlready;
    }

    public int getAge() { return this.statistics.age; }

    public boolean getAlive() { return this.isAlive; }

    public Genome getGenome() { return this.genome; }

    public int compareTo(@NotNull Animal other) {
        return Animal.compare(this, other);
    }

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

    public String toString() {
        return this.mapDirection.toString();
    }

    static private final Random random = new Random();

    // returns true if Animal first is stronger, false otherwise
    static public int compare(Animal first, Animal second) {
        if (first.energy > second.energy) return 1;
        if (first.energy < second.energy) return -1;
        if (first.getAge() > second.getAge()) return 1;
        if (first.getAge() < second.getAge()) return -1;
        if (first.kidsCount() > second.kidsCount()) return 1;
        if (first.kidsCount() < second.kidsCount()) return -1;
        // randomly choose between first and second
        return random.nextInt(2);
    }
}
