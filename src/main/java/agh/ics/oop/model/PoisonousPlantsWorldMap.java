package agh.ics.oop.model;

import agh.ics.oop.Configuration;
import agh.ics.oop.entities.*;
import agh.ics.oop.util.RandomNumber;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static agh.ics.oop.Configuration.Fields.*;

public class PoisonousPlantsWorldMap implements WorldMap {
    private final AnimalFactory animalFactory;
    private final PlantFactory plantFactory;
    private final Map<Vector2D, List<Animal>> animals = new ConcurrentHashMap<>();
    private final Map<Vector2D, Plant> plants = new ConcurrentHashMap<>();
    private final RandomNumber randomFieldIndex;
    private final int mapWidth;
    private final int mapHeight;
    private final float poisonousFieldsFraction;
    private Vector2D poisonousFieldStartPosition;
    private Vector2D poisonousFieldEndPosition;
    private final int numberOfGrowingPlants;
    private final List<MapChangeListener> subscribers = new LinkedList<>();
    private final UUID mapUUID = UUID.randomUUID();

    public PoisonousPlantsWorldMap(Configuration configuration) {
        this.mapWidth = configuration.get(MAP_WIDTH);
        this.mapHeight = configuration.get(MAP_HEIGHT);
        this.poisonousFieldsFraction = configuration.get(SPECIAL_FIELDS_FRACTION);
        this.numberOfGrowingPlants = configuration.get(NUMBER_OF_GROWING_PLANTS);
        this.randomFieldIndex = new RandomNumber(0, this.mapHeight * this.mapWidth);

        GenomeFactory genomeFactory = new GenomeFactory(configuration);
        this.animalFactory = new AnimalFactory(configuration, genomeFactory);
        this.plantFactory = new PlantFactory(configuration);

        this.buildFields();

        growPlants(configuration.get(STARTING_PLANTS_NUMBER));

        populateWithAnimals(configuration.get(STARTING_ANIMALS_NUMBER), configuration.get(INITIAL_ANIMAL_ENERGY));

        System.out.println("MAP OBJECT IS BUILT CORRECTLY");
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

    private void growPlants() { this.growPlants(this.numberOfGrowingPlants); }

    private void growPlants(int numberOfPlants) {
        for (int i = 0; i < numberOfPlants; i++) {
            if (!this.randomFieldIndex.hasNext()) return;
            int randomFieldIndex = this.randomFieldIndex.next();
            Vector2D plantPosition = new Vector2D(
                    randomFieldIndex % this.mapWidth, randomFieldIndex / this.mapWidth
            );
            boolean isOnHealthyArea = !(this.poisonousFieldStartPosition.getX() <= plantPosition.getX() &&
                    plantPosition.getX() <= this.poisonousFieldEndPosition.getX() &&
                    this.poisonousFieldEndPosition.getY() <= plantPosition.getY() &&
                    plantPosition.getY() <= this.poisonousFieldStartPosition.getY());
            this.placeElement(plantFactory.create(plantPosition, isOnHealthyArea));
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
                this.randomFieldIndex.restoreNumber(position.getX() + position.getY() * this.mapWidth);
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

    public NextMoveType moveType(Vector2D position, MapDirection direction, boolean dodgedAlready) {
        // move would cause to eat a poisonous plant
        Vector2D newPosition = position.add(direction.moveOffset);
        boolean isOnHealthyArea = !(this.poisonousFieldStartPosition.getX() <= newPosition.getX() &&
                newPosition.getX() <= this.poisonousFieldEndPosition.getX() &&
                this.poisonousFieldEndPosition.getY() <= newPosition.getY() &&
                newPosition.getY() <= this.poisonousFieldStartPosition.getY());
        if (!dodgedAlready && this.plants.containsKey(newPosition) && !isOnHealthyArea) return NextMoveType.DODGE;

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
