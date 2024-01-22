package agh.ics.oop.model;

import agh.ics.oop.entities.Animal;
import agh.ics.oop.entities.Plant;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2D and MoveDirection classes are defined.
 *
 * @author apohllo, idzik, szym-mie
 */
public interface WorldMap extends MoveValidator, MapChangeEmitter {

    /**
     * Place an animal on the map.
     *
     * @param worldElement element to place on the map.
     */
    void placeElement(WorldElement worldElement);

    /**
     * Moves an animal (if it is present on the map).
     *
     * @param animal entity to move.
     */
//    void moveAnimal(Animal animal);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return true if the position is occupied.
     */
    boolean isOccupied(Vector2D position);

    void step();

    /**
     * Return an animal at a given position.
     *
     * @param position The position of the animal.
     * @return animal or null if the position is not occupied.
     */
    List<WorldElement> getElements(Vector2D position);

    /**
     * Retrieve current bounds of this map.
     * @return bounds of the map.
     */
    Boundary getCurrentBounds();

    /**
     * Get a unique ID of this map.
     * @return unique ID.
     */
    UUID getID();

    Map<Vector2D, List<Animal>> getAnimals();

    Map<Vector2D, Plant> getPlants();
}