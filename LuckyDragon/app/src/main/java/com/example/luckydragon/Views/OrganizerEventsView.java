package com.example.luckydragon.Views;

import com.example.luckydragon.Fragments.OrganizerProfileFragment;
import com.example.luckydragon.Models.EventList;
import com.example.luckydragon.Models.Observable;

/**
 * Observer class that listens for updates to an EventList and
 * refreshes the OrganizerProfileFragment UI when changes occur.
 * <p>
 *     The OrganizerEventsView class observes an EventList and triggers an update
 *     in OrganizerProfileFragment when the list is modified, ensuring that the
 *     organizer's events are displayed up to date.
 * </p>
 */
public class OrganizerEventsView extends Observer {
    private final OrganizerProfileFragment organizerProfileFragment;

    /**
     * Constructs a new OrganizerEventsView observer and begins observing the EventList.
     * @param events the EventList to observe for changes
     * @param organizerProfileFragment the fragment to notify of updates
     */
    public OrganizerEventsView(EventList events, OrganizerProfileFragment organizerProfileFragment) {
        this.organizerProfileFragment = organizerProfileFragment;
        startObserving(events);
    }

    /**
     * Returns the observed EventList.
     * @return observed EventList instance
     */
    @Override
    public EventList getObservable() {
        return (EventList) super.getObservable();
    }

    /**
     * Called when the observed EventList is updated.
     * <p>
     *     This method notifies the organizerProfileFragment to refresh the UI, so
     *     any changes in the EventList are displayed.
     * </p>
     * @param whoUpdatedMe the Observable that triggered this update
     */
    @Override
    public void update(Observable whoUpdatedMe) {
        organizerProfileFragment.notifyAdapter();
    }

}
