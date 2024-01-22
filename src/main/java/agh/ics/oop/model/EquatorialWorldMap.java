package agh.ics.oop.model;

import agh.ics.oop.Configuration;
import agh.ics.oop.entities.*;
import agh.ics.oop.util.RandomNumber;

import java.util.*;

import static agh.ics.oop.Configuration.Fields.*;

public class EquatorialWorldMap extends AbstractWorldMap implements WorldMap {
    private final List<Vector2D> equator;
    private final RandomNumber randomEquatorIndex;
    private final List<Vector2D> regularField;
    private final RandomNumber randomRegularFieldIndex;
    private final float equatorFieldsFraction;
    private final float plantGrowAtEquatorChance;

    public EquatorialWorldMap(Configuration configuration) {
        super(configuration);

        this.equatorFieldsFraction = configuration.get(SPECIAL_FIELDS_FRACTION);
        this.plantGrowAtEquatorChance = configuration.get(PLANT_GROW_AT_EQUATOR_CHANCE);

        List<List<Vector2D>> fields = buildFields();
        equator = fields.get(0);
        regularField = fields.get(1);
        randomEquatorIndex = new RandomNumber(0, equator.size());
        randomRegularFieldIndex = new RandomNumber(0, regularField.size());

        growPlants(configuration.get(STARTING_PLANTS_NUMBER));

        System.out.println("MAP OBJECT IS BUILT CORRECTLY REGULAR MAP");
    }

    private List<List<Vector2D>> buildFields() {
        boolean[][] isEquator = new boolean[this.mapHeight][this.mapWidth];

        // generating equator
        // it will be at least two rows high, with every row populated randomly
        final int equatorSize = (int) (this.mapWidth * this.mapHeight * this.equatorFieldsFraction);
        final int middleRow = this.mapHeight / 2;
        int topRowOffset = -1;
        int bottomRowOffset = 0;

        // returns numbers in range [a, b) with no duplicates
        RandomNumber topRowRandom = new RandomNumber(0, this.mapWidth);
        RandomNumber bottomRowRandom = new RandomNumber(0, this.mapWidth);
        for (int i = 0; i < equatorSize; i += 2) {
            // both rows' free spaces for the equator end at the same time
            // refreshing random numbers, and marching one row further into the map
            if (!topRowRandom.hasNext()) {
                topRowRandom.refreshRange();
                bottomRowRandom.refreshRange();
                topRowOffset--;
                bottomRowOffset++;
            }
            isEquator[middleRow + topRowOffset][topRowRandom.next()] = true;
            isEquator[middleRow + bottomRowOffset][bottomRowRandom.next()] = true;
        }

        // generate lists of vectors, they will be used to choose position for the plant
        List<Vector2D> equator = new ArrayList<>(equatorSize);
        List<Vector2D> regularField = new ArrayList<>(this.mapWidth * this.mapHeight - equatorSize);
        for (int y = 0; y < this.mapHeight; y++) {
            for (int x = 0; x < this.mapWidth; x++) {
                if (isEquator[y][x]) equator.add(new Vector2D(x, y));
                else regularField.add(new Vector2D(x, y));
            }
        }

        return List.of(equator, regularField);
    }

    protected void growPlants() { this.growPlants(this.numberOfGrowingPlants); }

    private void growPlants(int numberOfPlants) {
        for (int i = 0; i < numberOfPlants; i++) {
            Vector2D plantPosition = null;
            if (random.nextDouble() < this.plantGrowAtEquatorChance && randomEquatorIndex.hasNext())
                plantPosition = equator.get(randomEquatorIndex.next());
            else if (randomRegularFieldIndex.hasNext()) plantPosition = regularField.get(randomRegularFieldIndex.next());
            if (plantPosition != null) this.placeElement(plantFactory.create(plantPosition));
        }
    }

    protected void eatPlants() {
        for (Plant plant : this.plants.values()) {
            Vector2D position = plant.getPosition();
            if (this.animals.containsKey(position) && !this.animals.get(position).isEmpty()) {
                Animal animal = this.animals.get(position).get(0);
                animal.eat(plant);
                plants.remove(position);
                int equatorIndex = equator.indexOf(position);
                if (equatorIndex != -1) randomEquatorIndex.restoreNumber(equatorIndex);
                else randomRegularFieldIndex.restoreNumber(regularField.indexOf(position));
            }
        }
    }

    public void moveAnimal(Animal animal) {
        if (animal.wasMoved()) return;
        animal.move();
        Vector2D position = animal.getPosition();
        MapDirection direction = animal.getDirection();
        switch (moveType(position, direction)) {
            case REGULAR -> animal.setPosition(position.add(direction.moveOffset));
            case POLAR -> animal.rotateBy(MoveDirection.ROTATE_180);
            case LEAP_TO_LEFT -> animal.setPosition(new Vector2D(0, position.getY()));
            case LEAP_TO_RIGHT -> animal.setPosition(new Vector2D(this.mapWidth - 1, position.getY()));
        }
        if (position != animal.getPosition()) {
            animals.get(position).remove(animal);
            placeElement(animal);
        }
    }

    private final static Random random = new Random();
}
