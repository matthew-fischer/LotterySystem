package com.example.luckydragon.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.luckydragon.Controllers.EventController;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.R;
import com.example.luckydragon.Views.EntrantEventWaitlistView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

/**
 * This is the fragment containing the entrant-specific event info.
 * Displays event waitlist information and attendee spots.
 * Allows entrant to join or leave waitlist.
 * Allows entrant to view the event poster.
 * Displays a geolocation warning if it is enabled for the event.
 * ISSUES: None
 */
public class EntrantEventWaitlistFragment extends Fragment {
    private String deviceId;
    private Event event;
    private EntrantEventWaitlistView entrantEventWaitlistView; // TODO define view
    private EventController eventController;
    private FusedLocationProviderClient fusedLocationClient;
    private Double latitude;
    private Double longitude;

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    getLocationAndJoinWaitlist();
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });

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
        waitlistActionButton.setOnClickListener(v -> {
            if(!event.onWaitList(deviceId)) {
                if(event.hasGeolocation()) {
                    getLocationAndJoinWaitlist();
                } else {
                    eventController.waitList(deviceId);
                }
            } else {
                if(event.hasGeolocation()) {
                    eventController.cancelWithLocation(deviceId);
                } else {
                    eventController.cancel(deviceId);
                }
            }
        });

        // Set up view poster button on click listener
        Button viewPosterButton = view.findViewById(R.id.viewEventPosterButton);
        viewPosterButton.setOnClickListener(v -> {
            // There is no poster uploaded
            if (event.getEventPoster() == null) {
                Toast.makeText(getContext(), "Organizer did not upload a poster.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            // Put together arguments for image fragment
            Bundle args = new Bundle();
            args.putString("title", "Event Poster");
            args.putString("negativeButton", "Close");
            args.putParcelable("image", event.getEventPoster());
            // Show image fragment
            DialogFragment displayPosterFragment = new DisplayImageFragment();
            displayPosterFragment.setArguments(args);
            displayPosterFragment.show(getChildFragmentManager(), "DisplayImageFragment");
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

    private void getLocationAndJoinWaitlist() {
        if (ActivityCompat.checkSelfPermission(requireContext().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
            return;
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if(location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                eventController.waitlistWithLocation(deviceId, latitude, longitude);
            } else {
                Log.e("LOCATION", "Last location could not be accessed.");
            }
        }).addOnFailureListener(e -> {
            Log.e("LOCATION", "Request for location failed");
            Log.e("LOCATION", e.getMessage());
        });
    }
}
