package agh.ics.oop.model;

import agh.ics.oop.Configuration;
import agh.ics.oop.entities.*;
import agh.ics.oop.util.RandomNumber;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static agh.ics.oop.Configuration.Fields.*;

public class EquatorialWorldMap implements WorldMap {
    private final AnimalFactory animalFactory;
    private final PlantFactory plantFactory;
    private final Map<Vector2D, List<Animal>> animals = new ConcurrentHashMap<>();
    private final Map<Vector2D, Plant> plants = new ConcurrentHashMap<>();
    private final List<Vector2D> equator;
    private final RandomNumber randomEquatorIndex;
    private final List<Vector2D> regularField;
    private final RandomNumber randomRegularFieldIndex;
    private final int mapWidth;
    private final int mapHeight;
    private final float equatorFieldsFraction;
    private final float plantGrowAtEquatorChance;
    private final int numberOfGrowingPlants;
    private final List<MapChangeListener> subscribers = new LinkedList<>();
    private final UUID mapUUID = UUID.randomUUID();

    public EquatorialWorldMap(Configuration configuration) {
        this.mapWidth = configuration.get(MAP_WIDTH);
        this.mapHeight = configuration.get(MAP_HEIGHT);
        this.equatorFieldsFraction = configuration.get(SPECIAL_FIELDS_FRACTION);
        this.plantGrowAtEquatorChance = configuration.get(PLANT_GROW_AT_EQUATOR_CHANCE);
        this.numberOfGrowingPlants = configuration.get(NUMBER_OF_GROWING_PLANTS);

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

        System.out.println("MAP OBJECT IS BUILT CORRECTLY");
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

        // code to test the above solution
        // TODO: remove when development is finished
        this.printMapState();

        return List.of(equator, regularField);
    }

    public void printMapState() {
        final StringBuilder sb = new StringBuilder("|");
        for (int y = 0; y < this.mapHeight; y++) {
            for (int x = 0; x < this.mapWidth; x++) {
                Vector2D position = new Vector2D(x, y);
                if (this.animals.containsKey(position) && !this.animals.get(position).isEmpty())
                    sb.append("|").append(this.animals.get(position).get(0));
                else if (this.plants.containsKey(position)) sb.append("|*");
                else sb.append("| ");
            }
            sb.append("||\n|");
        }
        System.out.println(sb);
    }

    private void growPlants() { this.growPlants(this.numberOfGrowingPlants); }

    private void growPlants(int numberOfPlants) {
        for (int i = 0; i < numberOfPlants; i++) {
            Vector2D plantPosition = null;
            if (random.nextDouble() < this.plantGrowAtEquatorChance && randomEquatorIndex.hasNext())
                plantPosition = equator.get(randomEquatorIndex.next());
            else if (randomRegularFieldIndex.hasNext()) plantPosition = regularField.get(randomRegularFieldIndex.next());
            if (plantPosition != null) this.placeElement(plantFactory.create(plantPosition));
        }
    }

    private void populateWithAnimals(int startingAnimalsNumber, int initialAnimalEnergy) {
        for (int i = 0; i < startingAnimalsNumber; i++) {
            Vector2D randomPosition = new Vector2D(random.nextInt(this.mapWidth), random.nextInt(this.mapHeight));
            Animal animal = this.animalFactory.create(randomPosition, initialAnimalEnergy);
            this.placeElement(animal);
        }
    }

    public void step() {
        System.out.println("stepping");
        this.removeDead();
        this.moveAnimals();
        this.orderOnFields();
        this.eatPlants();
        this.breedAnimals();
        this.growPlants();
        this.refreshAnimals();
        this.mapChangeNotify("step");
    }

    private void removeDead() {
        for (Vector2D animalPosition : this.animals.keySet()) {
            List<Animal> animalList = this.animals.get(animalPosition);
            for (int i = animalList.size() - 1; i >= 0; i--) {
                if (!animalList.get(i).getAlive()) animalList.remove(i);
            }
            if (animalList.isEmpty()) this.animals.remove(animalPosition);
        }
    }

    private void moveAnimals() {
        Set<Vector2D> uniqueAnimalsPositions = this.animals.keySet();
        for (Vector2D uniquePosition : uniqueAnimalsPositions) {
            List<Animal> animalList = this.animals.get(uniquePosition);
            for (int i = animalList.size() - 1; i >= 0; i--) {
                Animal animal = animalList.get(i);
                animal.update();
                moveAnimal(animal);
            }
        }
    }

    private void orderOnFields() {
        for (List<Animal> animalList : this.animals.values()) {
            animalList.sort(null);
        }
    }

    private void eatPlants() {
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

    private void breedAnimals() {
        for (List<Animal> animalList : this.animals.values()) {
            for (int i = 0; i < animalList.size() - 1; i += 2) {
                if (!this.animalFactory.canBreed(animalList.get(i), animalList.get(i + 1))) break;
                Animal kid = this.animalFactory.breedAnimals(animalList.get(i), animalList.get(i + 1));
                this.placeElement(kid);
            }
        }
    }

    private void refreshAnimals() {
        for (List<Animal> animalList : this.animals.values()) {
            for (Animal animal : animalList) {
                animal.refreshStatus();
            }
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
                List<Animal> list = new CopyOnWriteArrayList<>();
                list.add(animal);
                animals.put(animal.getPosition(), list);
            }
        }

        this.mapChangeNotify("place");
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

    // is it necessary? there is no scenario when a move could be illegal
    @Override
    public boolean isOccupied(Vector2D position) {
        return false;
    }

    @Override
    public List<WorldElement> getElements(Vector2D position) {
        List<WorldElement> elementList = new LinkedList<>();
        elementList.add(new Ground(position));

        Plant plant = this.plants.get(position);
        if (plant != null) elementList.add(plant);

        List<Animal> animalList = this.animals.get(position);
        if (animalList != null && !animalList.isEmpty())
            elementList.add(animalList.get(0));

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
