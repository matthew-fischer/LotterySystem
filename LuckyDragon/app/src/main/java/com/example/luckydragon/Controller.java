package com.example.luckydragon;

public class Controller {
    private final Observable observable;

    public Controller(Observable observable) {
        this.observable = observable;
    }

    public Observable getObservable() {
        return observable;
    }
}
