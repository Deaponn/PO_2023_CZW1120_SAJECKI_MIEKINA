package agh.ics.oop.model;

import agh.ics.oop.Configuration;
import agh.ics.oop.entities.*;
import agh.ics.oop.util.RandomNumber;

import java.util.*;

import static agh.ics.oop.Configuration.Fields.*;

public class EquatorialWorldMap implements WorldMap {
    private final AnimalFactory animalFactory;
    private final PlantFactory plantFactory;
    private final Map<Vector2D, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2D, Plant> plants = new HashMap<>();
    private final List<Vector2D> equator;
    private final RandomNumber randomEquatorIndex;
    private final List<Vector2D> regularField;
    private final RandomNumber randomRegularFieldIndex;
    private final int mapWidth;
    private final int mapHeight;
    private final float equatorSize;
    private final float plantGrowAtEquatorChance;
    private final List<MapChangeListener> subscribers = new LinkedList<>();
    private final UUID mapUUID = UUID.randomUUID();

    public EquatorialWorldMap(Configuration configuration) {
        this.mapWidth = configuration.get(MAP_WIDTH);
        this.mapHeight = configuration.get(MAP_HEIGHT);
        this.equatorSize = configuration.get(EQUATOR_SIZE);
        this.plantGrowAtEquatorChance = configuration.get(PLANT_GROW_AT_EQUATOR_CHANCE);

        GenomeFactory genomeFactory = new GenomeFactory(configuration);
        this.animalFactory = new AnimalFactory(configuration, genomeFactory);
        this.plantFactory = new PlantFactory(configuration);

        List<List<Vector2D>> fields = buildFields();
        equator = fields.get(0);
        regularField = fields.get(1);
        randomEquatorIndex = new RandomNumber(0, equator.size());
        randomRegularFieldIndex = new RandomNumber(0, regularField.size());

        growPlants(configuration.get(STARTING_PLANTS_NUMBER));

        populateWithAnimals(configuration.get(STARTING_ANIMALS_NUMBER), configuration.get(INITIAL_ANIMAL_ENERGY));
    }

    private List<List<Vector2D>> buildFields() {
        boolean[][] isEquator = new boolean[this.mapHeight][this.mapWidth];

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
        List<Vector2D> equator = new ArrayList<>(equatorSize);
        List<Vector2D> regularField = new ArrayList<>(this.mapWidth * this.mapHeight - equatorSize);
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

        return List.of(equator, regularField);
    }

    private void growPlants(int numberOfPlants) {
        for (int i = 0; i < numberOfPlants; i++) {
            Vector2D plantPosition;
            if (random.nextDouble() < this.plantGrowAtEquatorChance)
                plantPosition = equator.get(randomEquatorIndex.next());
            else plantPosition = regularField.get(randomRegularFieldIndex.next());
            this.placeElement(plantFactory.create(plantPosition));
        }
    }

    private void populateWithAnimals(int startingAnimalsNumber, int initialAnimalEnergy) {
        for (int i = 0; i < startingAnimalsNumber; i++) {
            Vector2D randomPosition = new Vector2D(random.nextInt(this.mapWidth), random.nextInt(this.mapHeight));
            Animal animal = this.animalFactory.create(randomPosition, initialAnimalEnergy);
            this.placeElement(animal);
        }
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
            case LEAP_TO_LEFT -> animal.setPosition(new Vector2D(0, position.getY()));
            case LEAP_TO_RIGHT -> animal.setPosition(new Vector2D(this.mapWidth - 1, position.getY()));
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
        if ((position.getY() == this.mapHeight - 1 && offset.getY() == 1) ||
                (position.getY() == 0 && offset.getY() == -1)
            ) {
            return NextMoveType.POLAR;
        }
        // move would cause X to go out of bounds
        if (position.getX() == this.mapWidth - 1 && offset.getX() == 1)
            return NextMoveType.LEAP_TO_LEFT;
        if (position.getX() == 0 && offset.getX() == -1)
            return NextMoveType.LEAP_TO_RIGHT;
        return NextMoveType.REGULAR;
    }

    private final static Random random = new Random();
}
