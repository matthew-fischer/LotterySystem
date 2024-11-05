/**
 * Defines the OrganizerProfileFragment which contains the organizer-specific profile content and logic.
 */

package com.example.luckydragon;

import android.app.Activity;
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

import com.google.android.material.textview.MaterialTextView;

public class OrganizerProfileFragment extends Fragment {
    private OrganizerProfileView organizerProfileView;
    private User user;
    private EventArrayAdapter eventListAdapter;

    public OrganizerProfileFragment() {
        super(R.layout.fragment_organizer_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Get user
        user = ((GlobalApp) requireActivity().getApplication()).getUser();
        assert user.getOrganizer() != null; // user must be an organizer by this point

        // Set up organizer events listview
        ListView eventsListView = view.findViewById(R.id.organizerProfileEventsListview);
        eventListAdapter = new EventArrayAdapter(user.getOrganizer().getEvents(), requireActivity().getApplicationContext());
        eventsListView.setAdapter(eventListAdapter);

        // Create view
        organizerProfileView = new OrganizerProfileView(user, this);

        // Update view
        setFacilityName();
        updateEventsList();

        // Add on click listener for "Add Event" button
        Button addEventButton = view.findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener((View v) -> {
            String facilityName = user.getOrganizer().getFacility();
            if(facilityName == null) { // if no facility, open the facility edit fragment instead
                DialogFragment editFacilityDialog = new EditFacilityDialogFragment("Add a facility before you create an event!");
                editFacilityDialog.show(getChildFragmentManager(), "EditFacilityDialogFragment");
            } else {
                DialogFragment addEventDialog = new AddEventDialogFragment();
                addEventDialog.show(getChildFragmentManager(), "AddEventDialogFragment");
            }
        });
        ImageButton facilityEditButton = view.findViewById(R.id.facilityEditButton);
        // Add on click listener for facility edit button
        facilityEditButton.setOnClickListener((View v) -> {
            DialogFragment editFacilityDialog = new EditFacilityDialogFragment();
            editFacilityDialog.show(getChildFragmentManager(), "EditFacilityDialogFragment");
        });

    }

    public void setFacilityName() {
        View view = requireView();

        // Set facility name
        String facilityName = user.getOrganizer().getFacility();
        MaterialTextView facilityTextView = view.findViewById(R.id.facilityTextView);
        ImageButton facilityEditButton = view.findViewById(R.id.facilityEditButton);
        assert user.getOrganizer() != null; // user must have an organizer by this point
        if (user.getOrganizer().getFacility() == null) {
            // Set facility text
            String noFacilityMessage = "You have not created a facility yet.";
            facilityTextView.setText(noFacilityMessage);
            // Change button to "add" icon
            facilityEditButton.setImageResource(R.drawable.baseline_add_24);
        } else {
            // Set facility text
            facilityTextView.setText(user.getOrganizer().getFacility());
        }
    }

    // EditFacilityDialogFragment uses this function to update the facility textview after a change is made.
    public void setFacilityTextView(String newFacility) {
        // Set facility text view to a new value
        Activity activity = requireActivity();
        MaterialTextView facilityTextView = activity.findViewById(R.id.facilityTextView);
        facilityTextView.setText(newFacility);
    }

    // EditFacilityDialogFragment uses this function to update the facility button icon after a change.
    public void setFacilityButtonIcon(int newResId) {
        Activity activity = requireActivity();
        ImageButton facilityButton = activity.findViewById(R.id.facilityEditButton);
        facilityButton.setImageResource(newResId);
    }

    public void updateEventsList() {
        eventListAdapter.notifyDataSetChanged();
        // Show "No events" textview if no events and hide otherwise
        TextView noEventsTextView = getView().findViewById(R.id.noEventsTextView);
        if(user.getOrganizer().getEvents().isEmpty()) {
            noEventsTextView.setVisibility(View.VISIBLE);
        } else {
            noEventsTextView.setVisibility(View.GONE);
        }
    }
}
