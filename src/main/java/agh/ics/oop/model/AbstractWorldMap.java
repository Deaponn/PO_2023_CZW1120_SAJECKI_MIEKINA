package agh.ics.oop.model;

import agh.ics.oop.Configuration;
import agh.ics.oop.entities.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static agh.ics.oop.Configuration.Fields.*;

public abstract class AbstractWorldMap implements WorldMap {
    protected final AnimalFactory animalFactory;
    protected final PlantFactory plantFactory;
    protected final Map<Vector2D, List<Animal>> animals = new ConcurrentHashMap<>();
    protected final Map<Vector2D, Plant> plants = new ConcurrentHashMap<>();
    protected final int mapWidth;
    protected final int mapHeight;
    protected final int numberOfGrowingPlants;
    protected final List<ObjectEventListener<WorldMap>> subscribers = new LinkedList<>();
    protected final String mapTitle;

    public AbstractWorldMap(String mapTitle, Configuration configuration) {
        this.mapWidth = configuration.get(MAP_WIDTH);
        this.mapHeight = configuration.get(MAP_HEIGHT);
        this.numberOfGrowingPlants = configuration.get(NUMBER_OF_GROWING_PLANTS);
        this.mapTitle = !mapTitle.isEmpty() ? mapTitle : UUID.randomUUID().toString();

        GenomeFactory genomeFactory = new GenomeFactory(configuration);
        this.animalFactory = new AnimalFactory(configuration, genomeFactory);
        this.plantFactory = new PlantFactory(configuration);

        populateWithAnimals(configuration.get(STARTING_ANIMALS_NUMBER), configuration.get(INITIAL_ANIMAL_ENERGY));
    }

    private void populateWithAnimals(int startingAnimalsNumber, int initialAnimalEnergy) {
        for (int i = 0; i < startingAnimalsNumber; i++) {
            Vector2D randomPosition = new Vector2D(random.nextInt(this.mapWidth), random.nextInt(this.mapHeight));
            Animal animal = this.animalFactory.create(randomPosition, initialAnimalEnergy);
            this.placeElement(animal);
        }
    }

    abstract void growPlants();

    abstract void eatPlants();

    public void step() {
        System.out.println("stepping");
        this.removeDead();
        this.moveAnimals();
        this.orderOnFields();
        this.eatPlants();
        this.breedAnimals();
        this.growPlants();
        this.refreshAnimals();
        this.notifySubscribers("step");
    }

    private void removeDead() {
        for (Vector2D animalPosition : this.animals.keySet()) {
            List<Animal> animalList = this.animals.get(animalPosition);
            for (int i = animalList.size() - 1; i >= 0; i--) {
                if (!animalList.get(i).getAlive()) {
                    Animal deadBody = animalList.remove(i);
                    this.notifySubscribers("animal died " + deadBody.getAge());
                }
            }
            if (animalList.isEmpty()) this.animals.remove(animalPosition);
        }
    }

    abstract void moveAnimal(Animal animal);

    private void moveAnimals() {
        Set<Vector2D> uniqueAnimalsPositions = this.animals.keySet();
        for (Vector2D uniquePosition : uniqueAnimalsPositions) {
            List<Animal> animalList = this.animals.get(uniquePosition);
            for (int i = animalList.size() - 1; i >= 0; i--) {
                Animal animal = animalList.get(i);
                moveAnimal(animal);
                animal.update();
            }
        }
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

    private void orderOnFields() {
        for (List<Animal> animalList : this.animals.values()) {
            animalList.sort(null);
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

        this.notifySubscribers("place");
    }

    // is it necessary? there is no scenario when a move could be illegal
    @Override
    public boolean isOccupied(Vector2D position) {
        return false;
    }

    protected Ground groundAtPosition(Vector2D position) {
        return new Ground(position);
    }

    @Override
    public List<WorldElement> getElements(Vector2D position) {
        List<WorldElement> elementList = new LinkedList<>();
        elementList.add(this.groundAtPosition(position));

        Plant plant = this.plants.get(position);
        if (plant != null) elementList.add(plant);

        List<Animal> animalList = this.animals.get(position);
        if (animalList != null) elementList.addAll(animalList);

        return elementList;
    }

    public Map<Vector2D, List<Animal>> getAnimals() {
        return this.animals;
    }

    public Map<Vector2D, Plant> getPlants() {
        return this.plants;
    }

    @Override
    public Boundary getCurrentBounds() {
        return Boundary.fromSize(this.mapWidth, this.mapHeight);
    }

    @Override
    public String getTitle() {
        return this.mapTitle;
    }

    @Override
    public void addEventSubscriber(ObjectEventListener<WorldMap> listener) {
        subscribers.add(listener);
    }

    @Override
    public void removeEventSubscriber(ObjectEventListener<WorldMap> listener) {
        subscribers.remove(listener);
    }

    @Override
    public void notifySubscribers(String message) {
        for (ObjectEventListener<WorldMap> subscriber : subscribers) {
            subscriber.sendEventData(this, message);
        }
    }

    @Override
    public boolean isListenerSubscribed(ObjectEventListener<WorldMap> listener) {
        return subscribers.contains(listener);
    }

    private final static Random random = new Random();
}
