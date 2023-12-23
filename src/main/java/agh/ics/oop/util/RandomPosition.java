package agh.ics.oop.util;

import agh.ics.oop.model.Vector2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

// creates positions with no duplicates
public class RandomPosition implements Iterator<Vector2D> {
    private final int height;
    private int grassLeft;
    private final List<Iterator<Integer>> randomInts;
    private final Random randomGenerator = new Random();
    public RandomPosition(int width, int height, int grass) {
        this.height = height;
        this.grassLeft = grass;
        randomInts = new ArrayList<>(height);
        while (randomInts.size() < height) randomInts.add(new RandomNumber(0, width));

    }
    @Override
    public boolean hasNext() {
        return grassLeft > 0;
    }
    @Override
    public Vector2D next() {
        grassLeft--;
        int randomRow = randomGenerator.nextInt(height);
        Iterator<Integer> randomColumnIterator = randomInts.get(randomRow);
        while (!randomColumnIterator.hasNext()) {
            if (randomRow == height) randomRow = 0;
            else randomRow++;
            randomColumnIterator = randomInts.get(randomRow);
        }
        int randomColumn = randomColumnIterator.next();
        return new Vector2D(randomColumn, randomRow);
    }
}
