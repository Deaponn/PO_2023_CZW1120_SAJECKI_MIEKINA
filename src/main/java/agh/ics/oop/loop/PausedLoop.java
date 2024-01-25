package agh.ics.oop.loop;

public interface PausedLoop extends Loop {
    void resume();
    void pause();
    boolean isPaused();
}
