package agh.ics.oop.util;

import agh.ics.oop.model.Vector2D;

import java.util.Iterator;

// creates positions with no duplicates
public class RandomPositionGenerator implements Iterable<Vector2D> {
    private final RandomPosition position;
    public RandomPositionGenerator(int width, int height, int grass) {
        position = new RandomPosition(width, height, grass);
    }
    public Iterator<Vector2D> iterator() {
        return position;
    }
}
