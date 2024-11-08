package com.example.luckydragon.Models;

import android.util.ArraySet;

import com.example.luckydragon.Views.Observer;

import java.util.Set;

/**
 * Observable abstract class. Used to implement MVC for application.
 */
public abstract class Observable {
    private final transient Set<Observer> observers;

    /**
     * Constructor for the Observable class. Initializes observes to am empty ArraySet.
     */
    protected Observable() {
        observers = new ArraySet<>();
    }

    /**
     * Adds an observer to this Observable's set of Observers.
     * @param observer
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
        observer.update(this);
    }

    /**
     * Removes an observer from this Observable's set of Observers.
     * @param observer
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers of this Observable by calling observers update function.
     */
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }
}
