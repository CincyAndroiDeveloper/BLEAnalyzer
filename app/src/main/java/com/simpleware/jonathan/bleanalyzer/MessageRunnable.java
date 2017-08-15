package com.simpleware.jonathan.bleanalyzer;

public abstract class MessageRunnable implements Runnable {

    public String message;

    public Runnable setMessage(String message) {
        this.message =  message;
        return this;
    }
}