package com.example.luckydragon;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

public class OrganizerProfileFragment extends Fragment {
    public OrganizerProfileFragment() {
        super(R.layout.fragment_organizer_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Add on click listener for "Add Event" button
        Button addEventButton = view.findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener((View v) -> {
            DialogFragment addEventDialog = new AddEventDialogFragment();
            addEventDialog.show(requireActivity().getSupportFragmentManager(), "AddEventDialogFragment");
        });
        // Add on click listener for facility edit button
        ImageButton facilityEditButton = view.findViewById(R.id.facilityEditButton);
        facilityEditButton.setOnClickListener((View v) -> {
            DialogFragment editFacilityDialog = new EditFacilityDialogFragment();
            editFacilityDialog.show(getChildFragmentManager(), "EditFacilityDialogFragment");
        });
        // Get facility from parent activity
        ProfileActivity parent = (ProfileActivity)requireActivity();
        Organizer organizer = (Organizer)(parent.getUser());
        String facilityName = organizer.getFacility();
        // Set facility name
        MaterialTextView facilityTextView = view.findViewById(R.id.facilityTextView);
        facilityTextView.setText(facilityName);
    }

    public void setFacilityTextView(String newFacility) {
        Activity activity = requireActivity();
        MaterialTextView facilityTextView = activity.findViewById(R.id.facilityTextView);
        facilityTextView.setText(newFacility);
    }
}
