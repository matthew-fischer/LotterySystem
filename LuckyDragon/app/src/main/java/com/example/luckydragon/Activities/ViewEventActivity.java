package com.example.luckydragon.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.luckydragon.Fragments.DisplayQRCodeFragment;
import com.example.luckydragon.Fragments.EntrantEventWaitlistFragment;
import com.example.luckydragon.Fragments.OrganizerEventFragment;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.R;
import com.example.luckydragon.Views.ViewEventView;

/**
 * This is the activity for the view event page.
 * It shows general event information like name, facility, date, and time.
 * Role-specific information is embedded as a fragment (OrganizerEventFragment).
 * Updated by ViewEventView.
 * Issues:
 *   - Need to add edit capability for organizers
 *   - Move entrant event view into a fragment and embed
 *   - Should a controller be added?
 */
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

        // Hide buttons for entrant
        if(globalApp.getRole() == GlobalApp.ROLE.ENTRANT) {
            hideEditButton();
            hideQrCodeButton();
        }

        // Start organizer event fragment
        if(globalApp.getRole() == GlobalApp.ROLE.ENTRANT) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.eventFragmentContainer, EntrantEventWaitlistFragment.class, null)
                    .commit();
        } else if(globalApp.getRole() == GlobalApp.ROLE.ORGANIZER) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.eventFragmentContainer, OrganizerEventFragment.class, null)
                    .commit();
        }

        // Initialize on click listener for qr button
        ImageButton viewQrCodeButton = findViewById(R.id.viewQrCodeButton);
        viewQrCodeButton.setOnClickListener((view) -> {
            // create a displayQRCode Dialog Fragment.
            Bundle args = new Bundle();
            args.putSerializable("event", event);
            DialogFragment displayQRFragment = new DisplayQRCodeFragment();
            displayQRFragment.setArguments(args);
            displayQRFragment.show(getSupportFragmentManager(), "DisplayQRCodeFragment");
        });
    }

    /**
     * Updates the displayed event name.
     */
    public void updateEventName() {
        TextView nameTextView = findViewById(R.id.eventNameTextView);
        nameTextView.setText(event.getName());
    }

    /**
     * Updates the displayed event facility.
     */
    public void updateEventFacility() {
        TextView facilityTextView = findViewById(R.id.eventFacilityTextView);
        facilityTextView.setText(event.getFacility());
    }

    /**
     * Updates the displayed date and time.
     */
    public void updateEventDateAndTime() {
        TextView dateAndTimeTextView = findViewById(R.id.eventDateAndTimeTextView);
        dateAndTimeTextView.setText(event.getDateAndTime());
    }

    /**
     * Hides the QR code button.
     */
    private void hideQrCodeButton() {
        ImageButton viewQrCodeButton = findViewById(R.id.viewQrCodeButton);
        viewQrCodeButton.setVisibility(View.GONE);
    }

    /**
     * Hides the edit button.
     */
    private void hideEditButton() {
        ImageButton editButton = findViewById(R.id.editEventButton);
        editButton.setVisibility(View.GONE);
    }
}
