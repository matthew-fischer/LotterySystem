package com.example.luckydragon;

import android.util.ArraySet;

import java.util.Set;

public abstract class Observable {
    private final transient Set<Observer> observers;

    protected Observable() {
        observers = new ArraySet<>();
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }
}
