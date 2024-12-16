package org.example.television_problem.model;

import org.example.television_problem.view_model.MainViewModel;
import org.example.television_problem.view_model.lobby.LobbyViewModel;
import org.example.television_problem.LogManager;
import org.example.television_problem.Utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.concurrent.Semaphore;

public class Guest extends Thread {
    private final int id;
    private final int favoriteChannel;
    private final int watchTime; // em segundos
    private final int restTime; // em segundos
    private final MainViewModel mainViewModel;
    private final LobbyViewModel lobbyViewModel;
    private DoubleProperty positionX = new SimpleDoubleProperty(100);
    private DoubleProperty positionY = new SimpleDoubleProperty(100);
    private ObjectProperty<Image> image = new SimpleObjectProperty<>();
    private GuestStatus status;

    public Guest(int id, int channel, int watchTime, int restTime, LobbyViewModel lobbyViewModel,
            GuestStatus status) {
        this.id = id;
        this.favoriteChannel = channel;
        this.watchTime = watchTime;
        this.restTime = restTime;
        this.lobbyViewModel = lobbyViewModel;
        this.status = status;
    }

    public ObjectProperty<Image> imageProperty() {
        return image;
    }

    public void setImage(Image image) {
        this.image.set(image);
    }

    public Image getImage() {
        return image.get();
    }

    public double getPositionX() {
        return positionX.get();
    }

    public void setPositionX(double x) {
        this.positionX.set(x);
    }

    public DoubleProperty positionXProperty() {
        return positionX;
    }

    public double getPositionY() {
        return positionY.get();
    }

    public void setPositionY(double y) {
        this.positionY.set(y);
    }

    public DoubleProperty positionYProperty() {
        return positionY;
    }

    public int getGuestId() {
        return this.id;
    }

    public int getFavoriteChannel() {
        return this.favoriteChannel;
    }

    @Override
    public void run() {
        while (true) {

            try {
                MainViewModel.mutexChannelSemaphore.acquire();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // LogManager.logGuestStateChange(id, "");

            if (lobbyViewModel.getActuallyChannel() == -1) {
                lobbyViewModel.setActuallyChannel(favoriteChannel);
                try {
                    MainViewModel.tvOfflineSemaphore.acquire();
                    if (MainViewModel.favoriteChannelSemaphore.availablePermits() != 0) {
                        MainViewModel.favoriteChannelSemaphore.acquire();
                    }
                    ;
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            // Verificar se o canal é o favorito
            if (lobbyViewModel.getActuallyChannel() == favoriteChannel) {
                lobbyViewModel.increaseSpectators();
                MainViewModel.mutexChannelSemaphore.release();
                System.out.println("Status Thread: " + this.status);
                if (this.status == GuestStatus.BLOCKED) {
                    Platform.runLater(() -> {
                        this.status = GuestStatus.WATCHING;
                        // Movimento para a direita com o Runnable para movimentação para baixo depois
                        lobbyViewModel.moveGuest(id, "LEFT", 600, () -> {

                        });

                        // lobbyViewModel.addSquare(id);
                    });
                } else if (this.status == GuestStatus.WAITING) {
                    Platform.runLater(() -> {
                        this.status = GuestStatus.WATCHING;
                        lobbyViewModel.moveGuest(id, "UP", 300, () -> {
                            lobbyViewModel.moveGuest(id, "LEFT", 300, () -> {

                            });
                        });
                    });
                }
                Utils.timeCpuBound(watchTime, () -> {
                    lobbyViewModel.setImage(this, GuestSprite.BACK2);
                    System.out.println("Hóspede " + id + " agora está: " + status);

                });
                System.out.println("Time's up!");

                try {
                    MainViewModel.mutexChannelSemaphore.acquire();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                lobbyViewModel.decreaseSpectators();

                // Se não houver mais espectadores, liberar o canal
                if (lobbyViewModel.getSpectators() == 0) {
                    lobbyViewModel.setActuallyChannel(-1);
                    MainViewModel.tvOfflineSemaphore.release();
                    MainViewModel.favoriteChannelSemaphore.release();
                }

                MainViewModel.mutexChannelSemaphore.release();
                Platform.runLater(() -> {
                    this.status = GuestStatus.WAITING;

                    // Movimento para a direita com o Runnable para movimentação para baixo depois
                    lobbyViewModel.moveGuest(id, "RIGHT", 300, () -> {
                        // Quando o movimento para a direita terminar, move para baixo
                        lobbyViewModel.moveGuest(id, "DOWN", 300, () -> {
                            // Aqui você pode adicionar mais movimentos, se necessário
                            System.out.println("Hóspede " + id + " terminou de se mover para baixo");
                        });
                    });
                });
                Utils.timeCpuBound(restTime, () -> {
                    lobbyViewModel.setImage(this, GuestSprite.BACK2);

                    System.out.println("Hóspede " + id + " agora está: " + status);

                });

            } else {
                // Liberar canal se não for o favorito
                Platform.runLater(() -> {
                    this.status = GuestStatus.BLOCKED;
                    // ir dormir

                });
                MainViewModel.mutexChannelSemaphore.release();
                try {
                    MainViewModel.favoriteChannelSemaphore.acquire();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }
    }

}
