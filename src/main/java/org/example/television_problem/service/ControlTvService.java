package org.example.television_problem.service;

import java.util.concurrent.Semaphore;

public class ControlTvService {
    private final Semaphore favoriteChannelSemaphore = new Semaphore(1);
    private final Semaphore tvOfflineSemaphore = new Semaphore(1);
    private int actuallyChannel = -1;
    private int spectators;

    public int getSpectators() {
        return this.spectators;
    }
    public void increaseSpectators(){
        spectators++;
    }
    public void decreaseSpectators(){
        spectators--;
    }

    public void acquireFavoriteChannel() throws InterruptedException {
        favoriteChannelSemaphore.acquire();
    }

    public void releaseFavoriteChannel(){
        favoriteChannelSemaphore.release();
    }

    public void acquireTvOffline() throws InterruptedException {
        tvOfflineSemaphore.acquire();
    }

    public void releaseTvOffline(){
        tvOfflineSemaphore.release();
    }

    public void setActuallyChannel(int channel){
        this.actuallyChannel = channel;
    }

    public int getActuallyChannel(){
        return this.actuallyChannel;
    }

}
