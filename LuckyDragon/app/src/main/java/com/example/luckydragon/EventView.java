package com.example.luckydragon;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class EventView extends Observer {
    private TextView eventNameView;
    private TextView facilityNameView;
    private TextView dateAndTimeView;
    private TextView waitlistSpotsView;
    private TextView attendeeSpotsView;
    private TextView currentlyJoinedView;
    private Button signUpButton;

    private String deviceId;

    public EventView(
            Event event,
            String deviceId,
            TextView eventNameView,
            TextView facilityNameView,
            TextView dateAndTimeView,
            TextView waitlistSpotsView,
            TextView attendeeSpotsView,
            TextView currentlyJoinedView,
            Button signUpButton
    ) {
        this.deviceId = deviceId;

        this.eventNameView = eventNameView;
        this.facilityNameView = facilityNameView;
        this.dateAndTimeView = dateAndTimeView;
        this.waitlistSpotsView = waitlistSpotsView;
        this.attendeeSpotsView = attendeeSpotsView;
        this.currentlyJoinedView = currentlyJoinedView;
        this.signUpButton = signUpButton;

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
            signUpButton.setVisibility(TextView.GONE);
        }
        // Default mode is signup view
    }
}
