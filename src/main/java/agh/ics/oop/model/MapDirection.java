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

    private final Vector2D moveOffset;

    MapDirection(Vector2D moveOffset) {
        this.moveOffset = moveOffset;
    }

    public Vector2D getMoveOffset() {
        return this.moveOffset;
    }

    public MapDirection rotateBy(MoveDirection moveDirection) {
        MapDirection[] mapDirections = MapDirection.values();
        int rotation = moveDirection.getRotation();
        int newRotation = this.ordinal() + rotation % mapDirections.length;
        return mapDirections[newRotation];
    }

    public static MapDirection randomDirection() {
        MapDirection[] mapDirections = MapDirection.values();
        return mapDirections[new Random().nextInt(mapDirections.length)];
    }
}
