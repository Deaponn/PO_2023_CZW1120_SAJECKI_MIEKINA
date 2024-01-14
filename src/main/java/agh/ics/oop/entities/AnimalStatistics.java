package agh.ics.oop.entities;

import java.util.LinkedList;
import java.util.List;

public class AnimalStatistics {
    public int age = 0;
    public int eatenCount = 0;
    public int deathDate;
    public final List<Animal> kids;

    public AnimalStatistics() {
        this.kids = new LinkedList<>();
    }

    public void addAge() {
        this.age++;
    }

    public void addKid(Animal kid) {
        this.kids.add(kid);
    }

    public int countKids() { return this.kids.size(); }

    public void addAncestorsToList(List<Animal> seenAlready) {
        this.kids.forEach((Animal animal) -> {
            if (!seenAlready.contains(animal)) {
                seenAlready.add(animal);
                animal.constructAncestorsList(seenAlready);
            }
        });
    }

    public void addEaten() {
        this.eatenCount++;
    }

    public void die(int day) { this.deathDate = day; }
}
