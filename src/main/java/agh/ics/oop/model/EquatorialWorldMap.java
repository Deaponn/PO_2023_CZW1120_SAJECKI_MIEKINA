package agh.ics.oop.model;

import agh.ics.oop.Configuration;
import agh.ics.oop.entities.Animal;
import agh.ics.oop.entities.Ground;
import agh.ics.oop.entities.Plant;
import agh.ics.oop.util.RandomNumber;

import java.util.*;

import static agh.ics.oop.Configuration.Fields.*;

public class EquatorialWorldMap implements WorldMap {
    private final Configuration configuration;
    private final Map<Vector2D, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2D, Plant> plants = new HashMap<>();
    private final List<Vector2D> equator;
    private final List<Vector2D> regularField;
    private final int mapWidth;
    private final int mapHeight;
    private final float equatorSize;
    private final List<MapChangeListener> subscribers = new LinkedList<>();
    private final UUID mapUUID = UUID.randomUUID();

    public EquatorialWorldMap(Configuration configuration) {
        this.configuration = configuration;

        this.mapWidth = (int) configuration.get(MAP_WIDTH);
        this.mapHeight = (int) configuration.get(MAP_HEIGHT);
        this.equatorSize = (float) configuration.get(EQUATOR_SIZE);

        boolean[][] isEquator = new boolean[this.mapHeight][this.mapWidth];

        // TODO: this is hardcoded for equator, poisonous plants lack implementation
        //  probably can rename equator to "specialField' or something like that to generalize the names
        // generating equator
        // it will be at least two rows high, with every row populated randomly
        final int equatorSize = (int) (this.mapWidth * this.mapHeight * this.equatorSize);
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
        equator = new ArrayList<>(equatorSize);
        regularField = new ArrayList<>(this.mapWidth * this.mapHeight - equatorSize);
        for (int y = 0; y < this.mapHeight; y++) {
            for (int x = 0; x < this.mapWidth; x++) {
                if (isEquator[y][x]) equator.add(new Vector2D(x, y));
                else regularField.add(new Vector2D(x, y));
            }
        }

        // code to test the above solution
        // TODO: remove when development is finished
        final StringBuilder sb = new StringBuilder("|");
        for (int y = 0; y < this.mapHeight; y++) {
            for (int x = 0; x < this.mapWidth; x++) {
                if (isEquator[y][x]) sb.append("|*");
                else sb.append("| ");
            }
            sb.append("||\n|");
        }
        System.out.println(sb);
    }

    // below function assumes that Plant positions are always unique
    @Override
    public void placeElement(WorldElement worldElement) {
        if (worldElement instanceof Plant plant) {
            plants.put(worldElement.getPosition(), plant);
        }

        if (worldElement instanceof Animal animal) {
            if (animals.containsKey(animal.getPosition())) {
                animals.get(animal.getPosition()).add(animal);
            } else {
                List<Animal> list = new ArrayList<>();
                list.add(animal);
                animals.put(animal.getPosition(), list);
            }
        }

        this.mapChangeNotify("place");
    }

    @Override
    public void moveAnimal(Animal animal) {
        Vector2D position = animal.getPosition();
        MapDirection direction = animal.getDirection();
        switch (moveType(position, direction)) {
            case REGULAR -> animal.setPosition(position.add(direction.moveOffset));
            case POLAR -> animal.rotateBy(MoveDirection.ROTATE_180);
            case LEAP_TO_OTHER_SIDE -> {
                if (position.getX() > 0) {
                    animal.setPosition(new Vector2D(0, position.getY()));
                } else {
                    animal.setPosition(new Vector2D(this.mapWidth - 1, position.getY()));
                }
            }
        }
        if (position != animal.getPosition()) {
            animals.get(position).remove(animal);
            placeElement(animal);
        }
    }

    // is it necessary? there is no scenario when a move could be illegal
    @Override
    public boolean isOccupied(Vector2D position) {
        return false;
    }

    // is this necessary? should it return Plant, or List<Animal>?
    // left unimplemented since I don't see usage
    @Override
    public List<WorldElement> getElements(Vector2D position) {
        List<WorldElement> elementList = new LinkedList<>();
        elementList.add(new Ground(position));
        Plant plant = this.plants.get(position);
        if (plant != null) elementList.add(plant);
        List<Animal> animalList = this.animals.get(position);
        if (animalList != null) elementList.addAll(animalList);
        return elementList;
    }

    @Override
    public Boundary getCurrentBounds() {
        return Boundary.fromSize(this.mapWidth, this.mapHeight);
    }

    @Override
    public UUID getID() {
        return mapUUID;
    }

    @Override
    public void mapChangeSubscribe(MapChangeListener listener) {
        subscribers.add(listener);
    }

    @Override
    public void mapChangeUnsubscribe(MapChangeListener listener) {
        subscribers.remove(listener);
    }

    @Override
    public void mapChangeNotify(String message) {
        for (MapChangeListener subscriber : subscribers) {
            subscriber.mapChanged(this, message);
        }
    }

    @Override
    public boolean mapChangeIsSubscribed(MapChangeListener listener) {
        return subscribers.contains(listener);
    }

    @Override
    public NextMoveType moveType(Vector2D position, MapDirection direction) {
        // move would cause Y to go out of bounds
        Vector2D offset = direction.moveOffset;
        if ((position.getY() == this.mapHeight && offset.getY() == 1) ||
                (position.getY() == 0 && offset.getY() == -1)
            ) {
            return NextMoveType.POLAR;
        }
        // move would cause X to go out of bounds
        if ((position.getX() == this.mapWidth && offset.getX() == 1) ||
                (position.getX() == 0 && offset.getX() == -1)) {
            return NextMoveType.LEAP_TO_OTHER_SIDE;
        }
        return NextMoveType.REGULAR;
    }
}
