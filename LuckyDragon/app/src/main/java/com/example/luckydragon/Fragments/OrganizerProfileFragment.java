/*
 * Fragment embedded in ProfileActivity containing organizer-specific info.
 * Shows the organizer's facility and events.
 * Has a view (OrganizerProfileView).
 * Issues:
 *   NONE
 */

package com.example.luckydragon.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.luckydragon.Activities.ViewEventActivity;
import com.example.luckydragon.Controllers.EventArrayAdapter;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.EventList;
import com.example.luckydragon.Models.User;
import com.example.luckydragon.Views.OrganizerEventView;
import com.example.luckydragon.Views.OrganizerEventsView;
import com.example.luckydragon.Views.OrganizerProfileView;
import com.example.luckydragon.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

/**
 * This is the fragment containing the organizer-specific profile info.
 * Displays the organizer's facility and events.
 * Updated by OrganizerProfileView.
 */
public class OrganizerProfileFragment extends Fragment {
    private OrganizerProfileView organizerProfileView;
    private User user;
    private EventArrayAdapter eventArrayAdapter;
    private OrganizerEventsView organizerEventsView;
    private EventList eventList;
    private TextView noEventsTextView;

    /**
     * Creates an OrganizerProfileFragment.
     */
    public OrganizerProfileFragment() {
        super(R.layout.fragment_organizer_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Get user
        GlobalApp globalApp = ((GlobalApp) requireActivity().getApplication());
        user = ((GlobalApp) requireActivity().getApplication()).getUser();
        noEventsTextView = view.findViewById(R.id.noEventsTextView);

        // Set up organizer events listview
        eventList = ((GlobalApp) requireActivity().getApplication()).getEvents();
        ListView eventsListView = view.findViewById(R.id.organizerProfileEventsListview);
        eventArrayAdapter = new EventArrayAdapter(eventList.getEventList(),
                requireActivity().getApplicationContext(), this, "ORGANIZER");
        eventsListView.setAdapter(eventArrayAdapter);

        // Set up onClick listener for event in organizer events listview
        eventsListView.setOnItemClickListener((parent, elementView, position, id) -> {
            Event clickedEvent = (Event) parent.getItemAtPosition(position);
            // Start ViewEventActivity
            globalApp.setEventToView(clickedEvent);
            startActivity(new Intent(getContext(), ViewEventActivity.class));
        });

        // Create views
        organizerProfileView = new OrganizerProfileView(user, this);
        organizerEventsView = new OrganizerEventsView(eventList, this);

        // Add on click listener for "Add Event" button
        Button addEventButton = view.findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener((View v) -> {
            String facilityName = user.getOrganizer().getFacility();
            if (facilityName == null) {
                // No facility, so open the facility edit fragment
                DialogFragment editFacilityDialog = new EditFacilityDialogFragment("Add a facility before you create an event!");
                editFacilityDialog.show(getChildFragmentManager(), "EditFacilityDialogFragment");
            } else {
                // Open create event fragment
                DialogFragment addEventDialog = new AddEventDialogFragment();
                addEventDialog.show(getChildFragmentManager(), "AddEventDialogFragment");
            }
        });

        // Add on click listener for facility edit button
        ImageButton facilityEditButton = view.findViewById(R.id.facilityEditButton);
        facilityEditButton.setOnClickListener((View v) -> {
            DialogFragment editFacilityDialog = new EditFacilityDialogFragment();
            editFacilityDialog.show(getChildFragmentManager(), "EditFacilityDialogFragment");
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // the view associated with theis fragment must stop observing when the fragment is destroyed
        organizerProfileView.stopObserving();
    }

    /**
     * Sets the facility text view to a new value.
     * EditFacilityDialogFragment uses this function to update the facility textview after a change is made.
     * @param newFacility the new facility
     */
    public void setFacilityTextView(String newFacility) {
        // Set facility text view to a new value
        Activity activity = requireActivity();
        MaterialTextView facilityTextView = activity.findViewById(R.id.facilityTextView);
        facilityTextView.setText(newFacility);
    }

    /**
     * Sets the facility button icon to a new drawable.
     * EditFacilityDialogFragment uses this function to update the facility button icon after a change.
     * @param newResId the id of the new drawable
     */
    public void setFacilityButtonIcon(int newResId) {
        Activity activity = requireActivity();
        ImageButton facilityButton = activity.findViewById(R.id.facilityEditButton);
        facilityButton.setImageResource(newResId);
    }

    /**
     * Updates the adapter to store the current events that were created by the organizer and
     * the visibility of noEventsTextView.
     */
    public void notifyAdapter() {
        if (!isAdded()) return;

        ArrayList<Event> eventData = new ArrayList<>();
        String deviceID = ((GlobalApp) requireActivity().getApplication()).getUser().getDeviceId();
        for (Event event: eventList.getEventList()) {
            if (event.getOrganizerDeviceId().equals(deviceID)) {
                eventData.add(event);
            }
        }
        eventArrayAdapter.clear();
        eventArrayAdapter.addAll(eventData);
        eventArrayAdapter.notifyDataSetChanged();

        if (eventData.isEmpty()) {
            setNoEventsVisibility(View.VISIBLE);
        } else {
            setNoEventsVisibility(View.GONE);
        }
    }


    /**
     * Sets the visibility of "No events" textview.
     * @param visibility the visibility (VISIBLE or GONE)
     */
    public void setNoEventsVisibility(int visibility) {
        noEventsTextView.setVisibility(visibility);
    }
}
