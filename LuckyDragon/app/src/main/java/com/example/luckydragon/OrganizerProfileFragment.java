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
        user.getOrganizer().fetchEvents();

        // Set up organizer events listview
        ListView eventsListView = view.findViewById(R.id.organizerProfileEventsListview);
        eventListAdapter = new EventArrayAdapter(user.getOrganizer().getEvents(), requireActivity().getApplicationContext(), this);
        eventsListView.setAdapter(eventListAdapter);

        // Create view
        organizerProfileView = new OrganizerProfileView(user, this);

        // Add on click listener for "Add Event" button
        Button addEventButton = view.findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener((View v) -> {
            String facilityName = user.getOrganizer().getFacility();
            if (facilityName == null) { // if no facility, open the facility edit fragment instead
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
    }

    public void setNoEventsVisibility(int visibility) {
        // Show "No events" textview if no events and hide otherwise
        TextView noEventsTextView = getView().findViewById(R.id.noEventsTextView);
        noEventsTextView.setVisibility(visibility);
    }
}
