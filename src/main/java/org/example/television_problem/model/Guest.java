package org.example.television_problem.model;

import org.example.television_problem.service.ControlTvService;

import java.util.concurrent.Semaphore;

class Guest extends Thread {
    private final int id;
    private final int favoriteChannel;
    private final int watchTime; // in seconds
    private final int restTime; // in seconds
    private final ControlTvService controlTvService;
    private final GuestStatus status;
    private final Semaphore mutexChannelSemaphore = new Semaphore(1);

    public Guest(int id, int channel, int watchTime, int restTime, ControlTvService controlTvService,GuestStatus status) {
        this.id = id;
        this.favoriteChannel = channel;
        this.watchTime = watchTime;
        this.restTime = restTime;
        this.controlTvService = controlTvService;
        this.status = status;
    }
    @Override
    public void run() {
        while (true) {
            try {
                mutexChannelSemaphore.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(controlTvService.getActuallyChannel() == -1) {
                controlTvService.setActuallyChannel(favoriteChannel);
                try {
                    controlTvService.acquireTvOffline();
                    controlTvService.acquireFavoriteChannel();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            if(controlTvService.getActuallyChannel() == favoriteChannel) {
                controlTvService.increaseSpectators();
                mutexChannelSemaphore.release();
                //loop de tempo de tv e movimentação ( chamar função do serviço para chamar o view_model
                try {
                    mutexChannelSemaphore.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                controlTvService.decreaseSpectators();
                if(controlTvService.getSpectators() == 0 ){
                    controlTvService.setActuallyChannel(-1);
                    controlTvService.releaseTvOffline();
                    controlTvService.releaseFavoriteChannel();;
                }
                mutexChannelSemaphore.release();
                // descansar

            }else {
                mutexChannelSemaphore.release();
                controlTvService.releaseFavoriteChannel();

            }


        }
    }
}
