package com.example.luckydragon;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class EventList extends Observable {

    private ArrayList<Event> events = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void fetchData() {

        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        events.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
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

                            events.add(event);
                        }

                        Log.d(TAG, "Events loaded successfully with initial get()");
                        notifyObservers();
                    } else {
                        Log.w(TAG, "Error getting initial documents.", task.getException());
                    }
                });

    }

    public ArrayList<Event> getEventList() {

        return events;

    }

}
