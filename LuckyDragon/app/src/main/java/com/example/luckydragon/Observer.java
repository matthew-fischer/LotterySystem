package com.example.luckydragon;

public abstract class Observer {
    public transient Observable observable;

    public void startObserving(Observable observable) {
        if (this.observable != null) {
            throw new RuntimeException("Can't view two models");
        }
        this.observable = observable;
        observable.addObserver(this);
    }

    public void stopObserving() {
        observable.removeObserver(this);
        this.observable = null;
    }

    public Observable getObservable() {
        return observable;
    }

    public abstract void update(Observable whoUpdatedMe);
}