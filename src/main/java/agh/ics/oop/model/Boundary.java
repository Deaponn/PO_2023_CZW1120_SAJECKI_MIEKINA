package agh.ics.oop.model;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record Boundary(Vector2D lowerLeft, Vector2D upperRight) {
    public boolean isInBounds(Vector2D position) {
        Vector2D lowerLeftMix = this.lowerLeft.lowerLeft(position);
        Vector2D upperRightMix = this.upperRight.upperRight(position);

        return lowerLeftMix.equals(this.lowerLeft) && upperRightMix.equals(this.upperRight);
    }

    public Vector2D getSize() {
        return this.upperRight.subtract(this.lowerLeft);
    }

    public <T> Stream<T> mapAllPositions(BiFunction<Integer, Integer, T> mapper) {
        int startX = this.lowerLeft.getX();
        int endX = this.upperRight.getX();
        int startY = this.lowerLeft.getY();
        int endY = this.upperRight.getY();

        return IntStream.range(startX, endX + 1).boxed().flatMap(x ->
                IntStream.range(startY, endY + 1).boxed().map(y -> mapper.apply(x, y))
        );
    }

    public <T> Stream<T> mapAllPositions(Function<Vector2D, T> mapper) {
        return this.mapAllPositions((x, y) -> mapper.apply(new Vector2D(x, y)));
    }

    public <T> Stream<T> mapColumns(Function<Integer, T> mapper) {
        int startX = this.lowerLeft.getX();
        int endX = this.upperRight.getX();
        return IntStream.range(startX, endX).boxed().map(mapper);
    }

    public <T> Stream<T> mapRows(Function<Integer, T> mapper) {
        int startY = this.lowerLeft.getY();
        int endY = this.upperRight.getY();
        return IntStream.range(startY, endY).boxed().map(mapper);
    }

    public <T> Stream<Stream<T>> mapByColumns(BiFunction<Integer, Integer, T> mapper) {
        int startX = this.lowerLeft.getX();
        int endX = this.upperRight.getX();
        int startY = this.lowerLeft.getY();
        int endY = this.upperRight.getY();

        return IntStream.range(startX, endX + 1).boxed().map(x ->
                IntStream.range(startY, endY + 1).boxed().map(y -> mapper.apply(x, y))
        );
    }

    public <T> Stream<Stream<T>> mapByColumns(Function<Vector2D, T> mapper) {
        return this.mapByColumns((x, y) -> mapper.apply(new Vector2D(x, y)));
    }

    public <T> Stream<Stream<T>> mapByRows(BiFunction<Integer, Integer, T> mapper) {
        int startX = this.lowerLeft.getX();
        int endX = this.upperRight.getX();
        int startY = this.lowerLeft.getY();
        int endY = this.upperRight.getY();

        return IntStream.range(startY, endY + 1).boxed().map(y ->
                IntStream.range(startX, endX + 1).boxed().map(x -> mapper.apply(x, y))
        );
    }

    public <T> Stream<Stream<T>> mapByRows(Function<Vector2D, T> mapper) {
        return this.mapByRows((x, y) -> mapper.apply(new Vector2D(x, y)));
    }

    public void forEachAllPositions(BiConsumer<Integer, Integer> action) {
        int startX = this.lowerLeft.getX();
        int endX = this.upperRight.getX();
        int startY = this.lowerLeft.getY();
        int endY = this.upperRight.getY();

        IntStream.range(startX, endX + 1).boxed().forEach(x ->
                IntStream.range(startY, endY + 1).boxed().forEach(y -> action.accept(x, y))
        );
    }

    public void forEachAllPositions(Consumer<Vector2D> action) {
        this.forEachAllPositions((x, y) -> action.accept(new Vector2D(x, y)));
    }
}
