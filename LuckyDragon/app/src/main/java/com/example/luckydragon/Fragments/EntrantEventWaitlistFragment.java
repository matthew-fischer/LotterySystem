package com.example.luckydragon.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.luckydragon.Controllers.EventController;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.R;
import com.example.luckydragon.Views.EntrantEventWaitlistView;

/**
 * This is the fragment containing the entrant-specific event info.
 * Displays event waitlist information and attendee spots.
 * Allows entrant to join or leave waitlist.
 * Allows entrant to view the event poster.
 * Displays a geolocation warning if it is enabled for the event.
 * ISSUES:
 *   - Once a entrant has been invited to attend an event, they will need to be shown new information and buttons.
 *      It might be best to use another fragment for that information, instead of adding complexity to this one.
 *   - Viewing the event poster has not been implemented yet.
 */
public class EntrantEventWaitlistFragment extends Fragment {
    private String deviceId;
    private Event event;
    private EntrantEventWaitlistView entrantEventWaitlistView; // TODO define view
    private EventController eventController;

    /**
     * Creates an EntrantEventFragment.
     */
    public EntrantEventWaitlistFragment() {
        super(R.layout.fragment_entrant_waitlist_event); // TODO
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Get event
        GlobalApp globalApp = (GlobalApp) requireActivity().getApplication();
        event = globalApp.getEventToView();

        // Get device id
        deviceId = globalApp.getUser().getDeviceId();

        // Create view
        entrantEventWaitlistView = new EntrantEventWaitlistView(event, this);

        // Create controller
        eventController = new EventController(event);

        // Set up waitlist action button on click listener
        Button waitlistActionButton = view.findViewById(R.id.waitlistActionButton);
        waitlistActionButton.setOnClickListener(v -> eventController.toggleWaitlist(deviceId));

        // Set up view poster button on click listener
        Button viewPosterButton = view.findViewById(R.id.viewEventPosterButton);
        viewPosterButton.setOnClickListener(v -> {
            // TODO show poster
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // the view associated with theis fragment must stop observing when the fragment is destroyed
        entrantEventWaitlistView.stopObserving();
    }

    /**
     * Updates the event status message.
     * If entrant is already on the waitlist, displays "You are on the waitlist."
     * If entrant is not on waitlist but it is full, displays "The waitlist is already full."
     * If entrant is not on waitlist and waitlist is not full, displays "You can join the waitlist."
     */
    public void updateStatusMessage() {
        TextView statusMessageTextView = getView().findViewById(R.id.entrantEventStatusMessage);
        String statusMessage;
        if(event.onWaitList(deviceId)) {
            statusMessage = "You are on the waitlist.";
        } else if(event.getWaitListSpots() == event.getWaitListSize()) {
            statusMessage = "The waitlist is already full.";
        } else {
            statusMessage = "You can join the waitlist.";
        }
        statusMessageTextView.setText(statusMessage);
    }

    /**
     * Updates the currently joined message.
     */
    public void updateCurrentlyJoinedMessage() {
        TextView currentlyJoinedMessageTextview = getView().findViewById(R.id.currentlyJoinedMessage);
        currentlyJoinedMessageTextview.setText(String.format("Currently Joined: %d", event.getWaitListSize()));
    }

    /**
     * Updates the waitlist spots message.
     */
    public void updateWaitlistSpotsMessage() {
        TextView waitlistSpotsMessageTextView = getView().findViewById(R.id.waitlistSpotsMessage);
        String waitlistMessage = "Waitlist Spots: %s";
        if(event.getWaitListSpots() == -1) {
            waitlistSpotsMessageTextView.setText(String.format(waitlistMessage, "No Limit"));
        } else {
            waitlistSpotsMessageTextView.setText(String.format(waitlistMessage, event.getWaitListSpots()));
        }
    }

    /**
     * Updates the attendee spots message.
     */
    public void updateAttendeeSpotsMessage() {
        TextView currentlyJoinedMessageTextview = getView().findViewById(R.id.attendeeSpotsMessage);
        currentlyJoinedMessageTextview.setText(String.format("Attendee Spots: %d", event.getAttendeeSpots()));
    }

    /**
     * Updates the waitlist action button.
     * If entrant is not on waitlist, displays "Join Waitlist".
     * If waitlist is full, deactivates the Join Waitlist button.
     * If entrant is already on waitlist, displays "Cancel".
     */
    public void updateWaitlistActionButton() {
        Button waitlistActionButton = getView().findViewById(R.id.waitlistActionButton);
        if(event.onWaitList(deviceId)) {
            waitlistActionButton.setText("Cancel");
        } else {
            waitlistActionButton.setText("Join Waitlist");
            if(event.getWaitListSize() == event.getWaitListSpots()) {
                waitlistActionButton.setActivated(false);
            } else {
                waitlistActionButton.setActivated(true);
            }
        }
    }

    /**
     * Updates the geolocation message.
     * If event has geolocation enabled, a warning message is displayed.
     * If not, no message is displayed.
     */
    public void updateGeolocationMessage() {
        TextView geolocationMessageTextView = getView().findViewById(R.id.geolocationMessage);
        if(event.hasGeolocation()) {
            geolocationMessageTextView.setVisibility(View.VISIBLE);
            geolocationMessageTextView.setText(R.string.geolocation_on_warning);
        } else {
            geolocationMessageTextView.setVisibility(View.GONE);
        }
    }
}
