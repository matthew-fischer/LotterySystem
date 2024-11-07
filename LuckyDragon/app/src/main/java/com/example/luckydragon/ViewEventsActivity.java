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

    private EventList eventList;
    private EventArrayAdapter eventListAdapter;
    private ListView eventsListView;
    private Fragment fragment;
    private ViewEventsView viewEventsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_events);
        getSupportActionBar().setTitle("Events");

        // Set up admin events listview
        eventList = ((GlobalApp) getApplication()).getEvents();
        eventsListView = findViewById(R.id.adminProfileEventsListview);
        eventListAdapter = new EventArrayAdapter(eventList.getEventList(), this, fragment);
        eventsListView.setAdapter(eventListAdapter);

        viewEventsView = new ViewEventsView(eventList, this);

        // Set up item click listener for ListView
        eventsListView.setOnItemClickListener((adapterView, v, position, l) -> {
            Event event = (Event) adapterView.getItemAtPosition(position);
            //Intent intent = new Intent(ViewEventsActivity.this, AdminEventActivity.class);
            Intent intent = new Intent(ViewEventsActivity.this, EventActivity.class);
            //intent.putExtra("event", event.getId());
            intent.putExtra("eventID", event.getId());
            intent.putExtra("role", "ADMIN");
            startActivity(intent);
        });
    }

    public void notifyAdapter() {

        eventListAdapter.notifyDataSetChanged();

    }

}
