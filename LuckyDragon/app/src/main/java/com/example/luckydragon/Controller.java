package com.example.luckydragon;

import android.widget.Toast;

import java.io.Serializable;

public class Controller {
    private Observable observable;

    public Controller(Observable observable) {
        this.observable = observable;
    }

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
