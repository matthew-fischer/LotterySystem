package com.example.luckydragon;

public class Organizer extends User {
    private String facility;

    public Organizer(String deviceID, String name, String email, String phoneNumber, String facility) {
        super(deviceID, name, email, phoneNumber);
        this.facility = facility;
    }

    public String getFacility() {
        return facility;
    }
}
