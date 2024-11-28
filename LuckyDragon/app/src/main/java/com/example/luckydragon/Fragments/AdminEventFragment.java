package com.example.luckydragon.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.luckydragon.Controllers.AdminBrowseProfileController;
import com.example.luckydragon.Controllers.EventController;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.R;
import com.example.luckydragon.Views.AdminBrowseEventView;
import com.example.luckydragon.Views.AdminBrowseProfileView;

/**
 * This is the fragment containing the admin-specific event information.
 * Displays the event details.
 * Allows admin to delete an event or remove its QR code.
 */
public class AdminEventFragment extends Fragment {
    private Event event;
    private EventController eventController;
    private Button removeQrButton;
    private Button adminRemoveEventPoster;
    private AdminBrowseEventView adminBrowseEventView;
    private TextView currentlyJoinedMessageTextview;
    private TextView waitlistSpotsMessageTextView;
    private TextView attendeeSpotsTextview;
    /**
     * Creates an AdminEventFragment.
     */
    public AdminEventFragment() {
        super(R.layout.fragment_admin_event);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        GlobalApp globalApp = ((GlobalApp) requireActivity().getApplication());
        event = globalApp.getEventToView();

        // Create controller
        eventController = new EventController(event);

        // Set up Text views
        currentlyJoinedMessageTextview = getView().findViewById(R.id.currentlyJoinedMessage);
        waitlistSpotsMessageTextView = getView().findViewById(R.id.waitlistSpotsMessage);
        attendeeSpotsTextview = getView().findViewById(R.id.attendeeSpotsMessage);

        // Set up delete event button on click listener
        Button deleteEventButton = view.findViewById(R.id.adminDeleteEventButton);
        deleteEventButton.setOnClickListener(v -> {
            eventController.deleteEvent();
            Toast.makeText(getContext(), "Event Deleted Successfully", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
        });
        // Set up remove qr button on click listener
        removeQrButton = view.findViewById(R.id.adminRemoveQRButton);
        removeQrButton.setOnClickListener(v -> {
            eventController.removeQR();
            Toast.makeText(getContext(), "QR code removed successfully", Toast.LENGTH_SHORT).show();
        });
        // Set up remove event poster button on click listener
        adminRemoveEventPoster = view.findViewById(R.id.adminRemoveEventPoster);
        adminRemoveEventPoster.setOnClickListener(v -> {
            eventController.removeEventPoster();
            Toast.makeText(getContext(), "Event poster removed successfully", Toast.LENGTH_SHORT).show();
        });

        // Now start observing event
        adminBrowseEventView = new AdminBrowseEventView(event, this);
    }

    /**
     * Updates the currently joined message.
     */
    public void updateCurrentlyJoinedMessage() {
        currentlyJoinedMessageTextview.setText(String.format("Currently Joined: %d", event.getWaitListSize()));
    }

    /**
     * Updates the waitlist spots message.
     */
    public void updateWaitlistSpotsMessage() {
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
        attendeeSpotsTextview.setText(String.format("Attendee Spots: %d", event.getAttendeeSpots()));
    }

    /**
     * Sets the visibility for the qr code button
     * @param visibility the visibility to set the button to
     */
    public void setQrCodeButtonVisibility(int visibility) {
        removeQrButton.setVisibility(visibility);
    }

    /**
     * Sets the visibility for the remove poster button
     * @param visibility the visibility to set the button to
     */
    public void setRemoveEventPosterButtonVisibility(int visibility) {
        adminRemoveEventPoster.setVisibility(visibility);
    }

}
