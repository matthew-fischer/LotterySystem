/**
 * Defines the Organizer model class.
 * Organizer is always part of a User.
 * ISSUES:
 *   - Should sort the events by most recently added. The current order is sufficient for now.
 */

package com.example.luckydragon.Models;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * Represents an Organizer. Will be an attribute of a User.
 * Adds a facility field and an event list.
 */
public class Organizer {
    private FirebaseFirestore db;
    private String deviceId;
    private String facility;
    private final Runnable notifyObservers;

    /**
     * Creates an organizer with a facility.
     * @param deviceId the organizer's device id
     * @param facility the organizer's facility
     * @param notifyObservers the parent user's notifyObservers method
     * @param db the database to use
     */
    public Organizer(String deviceId, String facility, Runnable notifyObservers, FirebaseFirestore db) {
        this.deviceId = deviceId;
        this.facility = facility;
        this.notifyObservers = notifyObservers;
        this.db = db;
    }

    /**
     * Gets the facility name for the organizer.
     * @return the organizer's facility name
     */
    public String getFacility() {
        return facility;
    }

    /**
     * Adds an event to the organizer's list.
     * @param event the event to be added
     */
    public void addEvent(Event event) {
        return;
    }

    /**
     * Remove an event from the organizer's list.
     * @param event the event to be removed
     */
    public void removeEvent(Event event) {
        return;
    }

    /**
     * Removes all events associated with the organizer and set facility to null.
     */
    public void removeFacility() {
        this.facility = null;
        db.collection("events")
                .whereEqualTo("organizerDeviceId", deviceId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document: queryDocumentSnapshots) {
                        document.getReference().delete();
                    }
                });
        notifyObservers.run();
    }

    /**
     * Sets the facility name for the organizer and updates organizers events in the database
     * to the facility name.
     * @param facility: the new facility name
     */
    public void setFacility(String facility) {
        this.facility = facility;
        db.collection("events")
                .whereEqualTo("organizerDeviceId", deviceId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document: queryDocumentSnapshots) {
                        document.getReference().update("facility", facility);
                    }
                });
        notifyObservers.run();
    }
}


