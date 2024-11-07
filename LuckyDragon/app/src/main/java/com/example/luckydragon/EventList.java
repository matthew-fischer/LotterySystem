package com.example.luckydragon;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class that manages and observes a list of Event objects from a
 * firestore database.
 * <p>
 *     EventList listens for real-time updates from the firestore collection
 *     "events", as well as supports manual fetching of data.
 * </p>
 */
public class EventList extends Observable {

    private ArrayList<Event> events = new ArrayList<>();
    private FirebaseFirestore db;

    /**
     * Constructs an EventList with the specified database instance.
     * <p>
     *     Sets up a real-time listener on the "events" collection to keep the
     *     events list updated with any changes from firestore.
     * </p>
     * @param firestore The firestore database instance
     */
    public EventList(FirebaseFirestore firestore) {
        this.db = firestore;

        db.collection("events").addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
                return;
            }
            if (value != null) {
                events.clear();
                for (QueryDocumentSnapshot doc: value) {
                    events.add(createEvent(doc));
                }
                notifyObservers();
            }
        });
    }

    /**
     * Fetches the current events data from firestore and updates the events list.
     */
    public void fetchData() {

        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        events.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            events.add(createEvent(document));
                        }

                        Log.d(TAG, "Events loaded successfully with initial get()");
                        notifyObservers();
                    } else {
                        Log.w(TAG, "Error getting initial documents.", task.getException());
                    }
                });

    }

    /**
     * Returns the current lists of events.
     * @return An ArrayList of Event objects.
     */
    public ArrayList<Event> getEventList() {

        return events;

    }

    /**
     * Creates an Event object from a firestore document.
     * @param document The firestore QueryDocumentSnapshot containing event data
     * @return An Event object
     */
    public Event createEvent(QueryDocumentSnapshot document) {
        Map<String, Object> eventData = document.getData();
        Event event = new Event(
                document.getId(),
                eventData.get("name") instanceof String ? (String) eventData.get("name") : null,
                eventData.get("organizerDeviceId") instanceof String ? (String) eventData.get("organizerDeviceId") : null,
                eventData.get("facility") instanceof String ? (String) eventData.get("facility") : null,
                eventData.get("waitListLimit") instanceof Number ? ((Number) eventData.get("waitListLimit")).intValue() : 0,
                eventData.get("attendeeLimit") instanceof Number ? ((Number) eventData.get("attendeeLimit")).intValue() : 0,
                eventData.get("date") instanceof String ? (String) eventData.get("date") : null,
                eventData.get("hours") instanceof Number ? ((Number) eventData.get("hours")).intValue() : 0,
                eventData.get("minutes") instanceof Number ? ((Number) eventData.get("minutes")).intValue() : 0
        );
        return event;
    }

}
