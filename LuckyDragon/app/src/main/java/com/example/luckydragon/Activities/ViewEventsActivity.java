package com.example.luckydragon.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.luckydragon.Controllers.EventArrayAdapter;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.EventList;
import com.example.luckydragon.R;
import com.example.luckydragon.Views.ViewEventsView;

/**
 * Activity for viewing a list of all the events.
 * <p>
 *     The ViewEventsActivity displays a list of events in a
 *     ListView with functionality to view details of every
 *     individual event.
 * </p>
 */
public class ViewEventsActivity extends AppBarActivity {

    private EventList eventList;
    private EventArrayAdapter eventListAdapter;
    private ListView eventsListView;
    private ViewEventsView viewEventsView;

    /**
     * Called when the activity is first created. Initializes the view components, including
     * setting up the event list and its adapter, and configuring the list item click behavior.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_events);
        getSupportActionBar().setTitle("Events");

        GlobalApp globalApp = (GlobalApp) getApplication();
        // Set up admin events listview
        eventList = globalApp.getEvents();
        eventsListView = findViewById(R.id.adminProfileEventsListview);
        eventListAdapter = new EventArrayAdapter(eventList.getEventList(), this, null, "ADMINISTRATOR");
        eventsListView.setAdapter(eventListAdapter);

        viewEventsView = new ViewEventsView(eventList, this);

        // Set up item click listener for ListView
        eventsListView.setOnItemClickListener((adapterView, v, position, l) -> {
            Event event = (Event) adapterView.getItemAtPosition(position);
            globalApp.setEventToView(event);
            startActivity(new Intent(this, ViewEventActivity.class));
        });
    }

    /**
     * Notifies the adapter that the data has changed.
     */
    public void notifyAdapter() {
        eventListAdapter.notifyDataSetChanged();
    }

}
