package com.example.luckydragon.Views;

import com.example.luckydragon.Activities.ViewEventActivity;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.Observable;

/**
 * This is the view that updates ViewEventActivity.
 * When the Event is modified, the update() method is triggered.
 * This updates the event details on the event page.
 */
public class ViewEventView extends Observer {
    private final ViewEventActivity viewEventActivity;
    private boolean forceHideQR;
    /**
     * Creates a ViewEventView.
     * @param event the event being displayed
     * @param viewEventActivity the activity displaying the event
     */
    public ViewEventView(Event event, ViewEventActivity viewEventActivity, boolean forceHideQR) {
        this.viewEventActivity = viewEventActivity;
        this.forceHideQR = forceHideQR;
        startObserving(event);
        if (forceHideQR) {
            viewEventActivity.hideQrCodeButton();
        }
    }

    /**
     * Update event details in ViewEventActivity.
     * @param whoUpdatedMe the observable who triggered the change (Event)
     */
    @Override
    public void update(Observable whoUpdatedMe) {
        viewEventActivity.updateEventName();
        viewEventActivity.updateEventFacility();
        viewEventActivity.updateEventDateAndTime();
        viewEventActivity.loadChildFragment();
        viewEventActivity.sampleAttendeesIfNeccessary();
    }

    @Override
    public Event getObservable() {
        return (Event) super.getObservable();
    }
}