package agh.ics.oop.model;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record Boundary(Vector2D lowerLeft, Vector2D upperRight) {
    public boolean isInBounds(Vector2D position) {
        Vector2D lowerLeftMix = this.lowerLeft.lowerLeft(position);
        Vector2D upperRightMix = this.upperRight.upperRight(position);

        return lowerLeftMix.equals(this.lowerLeft) && upperRightMix.equals(this.upperRight);
    }

    public <T> Stream<T> map(BiFunction<Integer, Integer, T> mapper) {
        int startX = this.lowerLeft.getX();
        int endX = this.upperRight.getX();
        int startY = this.lowerLeft.getY();
        int endY = this.upperRight.getY();

        return IntStream.range(startX, endX + 1).boxed().flatMap(x ->
                IntStream.range(startY, endY + 1).boxed().map(y -> mapper.apply(x, y))
        );
    }

    public <T> Stream<T> map(Function<Vector2D, T> mapper) {
        return this.map((x, y) -> mapper.apply(new Vector2D(x, y)));
    }
}
