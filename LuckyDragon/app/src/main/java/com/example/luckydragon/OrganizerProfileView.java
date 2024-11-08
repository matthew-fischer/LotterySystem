/*
 * Defines the view associated with OrganizerProfileFragment.
 * ISSUES:
 *   NONE
 */

package com.example.luckydragon;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.luckydragon.OrganizerProfileFragment;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

/**
 * This is the view associated with OrganizerProfileFragment.
 * Updates OrganizerProfileFragment when triggered by a change in an Observable (model) object.
 */
public class OrganizerProfileView extends Observer {
    private final OrganizerProfileFragment organizerProfileFragment;

    /**
     * Creates an OrganizerProfileView.
     * @param user the application user
     * @param organizerProfileFragment the fragment to update
     */
    public OrganizerProfileView(User user, OrganizerProfileFragment organizerProfileFragment) {
        this.organizerProfileFragment = organizerProfileFragment;
        startObserving(user);
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }

    /**
     * Updates OrganizerProfileFragment.
     * Sets facility name and updates event list.
     * @param whoUpdatedMe the observable who triggered the update
     */
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
