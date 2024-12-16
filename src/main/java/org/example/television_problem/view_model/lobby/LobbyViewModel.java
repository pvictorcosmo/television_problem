package org.example.television_problem.view_model.lobby;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import org.example.television_problem.PopupUtil;
import org.example.television_problem.model.Guest;
import org.example.television_problem.model.GuestSprite;
import org.example.television_problem.model.GuestStatus;

import de.saxsys.mvvmfx.ViewModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class LobbyViewModel implements ViewModel {
    private ListProperty<Guest> guests = new SimpleListProperty<>(FXCollections.observableArrayList());
    public static final Semaphore favoriteChannelSemaphore = new Semaphore(1);
    public static Semaphore tvOfflineSemaphore = new Semaphore(1);
    private int actuallyChannel = -1;
    private int spectators;
    private Image normalSprite;

    public LobbyViewModel() {
        // Inicializando guests, exemplo de 2 guests
        Guest guest1 = new Guest(1, 1, 10, 10, this, GuestStatus.BLOCKED);
        guest1.start();
        Guest guest2 = new Guest(2, 2, 10, 10, this, GuestStatus.BLOCKED);
        guest2.start();
        guests.add(guest1);
        guests.add(guest2);
        loadFrames();
        currentImage.set(frontFrames[0]); // Imagem inicial
        initTimeline();
    }

    public ListProperty<Guest> getGuests() {
        return this.guests;
    }

    private final DoubleProperty x = new SimpleDoubleProperty(1000);
    private final DoubleProperty y = new SimpleDoubleProperty(1000);
    private final ObjectProperty<Image> currentImage = new SimpleObjectProperty<>();

    private Image[] frontFrames, backFrames, leftFrames, rightFrames;
    private int currentFrame = 0;
    private Timeline timeline;

    private void loadFrames() {
        normalSprite = new Image(getClass()
                .getResource("/org/example/television_problem/view/assets/guest_sprite.png").toExternalForm());

        frontFrames = new Image[] {
                new Image(getClass().getResource("/org/example/television_problem/view/assets/sprites/back_1.png")
                        .toExternalForm()),
                new Image(getClass().getResource("/org/example/television_problem/view/assets/sprites/back_2.png")
                        .toExternalForm()),
                new Image(getClass().getResource("/org/example/television_problem/view/assets/sprites/back_3.png")
                        .toExternalForm())
        };

        backFrames = new Image[] {
                new Image(getClass().getResource("/org/example/television_problem/view/assets/sprites/front_1.png")
                        .toExternalForm()),
                new Image(getClass().getResource("/org/example/television_problem/view/assets/sprites/front_2.png")
                        .toExternalForm()),
                new Image(getClass().getResource("/org/example/television_problem/view/assets/sprites/front_3.png")
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

    // Método de movimentação individual de Guests
    public void moveGuest(int guestId, String direction, int distance, Runnable onFinish) {
        for (Guest guest : guests) {
            if (guest.getGuestId() == guestId) {
                System.out.println("moving: " + guestId);
                switch (direction) {
                    case "UP":
                        moveUp(guest, distance, onFinish);
                        break;
                    case "DOWN":
                        moveDown(guest, distance, onFinish);
                        break;
                    case "LEFT":
                        moveLeft(guest, distance, onFinish);
                        break;
                    case "RIGHT":
                        moveRight(guest, distance, onFinish);
                        break;
                }
                break; // Encerra o loop após encontrar o guest
            }
        }
    }

    // Método que move para a direita com o onFinish
    private void moveRight(Guest guest, int distance, Runnable onFinish) {
        startAnimation(rightFrames, 5, 0, guest, distance, onFinish);
    }

    // Métodos de movimentação para outras direções (para cima, para baixo, para a
    // esquerda)
    private void moveUp(Guest guest, int distance, Runnable onFinish) {
        startAnimation(frontFrames, 0, -5, guest, distance, onFinish);
    }

    private void moveDown(Guest guest, int distance, Runnable onFinish) {
        startAnimation(backFrames, 0, 5, guest, distance, onFinish);
    }

    private void moveLeft(Guest guest, int distance, Runnable onFinish) {
        startAnimation(leftFrames, -5, 0, guest, distance, onFinish);
    }

    private void startAnimation(Image[] frames, int deltaX, int deltaY, Guest guest, int distance, Runnable onFinish) {
        AtomicInteger distanceAnimation = new AtomicInteger(distance); // Usando AtomicInteger

        Timeline guestTimeline = new Timeline();
        guestTimeline.setCycleCount(Timeline.INDEFINITE);

        guestTimeline.getKeyFrames().setAll(
                new KeyFrame(Duration.millis(10), e -> {
                    if (distanceAnimation.get() <= 0) {
                        stopAnimation(guest, guestTimeline);
                        if (onFinish != null) {
                            onFinish.run(); // Executa o onFinish quando a animação terminar
                        }
                    }

                    currentFrame = (currentFrame + 1) % frames.length;
                    guest.setImage(frames[currentFrame]); // Atualiza a imagem do guest específico
                    guest.setPositionX(guest.getPositionX() + deltaX); // Atualiza a posição X do guest
                    guest.setPositionY(guest.getPositionY() + deltaY); // Atualiza a posição Y do guest

                    // Atualiza a distância da animação
                    if (deltaX != 0) {
                        distanceAnimation.addAndGet(-Math.abs(deltaX)); // Subtrai a distância da animação
                    } else {
                        distanceAnimation.addAndGet(-Math.abs(deltaY)); // Subtrai a distância da animação
                    }
                }));

        guestTimeline.play(); // Inicia a animação
    }

    // Método para parar a animação e restaurar a imagem original
    private void stopAnimation(Guest guest, Timeline timeline) {
        timeline.stop();
        guest.setImage(normalSprite); // Coloca o sprite normal do guest
    }

    public void setImage(Guest guest, GuestSprite sprite) {
        System.out.println("patH: " + sprite.getPath());
        guest.setImage(new Image(getClass()
                .getResource(sprite.getPath()).toExternalForm())); // Sprite
                                                                   // //
                                                                   // normal
    }

    private int lastId = 0;

    public void openGuestForm() {
        Platform.runLater(() -> {
            PopupUtil.showFormPopup((name, channel, ttv, td) -> {
                System.out.println("Formulário enviado!");
                System.out.println("Nome: " + (name.isEmpty() ? "Não informado" : name));
                System.out.println("Canal: " + channel);
                System.out.println("Tempo Assistindo TV (Ttv): " + ttv + " segundos");
                System.out.println("Tempo Descansando (Td): " + td + " segundos");

                // Cria o hóspede e inicia sua thread
                addNewGuest(lastId++, channel, ttv, td);
            });
        });
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public DoubleProperty yProperty() {
        return y;
    }

    public ObjectProperty<Image> currentImageProperty() {
        return currentImage;
    }

    // Método para adicionar um novo hóspede dinamicamente
    public void addNewGuest(int id, int channel, int watchTime, int restTime) {
        Guest guest = new Guest(id, channel, watchTime, restTime, this, GuestStatus.WAITING);
        guests.add(guest);
        guest.start();
    }

    public ListProperty<Guest> guestsProperty() {
        return guests;
    }

    public int getSpectators() {
        return this.spectators;
    }

    public void increaseSpectators() {
        spectators++;
    }

    public void decreaseSpectators() {
        spectators--;
    }

    public void setActuallyChannel(int channel) {
        this.actuallyChannel = channel;
    }

    public int getActuallyChannel() {
        return this.actuallyChannel;
    }
}
