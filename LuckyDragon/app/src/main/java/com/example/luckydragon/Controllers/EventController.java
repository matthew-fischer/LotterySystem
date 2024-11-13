package com.example.luckydragon.Controllers;

import com.example.luckydragon.Models.Event;

/**
 * This class is a controller for the EventActivity. Handles Entrants
 * accepting/declining invitations or joining/leaving a waitlist and updating
 * the Event model correspondingly. It also handles updating the model if a
 * Admin deletes an event or removes the QR code.
 */
public class EventController extends Controller {
    public EventController(Event observable) {
        super(observable);
    }

    @Override
    public Event getObservable() {
        return (Event) super.getObservable();
    }

    /**
     * Adds or removes entrant from waiting list, depending on whether entrant is already on waiting list or not.
     * @param deviceId the deviceId of the entrant
     */
    public void toggleWaitlist(String deviceId) {
        if(getObservable().onWaitList(deviceId)) {
            getObservable().leaveWaitList(deviceId);
        } else {
            getObservable().joinWaitList(deviceId);
        }
    }

    /**
     * Update the Event model after an Entrant joins the waiting list.
     * @param deviceId the deviceId of the entrant who is joining the waitlist
     */
    public void waitList(String deviceId) {
        // Add deviceID to Waitlist:
        getObservable().joinWaitList(deviceId);
    }

    /**
     * Updates the Event model after an Entrant leaves the waitlist or attendee
     * list.
     * @param deviceId the deviceId of the entrant
     */
    public void cancel(String deviceId) {
        getObservable().leaveWaitList(deviceId);
        getObservable().leaveAttendeeList(deviceId);
    }

    /**
     * Updates the Event model after an Entrant accepts an invitation to the waitlist
     * @param deviceId the deviceId of the entrant
     */
    public void acceptInvitation(String deviceId) {
        getObservable().leaveInviteeList(deviceId);
        getObservable().joinAttendeeList(deviceId);
    }

    /**
     * Updates the event model after an Entrant declines an invitation to the waitlist.
     * @param deviceId the deviceId of the entrant
     */
    public void declineInvitation(String deviceId) {
        getObservable().leaveInviteeList(deviceId);
        getObservable().joinCancelledList(deviceId);
    }

    /**
     * Updates the Event model after an Admin deletes the event.
     * @param eventId the id of the event
     */
    public void deleteEvent(String eventId) {
        getObservable().deleteEvent(eventId);
    }

    /**
     * Updates the Event model after an Admin removes the QR code of the event.
     * @param eventId the id of the event
     */
    public void removeQR(String eventId) {
        getObservable().removeQR(eventId);
    }

}
