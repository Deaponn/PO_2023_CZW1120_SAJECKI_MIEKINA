package agh.ics.oop.model;

import agh.ics.oop.Configuration;
import agh.ics.oop.entities.Animal;
import agh.ics.oop.entities.Plant;
import agh.ics.oop.util.RandomNumber;

import java.util.*;

public class EquatorialWorldMap implements WorldMap {
    private final Configuration configuration;
    private final Map<Vector2D, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2D, Plant> plants = new HashMap<>();
    private final List<Vector2D> equator;
    private final List<Vector2D> regularField;
    private final List<MapChangeListener> subscribers = new LinkedList<>();
    private final UUID mapUUID = UUID.randomUUID();

    public EquatorialWorldMap(Configuration configuration) {
        this.configuration = configuration;

        boolean[][] isEquator = new boolean[configuration.mapHeight()][configuration.mapWidth()];

        // TODO: this is hardcoded for equator, poisonous plants lack implementation
        //  probably can rename equator to "specialField' or something like that to generalize the names
        // generating equator
        // it will be at least two rows high, with every row populated randomly
        final int equatorSize = (int) (configuration.mapWidth() * configuration.mapHeight() * configuration.equatorSize());
        final int middleRow = configuration.mapHeight() / 2;
        int topRowOffset = -1;
        int bottomRowOffset = 0;
        // returns numbers in range [a, b) with no duplicates
        RandomNumber topRowRandom = new RandomNumber(0, configuration.mapWidth());
        RandomNumber bottomRowRandom = new RandomNumber(0, configuration.mapWidth());
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
        regularField = new ArrayList<>(configuration.mapWidth() * configuration.mapHeight() - equatorSize);
        for (int y = 0; y < configuration.mapHeight(); y++) {
            for (int x = 0; x < configuration.mapWidth(); x++) {
                if (isEquator[y][x]) equator.add(new Vector2D(x, y));
                else regularField.add(new Vector2D(x, y));
            }
        }

        // code to test the above solution
        // TODO: remove when development is finished
        final StringBuilder sb = new StringBuilder("|");
        for (int y = 0; y < configuration.mapHeight(); y++) {
            for (int x = 0; x < configuration.mapWidth(); x++) {
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
            return;
        }
        Animal animal = (Animal) worldElement;
        if (animals.containsKey(animal.getPosition())) {
            animals.get(animal.getPosition()).add(animal);
        } else {
            List<Animal> list = new ArrayList<>();
            list.add(animal);
            animals.put(animal.getPosition(), list);
        }
    }

    @Override
    public void moveAnimal(Animal animal) {
        Vector2D position = animal.getPosition();
        MapDirection direction = animal.getDirection();
        switch (moveType(position, direction)) {
            case REGULAR -> animal.setPosition(position.add(direction.getMoveOffset()));
            case POLAR -> animal.rotateBy(MoveDirection.ROTATE_180);
            case LEAP_TO_OTHER_SIDE -> {
                if (position.getX() > 0) {
                    animal.setPosition(new Vector2D(0, position.getY()));
                } else {
                    animal.setPosition(new Vector2D(configuration.mapWidth() - 1, position.getY()));
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
    public WorldElement getElement(Vector2D position) {
        return null;
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(new Vector2D(0, 0), new Vector2D(configuration.mapWidth(), configuration.mapHeight()));
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
        if ((position.getY() == configuration.mapHeight() && direction.getMoveOffset().getY() == 1) ||
                (position.getY() == 0 && direction.getMoveOffset().getY() == -1)
            ) {
            return NextMoveType.POLAR;
        }
        // move would cause X to go out of bounds
        if ((position.getX() == configuration.mapWidth() && direction.getMoveOffset().getX() == 1) ||
                (position.getX() == 0 && direction.getMoveOffset().getX() == -1)) {
            return NextMoveType.LEAP_TO_OTHER_SIDE;
        }
        return NextMoveType.REGULAR;
    }
}
