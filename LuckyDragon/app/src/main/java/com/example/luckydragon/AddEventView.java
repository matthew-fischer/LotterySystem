package com.example.luckydragon;

import android.widget.TextView;

public class AddEventView extends Observer {
    private TextView eventNameView;
    private TextView facilityNameView;
    private TextView dateAndTimeView;
    private TextView waitlistSpotsView;
    private TextView attendeeSpotsView;
    private TextView currentlyJoinedView;

    private String deviceId;

    public AddEventView(
            Event event,
            String deviceId,
            TextView eventNameView,
            TextView facilityNameView,
            TextView dateAndTimeView,
            TextView waitlistSpotsView,
            TextView attendeeSpotsView,
            TextView currentlyJoinedView
    ) {
        this.eventNameView = eventNameView;
        this.facilityNameView = facilityNameView;
        this.dateAndTimeView = dateAndTimeView;
        this.waitlistSpotsView = waitlistSpotsView;
        this.attendeeSpotsView = attendeeSpotsView;
        this.currentlyJoinedView = currentlyJoinedView;
        startObserving(event);
    }

    @Override
    public Event getObservable() {
        return (Event) super.getObservable();
    }

    @Override
    public void update(Observable whoUpdatedMe) {
        eventNameView.setText(getObservable().getName());
        facilityNameView.setText(getObservable().getFacility());
        dateAndTimeView.setText(getObservable().getDateAndTime());
        waitlistSpotsView.setText(String.format("Waitlist Spots: %s", getObservable().getWaitListSpots()));
        attendeeSpotsView.setText(String.format("Attendee Spots: %s", getObservable().getAttendeeSpots()));
        currentlyJoinedView.setText(String.format("Currently Joined: %s", getObservable().getCurrentlyJoined()));
        // Switch mode to waitlist view
        if (getObservable().onWaitList(deviceId)) {
            // TODO:
        }
        // Default mode is signup view
    }
}
