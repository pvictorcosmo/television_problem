package org.example.television_problem.service;

import java.util.concurrent.Semaphore;

import org.example.television_problem.view_model.MainViewModel;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ControlTvService {
    private final MainViewModel viewModel;

    private final Semaphore favoriteChannelSemaphore = new Semaphore(1);
    private final Semaphore tvOfflineSemaphore = new Semaphore(1);
    private int actuallyChannel = -1;
    private int spectators;

    public ControlTvService(MainViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void showSquare(int id) {
        // Chama o método do ViewModel para adicionar um quadrado
        viewModel.addSquare(id);
    }

    public void hideSquare(int id) {
        // Chama o método do ViewModel para remover o quadrado
        viewModel.removeSquare(id);
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

    public void acquireFavoriteChannel() throws InterruptedException {
        favoriteChannelSemaphore.acquire();
    }

    public void releaseFavoriteChannel() {
        favoriteChannelSemaphore.release();
    }

    public void acquireTvOffline() throws InterruptedException {
        tvOfflineSemaphore.acquire();
    }

    public void releaseTvOffline() {
        tvOfflineSemaphore.release();
    }

    public void setActuallyChannel(int channel) {
        this.actuallyChannel = channel;
    }

    public int getActuallyChannel() {
        return this.actuallyChannel;
    }

}
