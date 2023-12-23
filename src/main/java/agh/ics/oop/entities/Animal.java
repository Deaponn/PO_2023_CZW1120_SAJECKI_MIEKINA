package agh.ics.oop.entities;

import agh.ics.oop.Configuration;
import agh.ics.oop.model.*;

import java.util.List;
import java.util.Objects;
import java.util.Random;

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
    private final Configuration configuration;
    private final Random random = new Random();

    public Animal(Vector2D initialPosition, int initialEnergy, List<MoveDirection> genome, Configuration configuration) {
        energy = initialEnergy;
        position = initialPosition;
        direction = MapDirection.randomDirection();
        activeGene = random.nextInt(configuration.genomeLength());
        this.genome = genome;
        this.configuration = configuration;
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
        energy += configuration.plantEnergy();
        plantsEaten++;
    }

    public void rotateBy(MoveDirection directionChange) {
        direction = direction.rotateBy(directionChange);
    }

    public void setPosition(Vector2D newPosition) { position = newPosition; }

    public Vector2D getPosition() { return position; }

    public MapDirection getDirection() { return direction; }

    public boolean isAtPosition(Vector2D other) { return Objects.equals(position, other); }

    private void updateActiveGene() {
        if (random.nextDouble() < configuration.randomGenomeChangeChance()) {
            activeGene = random.nextInt(configuration.genomeLength());
        } else {
            activeGene = (activeGene + 1) % configuration.genomeLength();
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
