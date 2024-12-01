package com.example.luckydragon.Views;

import android.view.View;

import com.example.luckydragon.Fragments.AdminEventFragment;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.Observable;

/**
 * Observer class that listens for updates to an Event and
 * refreshes the AdminBrowseEventFragment UI when changes occur.
 * <p>
 *     The AdminBrowseEventView class observes an Event and triggers an
 *     update in AdminEventFragment when the event is modified, ensuring that
 *     the event data is displayed to user up-to-date.
 * </p>
 */
public class AdminBrowseEventView extends Observer{

    private final AdminEventFragment adminEventFragment;

    /**
     * Constructs a new AdminBrowseEventView observer and begins observing the Event.
     * @param event the event to observe for changes
     * @param adminEventFragment the fragment to notify of updates
     */
    public AdminBrowseEventView(Event event, AdminEventFragment adminEventFragment) {
        this.adminEventFragment = adminEventFragment;
        startObserving(event);
    }

    @Override
    public Event getObservable() {
        return (Event) super.getObservable();
    }

    @Override
    public void update(Observable whoUpdatedMe) {

        adminEventFragment.updateCurrentlyJoinedMessage();
        adminEventFragment.updateWaitlistSpotsMessage();
        adminEventFragment.updateAttendeeSpotsMessage();

        if (getObservable().getQrHash() == null) {
            adminEventFragment.setQrCodeButtonVisibility(View.GONE);
        }
        else {
            adminEventFragment.setQrCodeButtonVisibility(View.VISIBLE);
        }
        if (getObservable().getEventPoster() == null) {
            adminEventFragment.setRemoveEventPosterButtonVisibility(View.GONE);
        }
        else {
            adminEventFragment.setRemoveEventPosterButtonVisibility(View.VISIBLE);
        }
    }
}
