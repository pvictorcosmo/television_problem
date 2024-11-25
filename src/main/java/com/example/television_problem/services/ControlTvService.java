package com.example.television_problem.services;

import java.util.concurrent.Semaphore;

class ControlTvService {
    private final Semaphore control = new Semaphore(1, true);
    private int actuallyChannel = -1;

    public void watchTv(int id,int channel) {

        if(channel == actuallyChannel) {
            control.release();
        }

    }

}
