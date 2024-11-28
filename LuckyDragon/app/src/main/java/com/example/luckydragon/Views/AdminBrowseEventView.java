package com.example.luckydragon.Views;

import android.view.View;

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
        if (getObservable().getQrHash() == null) {
            adminEventFragment.setQrCodeButtonVisibility(View.GONE);
        }
        else {
            adminEventFragment.setQrCodeButtonVisibility(View.VISIBLE);
        }
        if (getObservable().getEventPoster() == null) {
            adminEventFragment.setRemoveEventPosterButtonVisibility(View.GONE);
        }
        else {
            adminEventFragment.setRemoveEventPosterButtonVisibility(View.VISIBLE);
        }
    }
}
