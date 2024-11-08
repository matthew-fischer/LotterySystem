package com.example.luckydragon.Views;

import com.example.luckydragon.Activities.ViewEventsActivity;
import com.example.luckydragon.Models.EventList;
import com.example.luckydragon.Models.Observable;

/**
 * Observer class that listens for updates to an EventList and
 * refreshes the ViewEventsActivity UI when changes occur.
 * <p>
 *     The ViewEventsView class observes an EventList and triggers an update
 *     in ViewEventsActivity when the list is modified, ensuring that the
 *     event data displayed to user is up-to-date.
 * </p>
 */
public class ViewEventsView extends Observer {

    private final ViewEventsActivity viewEventsActivity;

    /**
     * Constructs a new ViewEventsView observer and begins observing the EventList.
     * @param events the EventList to observe for changes
     * @param viewEventsActivity the Activity to notify of updates
     */
    public ViewEventsView(EventList events, ViewEventsActivity viewEventsActivity) {
        this.viewEventsActivity = viewEventsActivity;
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
     *     This method notifies the ViewEventsActivity to refresh the UI, so
     *     any changes in the EventList are displayed.
     * </p>
     * @param whoUpdatedMe the Observable that triggered this update
     */
    @Override
    public void update(Observable whoUpdatedMe) {

        viewEventsActivity.notifyAdapter();

    }

}
