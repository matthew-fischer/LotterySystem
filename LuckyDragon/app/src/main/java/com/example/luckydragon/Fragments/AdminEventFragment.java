package com.example.luckydragon.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.luckydragon.Controllers.EventController;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.R;

/**
 * This is the fragment containing the admin-specific event information.
 * Displays the event details.
 * Allows admin to delete an event or remove its QR code.
 */
public class AdminEventFragment extends Fragment {
    private Event event;
    private EventController eventController;

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

        // Set event details -- this only needs to happen when fragment is created
        updateCurrentlyJoinedMessage();
        updateWaitlistSpotsMessage();
        updateAttendeeSpotsMessage();

        // Set up delete event button on click listener
        Button deleteEventButton = view.findViewById(R.id.adminDeleteEventButton);
        deleteEventButton.setOnClickListener(v -> {
            eventController.deleteEvent();
            Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
        });
        // Set up remove qr button on click listener
        Button removeQrButton = view.findViewById(R.id.adminRemoveQRButton);
        removeQrButton.setOnClickListener(v -> {
            if (event.getQrHash() == null) {
                Toast.makeText(getContext(), "Event does not have a QR code", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
            else {
                eventController.removeQR();
                Toast.makeText(getContext(), "QR code removed successfully", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
        });
        // Set up remove event poster button on click listener
        Button adminRemoveEventPoster = view.findViewById(R.id.adminRemoveEventPoster);
        adminRemoveEventPoster.setOnClickListener(v -> {
            if (event.getEventPoster() == null) {
                Toast.makeText(getContext(), "Event does not have a poster", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
            else {
                eventController.removeEventPoster();
                Toast.makeText(getContext(), "Event poster removed successfully", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
        });
        // Set up remove facility button on click listener
        Button adminRemoveFacility = view.findViewById(R.id.adminRemoveFacilityButton);
        adminRemoveFacility.setOnClickListener(v -> {
            eventController.removeFacility();
            Toast.makeText(getContext(), "Facility removed and all events associated with it", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
        });
        // Hide remove QR button if event has no QR code
        if(event.getQrHash() == null) {
            removeQrButton.setVisibility(View.GONE);
        }
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
}
