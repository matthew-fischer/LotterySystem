package com.example.luckydragon.Views;

import com.example.luckydragon.Fragments.OrganizerEventFragment;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.Observable;

/**
 * This is the view that updates OrganizerEventFragment.
 * When the event is modified, the update() method is triggered.
 * This updates the event details in the OrganizerEventFragment.
 */
public class OrganizerEventView extends Observer {
    private final OrganizerEventFragment organizerEventFragment;

    /**
     * Creates an OrganizerEventView.
     * @param event the event being displayed
     * @param organizerEventFragment the fragment displaying the event
     */
    public OrganizerEventView(Event event, OrganizerEventFragment organizerEventFragment) {
        this.organizerEventFragment = organizerEventFragment;
        startObserving(event);
    }

    /**
     * Update event details in OrganizerEventFragment.
     * @param whoUpdatedMe the observable who triggered the update (Event)
     */
    @Override
    public void update(Observable whoUpdatedMe) {
        organizerEventFragment.updateWaitlistCapacity();
    }

    @Override
    public Event getObservable() {
        return (Event) super.getObservable();
    }
}
