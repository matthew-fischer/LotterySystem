/**
 * Defines the Organizer model class.
 * Organizer is always part of a User.
 * ISSUES:
 *   - Should sort the events by most recently added. The current order is sufficient for now.
 */

package com.example.luckydragon.Models;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
    private final ArrayList<Event> events;
    private final Runnable notifyObservers;

    /**
     * Creates an organizer without a facility.
     * @param deviceId the organizer's device id
     * @param notifyObservers the parent user's notifyObservers method
     * @param db the database to use
     */
    public Organizer(String deviceId, Runnable notifyObservers, FirebaseFirestore db) {
        this.deviceId = deviceId;
        this.notifyObservers = notifyObservers;
        this.events = new ArrayList<>();
        this.db = db;
    }

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
        this.events = new ArrayList<>();
        this.db = db;
    }

    /**
     * Fetches event data from firestore
     */
    public void fetchEvents() {
        // Get events
        db.collection("events")
                .whereEqualTo("organizerDeviceId", deviceId)
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        Map<String, Object> eventData;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            eventData = document.getData();
                            Event event = new Event(
                                    document.getId(),
                                    eventData.get("name") == null ? null : String.format("%s", eventData.get("name")),
                                    eventData.get("organizerDeviceId") == null ? null : String.format("%s", eventData.get("organizerDeviceID")),
                                    eventData.get("facility") == null ? null : String.format("%s", eventData.get("facility")),
                                    eventData.get("waitlistLimit") == null ? null : Integer.valueOf(String.format("%s", eventData.get("waitlistLimit"))),
                                    eventData.get("attendeeLimit") == null ? null : Integer.valueOf(String.format("%s", eventData.get("attendeeLimit"))),
                                    eventData.get("date") == null ? null : String.format("%s", eventData.get("date")),
                                    eventData.get("hours") == null ? null : Integer.valueOf(String.format("%s", eventData.get("hours"))),
                                    eventData.get("minutes") == null ? null : Integer.valueOf(String.format("%s", eventData.get("minutes")))
                            );

                            // Check for duplicate events (could switch this to a set for performance, but event counts should be low)
                            for(int i = 0; i < events.size(); i++) {
                                if(Objects.equals(event.getId(), events.get(i).getId())) return;
                            }
                            addEvent(event); // calls notifyObservers
                            event.fetchData(); // now fetch the rest of event data (waitlist, etc)
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

        // Set all of the organizers events to use this facility
        for(Event e : events) {
            e.setFacility(facility);
        }

        notifyObservers.run();
    }

    /**
     * Gets the organizer's events list.
     * @return list of the organizer's events
     */
    public ArrayList<Event> getEvents() {
        return events;
    }

    /**
     * Adds an event to the organizer's list.
     * @param event the event to be added
     */
    public void addEvent(Event event) {
        if(event.getName().isEmpty() || event.getFacility().isEmpty() || event.getAttendeeSpots() == -1) {
            Log.e("EVENT", "Did not add event because some mandatory fields were empty!");
            event.deleteEventFromDb(); // make sure event is not in db
            return;
        }
        events.add(event);
        notifyObservers.run();
    }

    /**
     * Remove an event from the organizer's list.
     * @param event the event to be removed
     */
    public void removeEvent(Event event) {
        events.remove(event);
        notifyObservers.run();
    }
}


