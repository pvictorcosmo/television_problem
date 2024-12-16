package org.example.television_problem.model;

import org.example.television_problem.view_model.MainViewModel;
import org.example.television_problem.LogManager;
import org.example.television_problem.Utils;
import javafx.application.Platform;

import java.util.concurrent.Semaphore;

public class Guest extends Thread {
    private final int id;
    private final int favoriteChannel;
    private final int watchTime; // em segundos
    private final int restTime; // em segundos
    private final MainViewModel mainViewModel;
    public static  Semaphore mutexChannelSemaphore = new Semaphore(1);
    public static  Semaphore favoriteChannelSemaphore = new Semaphore(1);
    public static Semaphore tvOfflineSemaphore = new Semaphore(1);

    private GuestStatus status;

    public Guest(int id, int channel, int watchTime, int restTime, MainViewModel mainViewModel,
            GuestStatus status) {
        this.id = id;
        this.favoriteChannel = channel;
        this.watchTime = watchTime;
        this.restTime = restTime;
        this.mainViewModel = mainViewModel;
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

            if (mainViewModel.getActuallyChannel() == -1) {
                mainViewModel.setActuallyChannel(favoriteChannel);
                try {
                    tvOfflineSemaphore.acquire();
                    if(favoriteChannelSemaphore.availablePermits()!=0){
                        favoriteChannelSemaphore.acquire();
                    };
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            // Verificar se o canal é o favorito
            if (mainViewModel.getActuallyChannel() == favoriteChannel) {
                mainViewModel.increaseSpectators();
                mutexChannelSemaphore.release();
                // Platform.runLater(() -> {

                // this.status = GuestStatus.WATCHING;
                // mainViewModel.addSquare(id);
                // });
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
                mainViewModel.decreaseSpectators();

                // Se não houver mais espectadores, liberar o canal
                if (mainViewModel.getSpectators() == 0) {
                    mainViewModel.setActuallyChannel(-1);
                    tvOfflineSemaphore.release();
                    favoriteChannelSemaphore.release();
                }

                mutexChannelSemaphore.release();
                // Platform.runLater(() -> {
                // this.status = GuestStatus.WAITING;
                // mainViewModel.removeSquare(id); // Mostrar o quadrado com o ID
                // });
                Utils.timeCpuBound(restTime, () -> {
                    System.out.println("Hóspede " + id + " agora está: " + status);

                });
                System.out.println("Time's up!");

            } else {
                // Liberar canal se não for o favorito
                mutexChannelSemaphore.release();
                try{
                    favoriteChannelSemaphore.acquire();
                }catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }

        }
    }
}
