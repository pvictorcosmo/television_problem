package org.example.television_problem.view_model.guest;

import org.example.television_problem.model.Guest;
import org.example.television_problem.model.GuestStatus;

import de.saxsys.mvvmfx.ViewModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.beans.property.*;
import javafx.util.Duration;

public class GuestViewModel implements ViewModel {

    // Propriedades observáveis para a View
    private final DoubleProperty x = new SimpleDoubleProperty(50);
    private final DoubleProperty y = new SimpleDoubleProperty(60);
    private final ObjectProperty<Image> currentImage = new SimpleObjectProperty<>();

    private Image[] frontFrames, backFrames, leftFrames, rightFrames;
    private int currentFrame = 0;
    private Timeline timeline;

    public GuestViewModel() {

        loadFrames();
        currentImage.set(frontFrames[0]); // Imagem inicial
        initTimeline();
    }

    private void loadFrames() {
        frontFrames = new Image[] {
                new Image(getClass().getResource("/org/example/television_problem/view/assets/sprites/front_1.png")
                        .toExternalForm()),
                new Image(getClass().getResource("/org/example/television_problem/view/assets/sprites/front_2.png")
                        .toExternalForm()),
                new Image(getClass().getResource("/org/example/television_problem/view/assets/sprites/front_3.png")
                        .toExternalForm())
        };

        backFrames = new Image[] {
                new Image(getClass().getResource("/org/example/television_problem/view/assets/sprites/back_1.png")
                        .toExternalForm()),
                new Image(getClass().getResource("/org/example/television_problem/view/assets/sprites/back_2.png")
                        .toExternalForm()),
                new Image(getClass().getResource("/org/example/television_problem/view/assets/sprites/back_3.png")
                        .toExternalForm())
        };

        leftFrames = new Image[] {
                new Image(getClass().getResource("/org/example/television_problem/view/assets/sprites/left_1.png")
                        .toExternalForm()),
                new Image(getClass().getResource("/org/example/television_problem/view/assets/sprites/left_2.png")
                        .toExternalForm())
        };
        rightFrames = new Image[] {
                new Image(getClass().getResource("/org/example/television_problem/view/assets/sprites/right_1.png")
                        .toExternalForm()),
                new Image(getClass().getResource("/org/example/television_problem/view/assets/sprites/right_2.png")
                        .toExternalForm())
        };
    }

    private void initTimeline() {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    // Métodos de movimento
    public void moveFront() {
        startAnimation(frontFrames, 0, 5);
    }

    public void moveBack() {
        startAnimation(backFrames, 0, -5);
    }

    public void moveLeft() {
        startAnimation(leftFrames, -5, 0);
    }

    public void moveRight() {
        startAnimation(rightFrames, 5, 0);
    }

    private void startAnimation(Image[] frames, int deltaX, int deltaY) {
        timeline.stop();
        timeline.getKeyFrames().setAll(
                new KeyFrame(Duration.millis(200), e -> {
                    currentFrame = (currentFrame + 1) % frames.length;
                    currentImage.set(frames[currentFrame]); // Atualiza o frame
                    x.set(x.get() + deltaX); // Atualiza posição X
                    y.set(y.get() + deltaY); // Atualiza posição Y
                }));
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }

    // Getters para as propriedades
    public DoubleProperty xProperty() {
        return x;
    }

    public DoubleProperty yProperty() {
        return y;
    }

    public ObjectProperty<Image> currentImageProperty() {
        return currentImage;
    }
}