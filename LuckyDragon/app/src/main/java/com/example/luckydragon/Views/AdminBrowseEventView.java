package com.example.luckydragon.Views;

import com.example.luckydragon.Fragments.AdminEventFragment;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.Observable;

public class AdminBrowseEventView extends Observer{

    private final AdminEventFragment adminEventFragment;

    public AdminBrowseEventView(Event event, AdminEventFragment adminEventFragment) {
        this.adminEventFragment = adminEventFragment;
        startObserving(event);
    }

    @Override
    public Event getObservable() {
        return (Event) super.getObservable();
    }

    @Override
    public void update(Observable whoUpdatedMe) {
        adminEventFragment.updateCurrentlyJoinedMessage();
        adminEventFragment.updateWaitlistSpotsMessage();
        adminEventFragment.updateAttendeeSpotsMessage();
    }
}
