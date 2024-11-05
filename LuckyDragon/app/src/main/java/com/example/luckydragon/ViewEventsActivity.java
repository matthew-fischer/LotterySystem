package com.example.luckydragon;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ViewEventsActivity extends AppBarActivity{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Set<Event> eventSet = new HashSet<>();
    private ArrayList<Event> eventList;
    private EventArrayAdapter eventListAdapter;
    private ListView eventsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_events);
        getSupportActionBar().setTitle("Events");

        // Set up admin events listview
        eventsListView = findViewById(R.id.adminProfileEventsListview);
        eventList = new ArrayList<>();
        eventListAdapter = new EventArrayAdapter(eventList, this);
        eventsListView.setAdapter(eventListAdapter);

        // Get all events
        db.collection("events")
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Got documents");
                        Map<String, Object> eventData;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            eventData = document.getData();
                            Event event = new Event(
                                    document.getId(),
                                    eventData.get("name") instanceof String ? (String) eventData.get("name") : null,
                                    eventData.get("organizerDeviceId") instanceof String ? (String) eventData.get("organizerDeviceId") : null,
                                    eventData.get("facility") instanceof String ? (String) eventData.get("facility") : null,
                                    eventData.get("waitListLimit") instanceof Number ? ((Number) eventData.get("waitListLimit")).intValue() : 0,  // default 0
                                    eventData.get("attendeeLimit") instanceof Number ? ((Number) eventData.get("attendeeLimit")).intValue() : 0, // default 0
                                    eventData.get("date") instanceof String ? (String) eventData.get("date") : null,
                                    eventData.get("hours") instanceof Number ? ((Number) eventData.get("hours")).intValue() : 0, // default 0
                                    eventData.get("minutes") instanceof Number ? ((Number) eventData.get("minutes")).intValue() : 0 // default 0
                            );
                            eventSet.add(event);
                            eventList.add(event);
                        }
                        View eventDescView = getLayoutInflater().inflate(R.layout.content_event_desc, null);
                        ImageButton qrCodeIcon = eventDescView.findViewById(R.id.qrCodeIcon);
                        qrCodeIcon.setVisibility(View.INVISIBLE);
                        eventListAdapter.notifyDataSetChanged();
                        eventsListView.setOnItemClickListener((AdapterView<?> adapterView, View v, int position, long l) -> {
                            //Log.e("TEST", "HERE");
                            System.out.println("AAAAAA");
                            Event event = (Event) adapterView.getItemAtPosition(position);
                            Intent intent = new Intent(ViewEventsActivity.this, AdminEventActivity.class);
                            intent.putExtra("event", event.getId());
                            startActivity(intent);
                        });
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });


    }
}
