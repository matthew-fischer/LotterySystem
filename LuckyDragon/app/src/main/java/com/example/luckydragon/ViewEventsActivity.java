package com.example.luckydragon;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ViewEventsActivity extends AppBarActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Set<Event> eventSet = new HashSet<>();
    private ArrayList<Event> eventList;
    private EventArrayAdapter eventListAdapter;
    private ListView eventsListView;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_events);
        getSupportActionBar().setTitle("Events");

        // Set up admin events listview
        eventsListView = findViewById(R.id.adminProfileEventsListview);
        eventList = new ArrayList<>();
        eventListAdapter = new EventArrayAdapter(eventList, this, fragment);
        eventsListView.setAdapter(eventListAdapter);

        // Initial one-time load with get()
        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        eventSet.clear();
                        eventList.clear();

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

                            eventSet.add(event);
                            eventList.add(event);
                        }

                        eventListAdapter.notifyDataSetChanged();
                        Log.d(TAG, "Events loaded successfully with initial get()");

                        // Set up the real-time listener for updates
                        addRealTimeListener();
                    } else {
                        Log.w(TAG, "Error getting initial documents.", task.getException());
                    }
                });

        // Set up item click listener for ListView
        eventsListView.setOnItemClickListener((adapterView, v, position, l) -> {
            Event event = (Event) adapterView.getItemAtPosition(position);
            Intent intent = new Intent(ViewEventsActivity.this, AdminEventActivity.class);
            intent.putExtra("event", event.getId());
            startActivity(intent);
        });
    }

    // Snapchat listener for real-time updates
    private void addRealTimeListener() {
        db.collection("events")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (snapshots != null) {
                        eventSet.clear();
                        eventList.clear();

                        for (QueryDocumentSnapshot document : snapshots) {
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

                            eventSet.add(event);
                            eventList.add(event);
                        }

                        eventListAdapter.notifyDataSetChanged();
                        Log.d(TAG, "Real-time update received");
                    } else {
                        Log.d(TAG, "No events available or empty snapshot");
                    }
                });
    }

}
