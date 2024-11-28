package com.example.luckydragon.Views;

import android.view.View;

import com.example.luckydragon.Fragments.AdminBrowseProfileFragment;
import com.example.luckydragon.Models.Observable;
import com.example.luckydragon.Models.User;

public class BrowseProfileView extends Observer {

    private final AdminBrowseProfileFragment adminBrowseProfileFragment;

    public BrowseProfileView(User user, AdminBrowseProfileFragment adminBrowseProfileFragment) {
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
