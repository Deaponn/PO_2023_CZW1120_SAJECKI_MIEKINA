package agh.ics.oop.model;

import java.util.Random;

public enum MoveDirection {
    ROTATE_0,
    ROTATE_45,
    ROTATE_90,
    ROTATE_135,
    ROTATE_180,
    ROTATE_225,
    ROTATE_270,
    ROTATE_315;

    public int getRotation() {
        return this.ordinal();
    }

    public static MoveDirection randomDirection() {
        Random random = new Random();
        MoveDirection[] moveDirections = MoveDirection.values();
        return moveDirections[random.nextInt(moveDirections.length)];
    }
}
