package com.example.luckydragon;

public class AddEventController extends Controller {
    public AddEventController(Event observable) {
        super(observable);
    }

    @Override
    public Event getObservable() {
        return (Event) super.getObservable();
    }

    public void add(String deviceId) {
        // Add deviceID to Waitlist:
        getObservable().waitList(deviceId);
    }
}
