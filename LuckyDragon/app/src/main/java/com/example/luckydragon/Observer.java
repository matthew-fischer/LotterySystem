package com.example.luckydragon;

/**
 * Abstract class where Observers keep track of changes in the Observable.
 */
public abstract class Observer {
    public transient Observable observable;

    /**
     * Begins observing an Observable.
     * @param observable
     * @throws RuntimeException, observer cannot start observing a Observable that is null.
     */
    public void startObserving(Observable observable) {
        if (this.observable != null) {
            throw new RuntimeException("Can't view two models");
        }
        this.observable = observable;
        observable.addObserver(this);
    }

    /**
     * Stops observing an Observable. Makes sure the Observable exists first.
     */
    public void stopObserving() {
        if (observable != null) {
            observable.removeObserver(this);
            this.observable = null;
        }
    }

    /**
     * Returns the Observable that the Observer is currently observing.
     * @return Observable.
     */
    public Observable getObservable() {
        return observable;
    }

    /**
     * Updates the Observer with changes of the Observable being observed.
     * @param whoUpdatedMe
     */
    public abstract void update(Observable whoUpdatedMe);
}