package com.example.luckydragon.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.luckydragon.Fragments.OrganizerEventFragment;
import com.example.luckydragon.Fragments.OrganizerProfileFragment;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.R;
import com.example.luckydragon.Views.ViewEventView;

public class ViewEventActivity extends AppBarActivity {
    private Event event;
    private ViewEventView viewEventView; // need to define view
    // private ViewEventController viewEventController; // need to define controller

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        getSupportActionBar().setTitle("Event");
        //getSupportActionBar().setTitle("Event");

        // I think it is best to pass eventId through the intent since it is completely specific to this activity-- we don't need to store this globally
        //String eventId = getIntent().getStringExtra("eventID");

        /*
        if (eventId == null) {
            throw new RuntimeException("ViewEventActivity cannot display an event with a null event id!");
        }

         */

        // Create view
        GlobalApp globalApp = (GlobalApp) getApplication();
        event = globalApp.getEventToView();
        //event.fetchData(); // get all event data
        viewEventView = new ViewEventView(event, this);

        // Start organizer event fragment
        // TODO when this page is used for both user and organizer event views, then we would have to start an EntrantEventFragment instead for entrants
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.eventFragmentContainer, OrganizerEventFragment.class, null)
                .commit();
    }

    public void updateEventName() {
        TextView nameTextView = findViewById(R.id.eventNameTextView);
        nameTextView.setText(event.getName());
    }

    public void updateEventFacility() {
        TextView facilityTextView = findViewById(R.id.eventFacilityTextView);
        facilityTextView.setText(event.getFacility());
    }

    public void updateEventDateAndTime() {
        TextView dateAndTimeTextView = findViewById(R.id.eventDateAndTimeTextView);
        dateAndTimeTextView.setText(event.getDateAndTime());
    }
}
