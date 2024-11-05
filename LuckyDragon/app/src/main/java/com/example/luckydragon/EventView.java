package com.example.luckydragon;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EventView extends Observer {
    private TextView eventNameView;
    private TextView facilityNameView;
    private TextView dateAndTimeView;
    private TextView geolocationWarningView;
    private TextView waitlistSpotsView;
    private TextView attendeeSpotsView;
    private TextView currentlyJoinedView;

    private TextView status;
    private TextView notSelectedText;
    private Button cancel;
    private Button decline;
    private Button accept;
    private Button signUp;
    private Button viewPoster;

    private String deviceId;

    public EventView(Event event, String deviceId, EventActivity eventActivity) {
        this.deviceId = deviceId;

        eventNameView = eventActivity.findViewById(R.id.eventName);
        facilityNameView = eventActivity.findViewById(R.id.facilityName);
        dateAndTimeView = eventActivity.findViewById(R.id.dateAndTime);
        geolocationWarningView = eventActivity.findViewById(R.id.geolcationWarning);
        waitlistSpotsView = eventActivity.findViewById(R.id.waitlistSpots);
        attendeeSpotsView = eventActivity.findViewById(R.id.attendeeSpots);
        currentlyJoinedView = eventActivity.findViewById(R.id.currentlyJoined);

        status = eventActivity.findViewById(R.id.eventStatus);
        signUp = eventActivity.findViewById(R.id.signUpButton);
        notSelectedText = eventActivity.findViewById(R.id.eventNotSelectedText);
        cancel = eventActivity.findViewById(R.id.eventCancel);
        decline = eventActivity.findViewById(R.id.eventDecline);
        accept = eventActivity.findViewById(R.id.eventAccept);
        viewPoster = eventActivity.findViewById(R.id.viewPosterButton);

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

        // show warning if event requires geolocation (US 01.08.01)
        if (getObservable().hasGeolocation()) {
            geolocationWarningView.setVisibility(View.VISIBLE);
        } else {
            geolocationWarningView.setVisibility(View.INVISIBLE);
        }

        // set list limit counts
        if (getObservable().getWaitListSpots() == -1) {
            waitlistSpotsView.setText("Waitlist Spots: Unlimited");
        } else {
            waitlistSpotsView.setText(String.format("Waitlist Spots: %s", getObservable().getWaitListSpots()));
        }
        if (getObservable().getAttendeeSpots() == -1) {
            attendeeSpotsView.setText("Attendee Spots: Unlimited");
        } else {
            attendeeSpotsView.setText(String.format("Attendee Spots: %s", getObservable().getAttendeeSpots()));
        }
        // set current count
        currentlyJoinedView.setText(String.format("Currently Joined: %s", getObservable().getWaitListSize()));

        // erase everything
        currentlyJoinedView.setVisibility(View.GONE);
        waitlistSpotsView.setVisibility(View.GONE);
        attendeeSpotsView.setVisibility(View.GONE);

        signUp.setVisibility(View.GONE);
        notSelectedText.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        decline.setVisibility(View.GONE);
        accept.setVisibility(View.GONE);
        viewPoster.setVisibility(View.GONE);

        // show what we want
        if (getObservable().onWaitList(deviceId)) {
            // Switch mode to waitlist view
            status.setText("You are on the waitlist");

            currentlyJoinedView.setVisibility(View.VISIBLE);
            waitlistSpotsView.setVisibility(View.VISIBLE);
            attendeeSpotsView.setVisibility(View.VISIBLE);

            cancel.setVisibility(View.VISIBLE);
            viewPoster.setVisibility(View.VISIBLE);
        } else if (getObservable().onInviteeList(deviceId)) {
            // mode: invitee
            status.setText("You have been invited to attend!");
            decline.setVisibility(View.VISIBLE);
            accept.setVisibility(View.VISIBLE);
        } else if (getObservable().onAttendeeList(deviceId)) {
            // mode: attendee
            status.setText("You are attending this event");

            currentlyJoinedView.setText(String.format("Current Attendees: %s", getObservable().getAttendeeListSize()));
            currentlyJoinedView.setVisibility(View.VISIBLE);
            attendeeSpotsView.setVisibility(View.VISIBLE);

            viewPoster.setVisibility(View.VISIBLE);
        } else if (getObservable().onCancelledList(deviceId)) {
            status.setText("Unfortunately, you have not been selected. Enable notifications incase a spot opens up.");
//            notSelectedText.setVisibility(View.VISIBLE);
        } else {
            // Default mode is signup view
            status.setText("You can join the waitlist");

            currentlyJoinedView.setVisibility(View.VISIBLE);
            waitlistSpotsView.setVisibility(View.VISIBLE);
            attendeeSpotsView.setVisibility(View.VISIBLE);

            signUp.setVisibility(View.VISIBLE);
            viewPoster.setVisibility(View.VISIBLE);
        }
    }
}
