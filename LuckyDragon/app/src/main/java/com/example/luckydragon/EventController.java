package com.example.luckydragon;

public class EventController extends Controller {
    public EventController(Event observable) {
        super(observable);
    }

    @Override
    public Event getObservable() {
        return (Event) super.getObservable();
    }

    public void waitList(String deviceId) {
        // Add deviceID to Waitlist:
        getObservable().joinWaitList(deviceId);
    }

    public void cancel(String deviceId) {
        getObservable().leaveWaitList(deviceId);
        getObservable().leaveAttendeeList(deviceId);
    }

    public void acceptInvitation(String deviceId) {
        getObservable().leaveInviteeList(deviceId);
        getObservable().joinAttendeeList(deviceId);
    }

    public void declineInvitation(String deviceId) {
        getObservable().leaveInviteeList(deviceId);
        getObservable().joinCancelledList(deviceId);
    }

    public void deleteEvent(String eventId) {
        getObservable().deleteEvent(eventId);
    }

    public void removeQR(String eventId) {
        getObservable().removeQR(eventId);
    }

}
