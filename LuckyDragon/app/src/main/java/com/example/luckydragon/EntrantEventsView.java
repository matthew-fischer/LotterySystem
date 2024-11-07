package com.example.luckydragon;

/**
 * Observer class that listens for updates to an EventList and
 * refreshes the EntrantProfileFragment UI when changes occur.
 * <p>
 *     The EntrantEventsView class observes an EventList and triggers an update
 *     in EntrantProfileFragment when the list is modified, ensuring that the
 *     event data displayed to user is up-to-date.
 * </p>
 */
public class EntrantEventsView extends Observer {

    private final EntrantProfileFragment entrantProfileFragment;

    /**
     * Constructs a new EntrantEventsView observer and begins observing the EventList.
     * @param events the EventList to observe for changes
     * @param entrantProfileFragment the Activity to notify of updates
     */
    public EntrantEventsView(EventList events, EntrantProfileFragment entrantProfileFragment) {
        this.entrantProfileFragment = entrantProfileFragment;
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
     *     This method notifies the EntrantProfileFragment to refresh the UI, so
     *     any changes in the EventList are displayed.
     * </p>
     * @param whoUpdatedMe the Observable that triggered this update
     */
    @Override
    public void update(Observable whoUpdatedMe) {
        entrantProfileFragment.notifyAdapter();
    }

}
