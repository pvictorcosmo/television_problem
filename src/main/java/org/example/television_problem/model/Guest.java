package org.example.television_problem.model;

import org.example.television_problem.service.ControlTvService;
import org.example.television_problem.LogManager;
import org.example.television_problem.Utils;
import javafx.application.Platform;

import java.util.concurrent.Semaphore;

public class Guest extends Thread {
    private final int id;
    private final int favoriteChannel;
    private final int watchTime; // em segundos
    private final int restTime; // em segundos
    private final ControlTvService controlTvService;
    private final Semaphore mutexChannelSemaphore = new Semaphore(1);
    private GuestStatus status;

    public Guest(int id, int channel, int watchTime, int restTime, ControlTvService controlTvService,
            GuestStatus status) {
        this.id = id;
        this.favoriteChannel = channel;
        this.watchTime = watchTime;
        this.restTime = restTime;
        this.controlTvService = controlTvService;
        this.status = status;
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
                mutexChannelSemaphore.acquire();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // LogManager.logGuestStateChange(id, "");

            if (controlTvService.getActuallyChannel() == -1) {
                controlTvService.setActuallyChannel(favoriteChannel);
                try {
                    controlTvService.acquireTvOffline();
                    controlTvService.acquireFavoriteChannel();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            // Verificar se o canal é o favorito
            if (controlTvService.getActuallyChannel() == favoriteChannel) {
                controlTvService.increaseSpectators();
                mutexChannelSemaphore.release();
                Platform.runLater(() -> {

                    this.status = GuestStatus.WATCHING;
                    controlTvService.showSquare(id);
                });
                Utils.timeCpuBound(watchTime, () -> {
                    System.out.println("Hóspede " + id + " agora está: " + status);

                });
                System.out.println("Time's up!");

                // Após o watch time, reduzir espectadores
                try {
                    mutexChannelSemaphore.acquire();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                controlTvService.decreaseSpectators();

                // Se não houver mais espectadores, liberar o canal
                if (controlTvService.getSpectators() == 0) {
                    controlTvService.setActuallyChannel(-1);
                    controlTvService.releaseTvOffline();
                    controlTvService.releaseFavoriteChannel();
                }

                mutexChannelSemaphore.release();
                Platform.runLater(() -> {
                    this.status = GuestStatus.WAITING;
                    controlTvService.hideSquare(id); // Mostrar o quadrado com o ID
                });
                Utils.timeCpuBound(restTime, () -> {
                    System.out.println("Hóspede " + id + " agora está: " + status);

                });
                System.out.println("Time's up!");

            } else {
                // Liberar canal se não for o favorito
                mutexChannelSemaphore.release();
                controlTvService.releaseFavoriteChannel();
            }

        }
    }
}
