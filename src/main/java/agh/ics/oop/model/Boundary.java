package agh.ics.oop.model;

public record Boundary(Vector2D lowerLeft, Vector2D upperRight) {
    public boolean isInBounds(Vector2D position) {
        Vector2D lowerLeftMix = this.lowerLeft.lowerLeft(position);
        Vector2D upperRightMix = this.upperRight.upperRight(position);

        return lowerLeftMix.equals(this.lowerLeft) && upperRightMix.equals(this.upperRight);
    }
}
