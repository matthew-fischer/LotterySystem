package com.example.luckydragon.Views;

import android.view.View;

import com.example.luckydragon.Fragments.AdminBrowseProfileFragment;
import com.example.luckydragon.Models.Observable;
import com.example.luckydragon.Models.User;

/**
 * Observer class that listens for updates to a User and
 * refreshes the AdminBrowseProfileFragment UI when changes occur.
 * <p>
 *     The BrowseProfileView class observes a User and triggers an update
 *     in AdminBrowseProfileFragment when the user is modified, ensuring that the
 *     user data displayed to user is up-to-date.
 * </p>
 */
public class AdminBrowseProfileView extends Observer {

    private final AdminBrowseProfileFragment adminBrowseProfileFragment;

    /**
     * Constructs a new ViewProfilesView observer and begins observing the UserList.
     * @param user the user to observe for changes
     * @param adminBrowseProfileFragment the fragment to notify of updates
     */
    public AdminBrowseProfileView(User user, AdminBrowseProfileFragment adminBrowseProfileFragment) {
        this.adminBrowseProfileFragment = adminBrowseProfileFragment;
        startObserving(user);
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }

    @Override
    public void update(Observable whoUpdatedMe) {
        if (getObservable().isOrganizer() && getObservable().getOrganizer().getFacility() != null) {
            // Organizer w/ facility
            adminBrowseProfileFragment.setFacilityContainerVisibility(View.VISIBLE);
            adminBrowseProfileFragment.setRemoveFacilityButtonVisiblity(View.VISIBLE);
            adminBrowseProfileFragment.setFacilityName(getObservable().getOrganizer().getFacility());

        } else if (getObservable().isOrganizer() && getObservable().getOrganizer().getFacility() == null) {
            // Organizer w/o facility
            adminBrowseProfileFragment.setFacilityContainerVisibility(View.VISIBLE);
            adminBrowseProfileFragment.setRemoveFacilityButtonVisiblity(View.GONE);
            adminBrowseProfileFragment.setFacilityName("No facility set.");

        } else {
            // Entrant
            adminBrowseProfileFragment.setFacilityContainerVisibility(View.GONE);
            adminBrowseProfileFragment.setRemoveFacilityButtonVisiblity(View.GONE);
        }
    }
}
