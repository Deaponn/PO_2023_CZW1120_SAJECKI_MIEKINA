package agh.ics.oop.windowx;

import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class ToastFadeOut {
    private final FadeTransition fadeTransition;

    public ToastFadeOut(Toast toast) {
        this.fadeTransition = new FadeTransition(Duration.seconds(1), toast);
        this.fadeTransition.setFromValue(1.0);
        this.fadeTransition.setToValue(0.0);
        this.fadeTransition.setOnFinished(event -> toast.close());
    }

    public void play() {
        this.fadeTransition.play();
    }
}
