package com.example.television_problem.model;

import com.example.television_problem.services.ControlTvService;

class Guest extends Thread {
    private final int id;
    private final int favoriteChannel;
    private final int watchTime; // in seconds
    private final int restTime; // in seconds
    private final ControlTvService controlTvService;
    private final GuestStatus status;

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


        }
    }
}
