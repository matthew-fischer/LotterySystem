package com.example.luckydragon.Controllers;

import com.example.luckydragon.Models.Observable;

/**
 * This is the Controller class. Part of the MVC implementation.
 */
public class Controller {
    private Observable observable;

    /**
     * Constructor for the Controller class. Assigns a observable is its own.
     * @param observable
     */
    public Controller(Observable observable) {
        this.observable = observable;
    }

    /**
     * Gets the Observable this controller owns.
     * @return Observable.
     */
    public Observable getObservable() {
        return observable;
    }

    /**
     * Set the observable to a new object.
     * This is used for testing purposes.
     * If a testing controller is passed into an activity, we need to set its observable to the observable used in the activity.
     * @param observable the new observable
     */
    public void setObservable(Observable observable) {
        this.observable = observable;
    }

}
