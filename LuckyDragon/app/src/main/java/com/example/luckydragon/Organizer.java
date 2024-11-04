/**
 * Defines the Organizer model class.
 */

package com.example.luckydragon;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
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
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final private String deviceId;
    private String facility;
    List<Event> events;

    Runnable notifyObservers;

    public Organizer(String deviceId, Runnable notifyObservers) {
        this.deviceId = deviceId;
        this.notifyObservers = notifyObservers;
    }

    /**
     * Creates an Organizer from a facility name.
     * @param facility: the organizer's facility name
     */
    public Organizer(String deviceId, String facility, Runnable notifyObservers) {
        this.deviceId = deviceId;
        this.notifyObservers = notifyObservers;
        this.facility = facility;
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
     * @return the organizer's facility name
     */
    public String getFacility() {
        return facility;
    }

    /**
     * Sets the facility name for the organizer.
     * @param facility: the new facility name
     */
    public void setFacility(String facility) {
        this.facility = facility;
    }

    /**
     * Adds an event to the organizer's list.
     */
    public void addEvent(Event event) {
        events.add(event);
    }

    /**
     * Removes an event from the organizer's list.
     */
    public void removeEvent(Event event) {
        events.remove(event);
    }
}
