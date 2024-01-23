package agh.ics.oop.model;

import agh.ics.oop.Configuration;
import agh.ics.oop.entities.*;
import agh.ics.oop.util.RandomNumber;

import java.util.*;

import static agh.ics.oop.Configuration.Fields.*;

public class PoisonousPlantsWorldMap extends AbstractWorldMap implements WorldMap {
    private final RandomNumber randomFieldIndex;
    private final float poisonousFieldsFraction;
    private Vector2D poisonousFieldStartPosition;
    private Vector2D poisonousFieldEndPosition;

    public PoisonousPlantsWorldMap(String mapTitle, Configuration configuration) {
        super(mapTitle, configuration);

        this.poisonousFieldsFraction = configuration.get(SPECIAL_FIELDS_FRACTION);
        this.randomFieldIndex = new RandomNumber(0, this.mapHeight * this.mapWidth);

        this.buildFields();

        growPlants(configuration.get(STARTING_PLANTS_NUMBER));

        System.out.println("MAP OBJECT IS BUILT CORRECTLY POISONOUS PLANTS");
    }

    private void buildFields() {
        // generating poisonous square
        // the equation comes from
        // A - map area, x, y - map dimensions, A = x * y
        // B - poisonous square area, x' - square side length, B = (x')^2
        // p - fraction of poisonous plants
        // B = (x')^2 = p * A = p * x * y
        // x' = sqrt(p * x * y)
        final int poisonousFieldsSize = (int) Math.sqrt(this.mapWidth * this.mapHeight * this.poisonousFieldsFraction);
        // lower left corner
        this.poisonousFieldStartPosition = new Vector2D(
                random.nextInt(this.mapWidth - poisonousFieldsSize),
                random.nextInt(poisonousFieldsSize, this.mapHeight)
        );
        // upper right corner
        this.poisonousFieldEndPosition = this.poisonousFieldStartPosition.add(
                new Vector2D(poisonousFieldsSize, -poisonousFieldsSize)
        );

        System.out.println("POISONOUS PLANT START " + this.poisonousFieldStartPosition + " END " + this.poisonousFieldEndPosition);
    }

    protected void growPlants() { this.growPlants(this.numberOfGrowingPlants); }

    private void growPlants(int numberOfPlants) {
        for (int i = 0; i < numberOfPlants; i++) {
            if (!this.randomFieldIndex.hasNext()) return;
            int randomFieldIndex = this.randomFieldIndex.next();
            Vector2D plantPosition = new Vector2D(
                    randomFieldIndex % this.mapWidth, randomFieldIndex / this.mapWidth
            );
            this.placeElement(plantFactory.create(plantPosition, !isPoisoned(plantPosition)));
        }
    }

    protected void eatPlants() {
        for (Plant plant : this.plants.values()) {
            Vector2D position = plant.getPosition();
            if (this.animals.containsKey(position) && !this.animals.get(position).isEmpty()) {
                Animal animal = this.animals.get(position).get(0);
                animal.eat(plant);
                plants.remove(position);
                this.randomFieldIndex.restoreNumber(position.getX() + position.getY() * this.mapWidth);
            }
        }
    }

    public void moveAnimal(Animal animal) {
        this.moveAnimal(animal, false);
    }

    public void moveAnimal(Animal animal, boolean dodgedAlready) {
        if (animal.wasMoved()) return;
        Vector2D position = animal.getPosition();
        MapDirection direction = animal.getDirection();
        switch (moveType(position, direction, dodgedAlready)) {
            case REGULAR -> animal.setPosition(position.add(direction.moveOffset));
            case POLAR -> animal.rotateBy(MoveDirection.ROTATE_180);
            case LEAP_TO_LEFT -> animal.setPosition(new Vector2D(0, position.getY()));
            case LEAP_TO_RIGHT -> animal.setPosition(new Vector2D(this.mapWidth - 1, position.getY()));
            case DODGE -> {
                animal.genomeRotation();
                this.moveAnimal(animal, true);
                return;
            }
        }
        if (position != animal.getPosition()) {
            animals.get(position).remove(animal);
            placeElement(animal);
        }
        animal.move();
    }

    public NextMoveType moveType(Vector2D position, MapDirection direction, boolean dodgedAlready) {
        // move would cause to eat a poisonous plant
        Vector2D newPosition = position.add(direction.moveOffset);
        if (!dodgedAlready && this.plants.containsKey(newPosition) && isPoisoned(newPosition))
            return NextMoveType.DODGE;

        return super.moveType(position, direction);
    }

    private boolean isPoisoned(Vector2D position) {
        return this.poisonousFieldStartPosition.getX() <= position.getX() &&
                position.getX() <= this.poisonousFieldEndPosition.getX() &&
                this.poisonousFieldEndPosition.getY() <= position.getY() &&
                position.getY() <= this.poisonousFieldStartPosition.getY();
    }

    @Override
    protected Ground groundAtPosition(Vector2D position) {
        return new Ground(position, this.isPoisoned(position));
    }

    private final static Random random = new Random();
}
