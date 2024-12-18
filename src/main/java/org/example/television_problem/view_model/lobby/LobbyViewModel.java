package org.example.television_problem.view_model.lobby;

import java.util.ArrayList;
import java.util.List;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        // Guest guest1 = new Guest(1, 1, 10, 10, this, GuestStatus.BLOCKED);
        // guest1.start();
        // Guest guest2 = new Guest(2, 2, 10, 10, this, GuestStatus.BLOCKED);
        // guest2.start();
        // Guest guest3 = new Guest(3, 3, 10, 10, this, GuestStatus.BLOCKED);
        // guest3.start();
        // guests.add(guest1);
        // guests.add(guest2);
        // guests.add(guest3);
        loadFrames();
        currentImage.set(frontFrames[0]); // Imagem inicial
        initTimeline();
        double sofaStartX = 140; // Posição inicial X do sofá
        double sofaWidth = 220; // Largura total do sofá
        initializeSofaPositions(sofaStartX, sofaWidth);
    }

    public ListProperty<Guest> getGuests() {
        return this.guests;
    }

    private Timeline waitingAnimationTimeline;

    public void initWaitingAnimation(Guest guest) {
        if (waitingAnimationTimeline != null && waitingAnimationTimeline.getStatus() == Timeline.Status.RUNNING) {
            return; // Já está rodando
        }

        Image guestSprite = new Image(getClass()
                .getResource("/org/example/television_problem/view/assets/sprites/front_1.png").toExternalForm());
        Image jumpingSprite = new Image(getClass()
                .getResource("/org/example/television_problem/view/assets/sprites/front_2.png").toExternalForm());

        waitingAnimationTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            // Alterna o sprite apenas para o hóspede especificado
            if (guest.getImage().equals(guestSprite)) {
                guest.setImage(jumpingSprite);
            } else {
                guest.setImage(guestSprite);
            }
        }));

        waitingAnimationTimeline.setCycleCount(Timeline.INDEFINITE);
        waitingAnimationTimeline.play();
    }

    // Parar a animação de espera para um Guest específico
    public void stopWaitingAnimation(Guest guest) {
        if (waitingAnimationTimeline != null) {
            waitingAnimationTimeline.stop();
            waitingAnimationTimeline = null;

            // Restaura o sprite normal apenas para o hóspede especificado
            guest.setImage(normalSprite);
        }
    }

    private final StringProperty currentImagePath = new SimpleStringProperty();

    public StringProperty currentImagePathProperty() {
        return currentImagePath;
    }

    public void updateChannel(int channel) {
        String path = "";
        if (channel != -1) {
            path = "/org/example/television_problem/view/assets/canal" + channel + ".png";
        } else {
            path = "/org/example/television_problem/view/assets/tv.png";
        }

        currentImagePath.set(path);
    }

    private final DoubleProperty x = new SimpleDoubleProperty(1000);
    private final DoubleProperty y = new SimpleDoubleProperty(1000);
    private final ObjectProperty<Image> currentImage = new SimpleObjectProperty<>();
    private static final double GUEST_WIDTH = 60; // Largura do Guest
    private Image[] frontFrames, backFrames, leftFrames, rightFrames;
    private int currentFrame = 0;
    private Timeline timeline;
    private List<Double> sofaPositionsX; // Lista para armazenar posições X no sofá
    private final double SOFA_Y = 200; // Posição fixa Y do sofá
    private final double SOFA_SPACING = 10; // Espaçamento entre Guests no sofá

    // Método de inicialização das posições no sofá
    public void initializeSofaPositions(double sofaStartX, double sofaWidth) {
        sofaPositionsX = new ArrayList<>();
        double currentX = sofaStartX;

        // Calcular posições disponíveis no sofá
        while (currentX + GUEST_WIDTH <= sofaStartX + sofaWidth) {
            sofaPositionsX.add(currentX);
            currentX += GUEST_WIDTH + SOFA_SPACING; // Próxima posição com espaçamento
        }
    }

    private final StringProperty logProperty = new SimpleStringProperty();

    public StringProperty logProperty() {
        return logProperty;
    }

    // Método para adicionar uma nova mensagem ao log
    public void logEvent(String message) {
        // Adiciona uma nova mensagem ao log

        logProperty.set(message + "\n");
    }

    // Método para mover o Guest para o sofá
    public void moveGuestToSofa(int guestId, Runnable onFinish) {
        for (Guest guest : guests) {
            if (guest.getGuestId() == guestId) {
                if (sofaPositionsX.isEmpty()) {
                    System.out.println("Sem posições livres no sofá!");
                    return; // Sem posições disponíveis
                }

                // Pegar a primeira posição livre
                double targetX = sofaPositionsX.remove(0);

                moveGuestToBed(guestId, targetX, SOFA_Y, () -> {
                    guest.setPositionX(targetX); // Atualiza posição X
                    guest.setPositionY(SOFA_Y); // Atualiza posição Y
                    System.out.println("Guest " + guestId + " chegou ao sofá na posição X: " + targetX);
                    setImage(guest, GuestSprite.BACK2);

                    if (onFinish != null) {
                        onFinish.run();
                    }
                });
                break;
            }
        }
    }

    public void moveGuestToBed(int guestId, double bedX, double bedY, Runnable onFinish) {
        for (Guest guest : guests) {
            if (guest.getGuestId() == guestId) {
                // Pegar posição atual do Guest
                double currentX = guest.getPositionX();
                double currentY = guest.getPositionY();

                // Calcular distâncias
                double deltaX = bedX - currentX;
                double deltaY = bedY - currentY;

                // Determinar direções
                String horizontalDirection = deltaX > 0 ? "RIGHT" : "LEFT";
                String verticalDirection = deltaY > 0 ? "DOWN" : "UP";

                // Distâncias absolutas
                int distanceX = (int) Math.abs(deltaX);
                int distanceY = (int) Math.abs(deltaY);

                // Primeiro movimento horizontal, depois vertical
                moveGuest(guestId, horizontalDirection, distanceX, () -> {
                    // Atualiza a posição X após movimento horizontal
                    guest.setPositionX(bedX);

                    moveGuest(guestId, verticalDirection, distanceY, () -> {
                        // Atualiza a posição Y após movimento vertical
                        guest.setPositionY(bedY);

                        if (onFinish != null) {

                            onFinish.run(); // Callback final

                        }
                    });
                });

                break;
            }
        }
    }

    // public void moveGuestToCouch(int guestId, double couchX, double couchY,
    // Runnable onFinish) {
    // for (Guest guest : guests) {
    // if (guest.getGuestId() == guestId) {
    // // Calcular distâncias
    // double currentX = guest.getPositionX();
    // double currentY = guest.getPositionY();
    // double deltaX = bedX - currentX;
    // double deltaY = bedY - currentY;

    // // Determinar direções
    // String horizontalDirection = deltaX > 0 ? "RIGHT" : "LEFT";
    // String verticalDirection = deltaY > 0 ? "DOWN" : "UP";

    // // Distâncias absolutas
    // int distanceX = (int) Math.abs(deltaX);
    // int distanceY = (int) Math.abs(deltaY);

    // // Primeiro movimento horizontal, depois vertical
    // moveGuest(guestId, horizontalDirection, distanceX, () -> {
    // moveGuest(guestId, verticalDirection, distanceY, onFinish);
    // });
    // break;
    // }
    // }
    // }

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

    }

    private int lastId = 1;

    public void openGuestForm() {
        Platform.runLater(() -> {
            PopupUtil.showFormPopup((channel, ttv, td) -> {
                System.out.println("Formulário enviado!");
                System.out.println("Canal: " + channel);
                System.out.println("Tempo Assistindo TV (Ttv): " + ttv + " segundos");
                System.out.println("Tempo Descansando (Td): " + td + " segundos");

                // Cria o hóspede e inicia sua thread
                addNewGuest(lastId++, channel, ttv, td);
            });
        });
    }

    public ObjectProperty<Image> tvImage = new SimpleObjectProperty<>();

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
        Guest guest = new Guest(id, channel, watchTime, restTime, this, GuestStatus.BLOCKED);
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

    public ObjectProperty<Image> tvImageProperty() {
        return tvImage;
    }

    public int getActuallyChannel() {
        return this.actuallyChannel;
    }
}
