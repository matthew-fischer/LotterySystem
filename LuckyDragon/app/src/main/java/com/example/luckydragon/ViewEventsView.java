package com.example.luckydragon;

public class ViewEventsView extends Observer {

    private final ViewEventsActivity viewEventsActivity;

    public ViewEventsView(EventList events, ViewEventsActivity viewEventsActivity) {
        this.viewEventsActivity = viewEventsActivity;
        startObserving(events);
    }

    @Override
    public EventList getObservable() {
        return (EventList) super.getObservable();
    }

    @Override
    public void update(Observable whoUpdatedMe) {

        viewEventsActivity.notifyAdapter();

    }

}
