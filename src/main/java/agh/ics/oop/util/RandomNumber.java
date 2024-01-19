package agh.ics.oop.util;

import java.util.*;

// generates random integers in range [start, end)
// without duplicates
public class RandomNumber implements Iterator<Integer> {
    private final int start;
    private final int length;
    private final List<Integer> seenNumbers;
    private final Random randomGenerator = new Random();
    public RandomNumber(int start, int end) {
        this.start = start;
        this.length = end - start;
        seenNumbers = new LinkedList<>();
    }
    public boolean hasNext() {
        return seenNumbers.size() < length;
    }
    // source: https://stackoverflow.com/questions/4040001/creating-random-numbers-with-no-duplicates
    public Integer next() {
        int randomNumber = start + randomGenerator.nextInt(length - seenNumbers.size());
        ListIterator<Integer> seenIterator = seenNumbers.listIterator();
        while (seenIterator.hasNext()) {
            int nextNumber = seenIterator.next();
            if (nextNumber <= randomNumber) randomNumber++;
            else {
                seenIterator.previous();
                break;
            }
        }
        seenNumbers.add(seenIterator.nextIndex(), randomNumber);
        System.out.println("next random number is " + randomNumber);
        return randomNumber;
    }
    public void refreshRange() {
        seenNumbers.clear();
    }
    public void restoreNumber(int number) {
        seenNumbers.remove(Integer.valueOf(number));
    }
}
