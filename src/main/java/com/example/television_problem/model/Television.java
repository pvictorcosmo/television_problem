package com.example.television_problem.model;

public class Television {
    public TelevisionStatus status;
    public int channel;
    public Television(int channel, TelevisionStatus status) {
        this.status = status;
        this.channel = channel;
    }
}
