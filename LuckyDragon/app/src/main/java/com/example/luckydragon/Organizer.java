/**
 * Defines the Organizer model class.
 */

package com.example.luckydragon;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents an Organizer. Subclass of User.
 * Adds a facility field.
 * <p>
 * Issues:
 *   This is only a basic implementation. Additional functionality should be added as needed.
 *   Since email and phone number are optional, additional constructors should be added.
 */
public class Organizer {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String deviceId;
    private String facility;
    private final ArrayList<Event> events;
    private final Runnable notifyObservers;

    public Organizer(String deviceID, Runnable notifyObservers) {
        this.deviceId = deviceId;
        this.notifyObservers = notifyObservers;
        this.events = new ArrayList<>();
        fetchEvents();
    }

    /**
     * Creates an Organizer from a facility name.
     *
     * @param facility: the organizer's facility name
     */
    public Organizer(String deviceId, String facility, Runnable notifyObservers) {
        this.deviceId = deviceId;
        this.facility = facility;
        this.notifyObservers = notifyObservers;
        this.events = new ArrayList<>();
        fetchEvents();
    }

    /**
     * Fetches event data from firestore
     */
    public void fetchEvents() {
        // Get events
        db.collection("events")
                .whereEqualTo("organizerDeviceID", deviceId)
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        Map<String, Object> eventData;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            eventData = document.getData();
                            Event event = new Event(
                                    document.getId(),
                                    eventData.get("Name") == null ? null : String.format("%s", eventData.get("Name")),
                                    eventData.get("OrganizerDeviceID") == null ? null : String.format("%s", eventData.get("OrganizerDeviceID")),
                                    eventData.get("Facility") == null ? null : String.format("%s", eventData.get("Facility")),
                                    eventData.get("WaitlistLimit") == null ? null : Integer.valueOf(String.format("%s", eventData.get("WaitlistLimit"))),
                                    eventData.get("AttendeeLimit") == null ? null : Integer.valueOf(String.format("%s", eventData.get("AttendeeLimit"))),
                                    eventData.get("Date") == null ? null : String.format("%s", eventData.get("Date")),
                                    eventData.get("Hours") == null ? null : Integer.valueOf(String.format("%s", eventData.get("Hours"))),
                                    eventData.get("Minutes") == null ? null : Integer.valueOf(String.format("%s", eventData.get("Minutes")))
                            );
                            events.add(event);
                            notifyObservers.run(); // notify observers of the parent User
                        }

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    /**
     * Gets the facility name for the organizer.
     *
     * @return the organizer's facility name
     */
    public String getFacility() {
        return facility;
    }

    /**
     * Sets the facility name for the organizer.
     *
     * @param facility: the new facility name
     */
    public void setFacility(String facility) {
        this.facility = facility;
    }

    /**
     * <<<<<<< HEAD
     * Gets the organizer's events list.
     */
    public ArrayList<Event> getEvents() {
        return events;
    }

    /**
     * Adds an event to the organizer's list.
     *
     * @param event the event to be added
     */
    public void addEvent(Event event) {
        Log.e("EVENT", "event added");
        events.add(event);
        notifyObservers.run();
    }

    /**
     * Remove an event from the organizer's list.
     *
     * @param event the event to be removed
     */
    public void removeEvent(Event event) {
        events.remove(event);
        notifyObservers.run();
    }
}


