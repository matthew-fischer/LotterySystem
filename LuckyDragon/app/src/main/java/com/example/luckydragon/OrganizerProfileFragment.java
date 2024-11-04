/**
 * Defines the OrganizerProfileFragment which contains the organizer-specific profile content and logic.
 */

package com.example.luckydragon;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class OrganizerProfileFragment extends Fragment {
    private OrganizerProfileView organizerProfileView;
    private User user;
    private EventArrayAdapter eventListAdapter;


    public OrganizerProfileFragment() {
        super(R.layout.fragment_organizer_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Get user from global app
        user = ((GlobalApp) requireActivity().getApplication()).getUser();

        // Create view
        organizerProfileView = new OrganizerProfileView(user, this);

        // Update view
        user.notifyObservers();
        /*
        // Get facility from parent activity
        ProfileActivity parent = (ProfileActivity)requireActivity();
        User user = parent.getUser();

        //assert user.getOrganizer() != null;  // if this runs, we messed up in coding.
        //facilityName = user.getOrganizer().getFacility();



        // Set up organizer events listview
        ListView eventsListView = parent.findViewById(R.id.organizerProfileEventsListview);
        eventList = new ArrayList<>();
        eventListAdapter = new EventArrayAdapter(eventList, parent.getApplicationContext());
        eventsListView.setAdapter(eventListAdapter);
        */

    }

    public void initializeView() {
        View view = getView();

        // Set facility name
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

        // Add on click listener for "Add Event" button
        /* DISABLE EVENT ADDING UNTIL TONY REFACTORS IT
        Button addEventButton = view.findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener((View v) -> {
            if(user.getOrganizer().getFacility() == null) { // if no facility, open the facility edit fragment instead
                DialogFragment editFacilityDialog = new EditFacilityDialogFragment("Add a facility before you create an event!");
                editFacilityDialog.show(getChildFragmentManager(), "EditFacilityDialogFragment");
            } else {
                DialogFragment addEventDialog = new AddEventDialogFragment();
                addEventDialog.show(getChildFragmentManager(), "AddEventDialogFragment");
            }
        });
         */
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
}
