package com.example.luckydragon.Views;

import android.view.View;
import android.widget.TextView;

import com.example.luckydragon.Fragments.OrganizerEventFragment;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.Observable;
import com.example.luckydragon.R;

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
        organizerEventFragment.displayInviteelist();
        organizerEventFragment.displayWaitlist();
        organizerEventFragment.displayCancelledlist();
    }

    @Override
    public Event getObservable() {
        return (Event) super.getObservable();
    }
}
