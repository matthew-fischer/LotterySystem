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
import java.util.Set;

public class OrganizerProfileFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String facilityName;
    Set<Event> eventSet = new HashSet<>();
    ArrayList<Event> eventList;
    EventArrayAdapter eventListAdapter;

    public OrganizerProfileFragment() {
        super(R.layout.fragment_organizer_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Get facility from parent activity
        ProfileActivity parent = (ProfileActivity)requireActivity();
        User user = parent.getUser();

        assert user.getOrganizer() != null;  // if this runs, we messed up in coding.
        facilityName = user.getOrganizer().getFacility();

        // Set facility name
        MaterialTextView facilityTextView = view.findViewById(R.id.facilityTextView);
        ImageButton facilityEditButton = view.findViewById(R.id.facilityEditButton);
        if (facilityName == null) {
            // Set facility text
            String noFacilityMessage = "You have not created a facility yet.";
            facilityTextView.setText(noFacilityMessage);
            // Change button to "add" icon
            facilityEditButton.setImageResource(R.drawable.baseline_add_24);
        } else {
            // Set facility text
            facilityTextView.setText(facilityName);
        }

        // Add on click listener for "Add Event" button
        Button addEventButton = view.findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener((View v) -> {
            facilityName = user.getOrganizer().getFacility();
            if(facilityName == null) { // if no facility, open the facility edit fragment instead
                DialogFragment editFacilityDialog = new EditFacilityDialogFragment("Add a facility before you create an event!");
                editFacilityDialog.show(getChildFragmentManager(), "EditFacilityDialogFragment");
            } else {
                DialogFragment addEventDialog = new AddEventDialogFragment();
                addEventDialog.show(getChildFragmentManager(), "AddEventDialogFragment");
            }
        });
        // Add on click listener for facility edit button
        facilityEditButton.setOnClickListener((View v) -> {
            DialogFragment editFacilityDialog = new EditFacilityDialogFragment();
            editFacilityDialog.show(getChildFragmentManager(), "EditFacilityDialogFragment");
        });

        // Set up organizer events listview
        ListView eventsListView = parent.findViewById(R.id.organizerProfileEventsListview);
        eventList = new ArrayList<>();
        eventListAdapter = new EventArrayAdapter(eventList, parent.getApplicationContext());
        eventsListView.setAdapter(eventListAdapter);

        // Get events
        db.collection("events")
                .whereEqualTo("organizerDeviceId", parent.getUser().getDeviceId())
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Got documents");
                        Map<String, Object> eventData;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            eventData = document.getData();
                            System.out.println(eventData.get("waitListLimit"));
                            Event event = new Event(
                                    document.getId(),
                                    eventData.get("name") == null ? null : String.format("%s", eventData.get("name")),
                                    eventData.get("organizerDeviceId") == null ? null : String.format("%s", eventData.get("organizerDeviceId")),
                                    eventData.get("facility") == null ? null : String.format("%s", eventData.get("facility")),
                                    eventData.get("waitListLimit") == null ? null : Integer.valueOf(String.format("%s", eventData.get("waitListLimit"))),
                                    eventData.get("attendeeLimit") == null ? null : Integer.valueOf(String.format("%s", eventData.get("attendeeLimit"))),
                                    eventData.get("date") == null ? null : String.format("%s", eventData.get("date")),
                                    eventData.get("hours") == null ? null : Integer.valueOf(String.format("%s", eventData.get("hours"))),
                                    eventData.get("minutes") == null ? null : Integer.valueOf(String.format("%s", eventData.get("minutes")))
                            );
                            eventSet.add(event);
                            eventList.add(event);
                        }
                        eventListAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
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

    public void addEvent(Event event) {
        System.out.println("event added");

        eventList.add(event);
        eventSet.add(event);

        System.out.println(eventList);
        eventListAdapter.notifyDataSetChanged();
    }
}
