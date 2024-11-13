package com.example.luckydragon.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.luckydragon.Controllers.EntrantArrayAdapter;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.R;
import com.example.luckydragon.Views.OrganizerEventView;

/**
 * This is the fragment containing the organizer-specific event info.
 * Displays the different lists of entrants (waiting list, attendees list, etc).
 * ISSUES:
 *   - Need to add a view to update this when changes are made (e.g. waitlist capacity is changed).
 *   - When view is added, should add an onDestroy() method telling the view to stop observing.
 */
public class OrganizerEventFragment extends Fragment {
    private Event event;
    private EntrantArrayAdapter entrantArrayAdapter;
    private OrganizerEventView organizerEventView;

    /**
     * Creates an OrganizerEventFragment.
     */
    public OrganizerEventFragment() {
        super(R.layout.fragment_organizer_event);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Get event
        GlobalApp globalApp = (GlobalApp) requireActivity().getApplication();
        event = globalApp.getEventToView();
        globalApp.setEventToView(null); // no longer need to store event in globalApp

        // Initialize view
        organizerEventView = new OrganizerEventView(event, this);

        // Set up waitlist list view
        ListView waitlistListView = view.findViewById(R.id.eventWaitlistListView);
        // Need a user array adapter
        entrantArrayAdapter = new EntrantArrayAdapter(event.getWaitlistUsers(), requireActivity().getApplicationContext(), this);
        waitlistListView.setAdapter(entrantArrayAdapter);
    }

    /**
     * Updates the displayed waitlist capacity.
     */
    public void updateWaitlistCapacity() {
        TextView waitlistCapacityTextView = getView().findViewById(R.id.waitlistCapacityTextView);
        String waitlistLimit = "No Limit";
        if(event.getWaitListSpots() != -1) {
            waitlistLimit = String.format("%s", event.getWaitListSpots());
        }
        waitlistCapacityTextView.setText(String.format("Capacity: %s", waitlistLimit));
    }
}
