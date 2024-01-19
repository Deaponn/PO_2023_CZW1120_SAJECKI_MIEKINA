package agh.ics.oop.model;

import java.util.Random;

public enum MapDirection {
    NORTH     (new Vector2D( 0,  1)),
    NORTH_EAST(new Vector2D( 1,  1)),
    EAST      (new Vector2D( 1,  0)),
    SOUTH_EAST(new Vector2D( 1, -1)),
    SOUTH     (new Vector2D( 0, -1)),
    SOUTH_WEST(new Vector2D(-1, -1)),
    WEST      (new Vector2D(-1,  0)),
    NORTH_WEST(new Vector2D(-1,  1));

    public final Vector2D moveOffset;

    MapDirection(Vector2D moveOffset) {
        this.moveOffset = moveOffset;
    }

    public MapDirection rotateBy(MoveDirection moveDirection) {
        MapDirection[] mapDirections = MapDirection.values();
        int rotation = moveDirection.getRotation();
        int newRotation = (this.ordinal() + rotation) % mapDirections.length;
        return mapDirections[newRotation];
    }

    public static MapDirection randomDirection() {
        Random random = new Random();
        MapDirection[] mapDirections = MapDirection.values();
        return mapDirections[random.nextInt(mapDirections.length)];
    }

    // corresponds to directions of keyboard numpad
    public String toString() {
        return switch(this) {
            case NORTH -> "8";
            case NORTH_EAST -> "9";
            case EAST -> "6";
            case SOUTH_EAST -> "3";
            case SOUTH -> "2";
            case SOUTH_WEST -> "1";
            case WEST -> "4";
            case NORTH_WEST -> "7";
        };
    }
}
