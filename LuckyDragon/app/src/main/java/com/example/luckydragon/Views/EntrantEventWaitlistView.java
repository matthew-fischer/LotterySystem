package com.example.luckydragon.Views;

import com.example.luckydragon.Fragments.EntrantEventWaitlistFragment;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.Observable;

/**
 * This is the view that updates EntrantEventFragment.
 * When the event is modified, the update() method is triggered.
 * This updates the information displayed by EntrantEventFragment.
 */
public class EntrantEventWaitlistView extends Observer {
    private final EntrantEventWaitlistFragment entrantEventWaitlistFragment;

    /**
     * Creates an EntrantEventView.
     * @param event the event being displayed
     * @param entrantEventWaitlistFragment the fragment displaying the event
     */
    public EntrantEventWaitlistView(Event event, EntrantEventWaitlistFragment entrantEventWaitlistFragment) {
        this.entrantEventWaitlistFragment = entrantEventWaitlistFragment;
        startObserving(event);
    }

    /**
     * Update event information in EntrantEventFragment.
     * Updates the four main textviews (status, currently joined, waitlist spots, attendee spots).
     * Updates the waitlist action button, which allows joining and leaving the waitlist.
     * Updates the geolocation warning message.
     * @param whoUpdatedMe the observable who triggered the update (Event)
     */
    @Override
    public void update(Observable whoUpdatedMe) {
        entrantEventWaitlistFragment.updateStatusMessage();
        entrantEventWaitlistFragment.updateCurrentlyJoinedMessage();
        entrantEventWaitlistFragment.updateWaitlistSpotsMessage();
        entrantEventWaitlistFragment.updateAttendeeSpotsMessage();
        entrantEventWaitlistFragment.updateWaitlistActionButton();
        entrantEventWaitlistFragment.updateGeolocationMessage();
    }

    @Override
    public Event getObservable() {
        return (Event) super.getObservable();
    }
}
