package agh.ics.oop.entities;

import agh.ics.oop.Configuration;
import agh.ics.oop.model.*;
import agh.ics.oop.render.AssignRenderer;
import agh.ics.oop.render.renderer.AnimalRenderer;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import static agh.ics.oop.Configuration.Fields.*;

@AssignRenderer(renderer = AnimalRenderer.class)
public class Animal implements WorldElement, WorldEntity {
    private Vector2D position;
    private MapDirection direction;
    // prevents updating multiple times:
    // when HashMap key (2, 2) is updated,
    // animal moves to (2, 3), and then HashMap key (2, 3) is updated,
    // it won't double move already moved Animal
    private boolean wasUpdated = false;
    private boolean isAlive = true;
    private int activeGene;
    private int energy;
    private int age = 0;
    private int kidsCount = 0;
    private int ancestorsCount = 0;
    private int plantsEaten = 0;
    private final List<MoveDirection> genome;
    private final int addEnergy;
    private final float randomGenomeChangeChance;
    private final int genomeLength;

    private final Random random = new Random();

    public Animal(Vector2D initialPosition, int initialEnergy, List<MoveDirection> genome, Configuration configuration) {
        this.energy = initialEnergy;
        this.position = initialPosition;
        this.direction = MapDirection.randomDirection();
        this.genomeLength = configuration.get(GENOME_LENGTH);
        this.activeGene = random.nextInt(this.genomeLength);
        this.addEnergy = configuration.get(PLANT_ENERGY);
        this.randomGenomeChangeChance = configuration.get(RANDOM_GENOME_CHANGE_CHANCE);
        this.genome = genome;
    }

    public void update() {
        if (wasUpdated || !isAlive) return;
        wasUpdated = true;
        if (energy == 0) {
            isAlive = false;
            return;
        }
        age++;
        energy--;
        rotateBy(genome.get(activeGene));
        updateActiveGene();
    }

    public void refreshUpdateStatus() { wasUpdated = false; }

    public void eat() {
        energy += this.addEnergy;
        plantsEaten++;
    }

    public void rotateBy(MoveDirection directionChange) {
        direction = direction.rotateBy(directionChange);
    }

    public void setPosition(Vector2D newPosition) { position = newPosition; }

    public Vector2D getPosition() { return position; }

    public MapDirection getDirection() { return direction; }

    public int getEnergy() {
        return this.energy;
    }

    public boolean isAtPosition(Vector2D other) { return Objects.equals(position, other); }

    private void updateActiveGene() {
        if (random.nextDouble() < this.randomGenomeChangeChance) {
            activeGene = random.nextInt(this.genomeLength);
        } else {
            activeGene = (activeGene + 1) % this.genomeLength;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return isAlive == animal.isAlive && activeGene == animal.activeGene && energy == animal.energy && age == animal.age && kidsCount == animal.kidsCount && ancestorsCount == animal.ancestorsCount && plantsEaten == animal.plantsEaten && Objects.equals(getPosition(), animal.getPosition()) && getDirection() == animal.getDirection() && Objects.equals(genome, animal.genome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosition(), getDirection(), isAlive, activeGene, energy, age, kidsCount, ancestorsCount, plantsEaten, genome);
    }
}
