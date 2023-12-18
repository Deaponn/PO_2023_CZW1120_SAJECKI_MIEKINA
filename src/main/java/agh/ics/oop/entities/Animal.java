package agh.ics.oop.entities;

import agh.ics.oop.Configuration;
import agh.ics.oop.model.*;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Animal implements WorldElement, WorldEntity {
    private Vector2D position;
    private MapDirection direction;
    private int activeGene;
    private int energy;
    private int age = 0;
    private int kidsCount = 0;
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

    public void move(MoveValidator validator) {
        rotateBy(genome.get(activeGene));
        switch (validator.moveType(position)) {
            case REGULAR -> position = position.add(direction.getMoveOffset());
            case POLAR -> direction = direction.rotateBy(MoveDirection.ROTATE_180);
            case LEAP_TO_OTHER_SIDE -> {
                if (position.getX() > 0) {
                    position = new Vector2D(0, position.getY());
                } else {
                    position = new Vector2D(configuration.mapWidth() - 1, position.getY());
                }
            }
        }
        updateActiveGene();
    }

    public void eat() {
        energy += configuration.plantEnergy();
    }

    public void age() { age++; }

    public void decreaseEnergy() {
        energy--;
        if (energy == 0) {
            // TODO: handle death
        }
    }

    public void rotateBy(MoveDirection directionChange) {
        direction = direction.rotateBy(directionChange);
    }

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
}
