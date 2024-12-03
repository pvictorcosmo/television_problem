package org.example.television_problem.service;

import java.util.concurrent.Semaphore;

import org.example.television_problem.view_model.MainViewModel;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ControlTvService {
    public MainViewModel viewModel;
    private static ControlTvService instance;

    private final Semaphore favoriteChannelSemaphore = new Semaphore(1);
    private final Semaphore tvOfflineSemaphore = new Semaphore(1);
    private int actuallyChannel = -1;
    private int spectators;

    private ControlTvService(MainViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public static ControlTvService getInstance(MainViewModel viewModel) {
        if (instance == null) {
            instance = new ControlTvService(viewModel);
        }
        return instance;
    }

    public void showSquare(int id) {

        viewModel.addSquare(id);
    }

    public void hideSquare(int id) {

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
