package com.example.luckydragon;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

public class OrganizerProfileView extends Observer {
    private final OrganizerProfileFragment organizerProfileFragment;

    public OrganizerProfileView(User user, OrganizerProfileFragment organizerProfileFragment) {
        this.organizerProfileFragment = organizerProfileFragment;
        startObserving(user);
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }

    @Override
    public void update(Observable whoUpdatedMe) {
        // Set facility name
        Objects.requireNonNull(getObservable().getOrganizer()); // user must have an organizer by this point
        String name;
        if (getObservable().getOrganizer().getFacility() == null) {
            // Set facility text
            name = organizerProfileFragment.getString(R.string.no_facility_message);
            // Change button to "add" icon
            organizerProfileFragment.setFacilityButtonIcon(R.drawable.baseline_add_24);
        } else {
            // Set facility text
            name = getObservable().getOrganizer().getFacility();
        }
        organizerProfileFragment.setFacilityTextView(name);

        organizerProfileFragment.updateEventsList();
        if (getObservable().getOrganizer().getEvents().isEmpty()) {
            // Show "No events" textview if no events and hide otherwise
            organizerProfileFragment.setNoEventsVisibility(View.VISIBLE);
        } else {
            organizerProfileFragment.setNoEventsVisibility(View.GONE);
        }
    }
}
