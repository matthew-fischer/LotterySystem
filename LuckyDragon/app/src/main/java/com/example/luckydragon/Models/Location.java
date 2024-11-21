package com.example.luckydragon.Models;

/**
 * Represents a location as a <latitude, longitude> pair.
 */
public class Location {
    private double latitude;
    private double longitude;

    /**
     * Create a Location object.
     * @param latitude location latitude
     * @param longitude location longitude
     */
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
