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
    private Event event;
    /**
     * Creates a ViewEv(entView.
     * @param event the event being displayed
     * @param viewEventActivity the activity displaying the event
     */
    public ViewEventView(Event event, ViewEventActivity viewEventActivity, boolean forceHideQR) {
        this.viewEventActivity = viewEventActivity;
        this.forceHideQR = forceHideQR;
        this.event = event;
        startObserving(event);
        updateQRVisibility();
    }

    /**
     * Update the visibility of the QR Code image button of ViewEventActivity.
     * If it's forced to be hidden, it will always be hidden. Otherwise
     * decide based on whether or not the event has a QR code.
     */
    public void updateQRVisibility() {
        if (forceHideQR || event.getQRBitMatrix() == null) {
            viewEventActivity.hideQrCodeButton();
        } else {
            viewEventActivity.showQrCodeButton();
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
