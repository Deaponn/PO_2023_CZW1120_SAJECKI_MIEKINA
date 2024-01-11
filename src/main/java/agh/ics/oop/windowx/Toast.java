package agh.ics.oop.windowx;

import agh.ics.oop.window.Window;
import javafx.concurrent.Task;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Toast extends StackPane {
    private final Window<?> window;
    private final Text text;
    private final ToastFadeOut toastFadeOut;
    private final Toast.Duration duration;

    public Toast(Window<?> window, String message, Toast.Duration duration) {
        super();
        this.window = window;
        this.duration = duration;

        this.text = new Text(message);
        this.text.setFill(Toast.textPaint);

        double toastWidth = this.getToastWidth();
        double toastHeight = this.getToastHeight();

        Region region = new Region();

        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(toastWidth);
        rectangle.setHeight(toastHeight);
        rectangle.setArcWidth(toastHeight);
        rectangle.setArcHeight(toastHeight);

        region.setBackground(Background.fill(Toast.backgroundPaint));
        region.setBorder(Border.stroke(Toast.borderPaint));
        region.setShape(rectangle);
        region.setCenterShape(true);
        region.setScaleShape(false);

        this.getChildren().addAll(region, text);
        this.followWindowSize();

        this.toastFadeOut = new ToastFadeOut(this);
        this.startDelay();
    }

    private void startDelay() {
        Task<Long> hideTask = new Task<>() {
            @Override
            protected Long call() throws InterruptedException {
                long delay = Toast.this.duration.milliseconds;
                Thread.sleep(delay);
                Toast.this.toastFadeOut.play();
                return delay;
            }
        };

        Thread hideTaskThread = new Thread(hideTask);
        hideTaskThread.start();
    }

    private double getToastWidth() {
        return this.text.getFont().getSize() * (this.text.getText().length() + 2);
    }

    private double getToastHeight() {
        return this.text.getFont().getSize() + Toast.verticalMargin;
    }

    private void followWindowSize() {
        Pane root = this.window.getRoot();
        root.widthProperty().subscribe(width -> this.setLayoutX((double) width / 2));
        root.heightProperty().subscribe(height -> this.setLayoutY((double) height - 20));
        this.window.getRoot().getChildren().remove(this);
    }

    public void close() {
        this.window.removeNode(this);
    }

    private static final Paint textPaint = Paint.valueOf("#e4e4e4");
    private static final Paint backgroundPaint = Paint.valueOf("#303030");
    private static final Paint borderPaint = Paint.valueOf("#444444");
    private static final double verticalMargin = 5;

    public enum Duration {
        SHORT(3000),
        MEDIUM(5000),
        LONG(7000);

        private final long milliseconds;

        Duration(long milliseconds) {
            this.milliseconds = milliseconds;
        }
    }
}
