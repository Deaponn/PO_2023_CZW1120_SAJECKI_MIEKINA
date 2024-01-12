package agh.ics.oop.entities;

public class AnimalStatistics {
    public int age = 0;
    public int kidsCount = 0;
    // TODO set ancestor count
    public int ancestorsCount = 0;
    public int eatenCount = 0;

    public void addAge() {
        this.age++;
    }

    public void addKid() {
        this.kidsCount++;
    }

    public void addEaten() {
        this.eatenCount++;
    }
}
