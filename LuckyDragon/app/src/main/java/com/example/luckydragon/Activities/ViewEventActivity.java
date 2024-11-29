package com.example.luckydragon.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.luckydragon.Fragments.AdminEventFragment;
import com.example.luckydragon.Fragments.DisplayImageFragment;
import com.example.luckydragon.Fragments.DisplayQRCodeFragment;
import com.example.luckydragon.Fragments.EntrantEventAttendingFragment;
import com.example.luckydragon.Fragments.EntrantEventInvitedFragment;
import com.example.luckydragon.Fragments.EntrantEventWaitlistFragment;
import com.example.luckydragon.Fragments.OrganizerEventFragment;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.User;
import com.example.luckydragon.R;
import com.example.luckydragon.Views.ViewEventView;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        getSupportActionBar().setTitle("Event");

        // Create view
        GlobalApp globalApp = (GlobalApp) getApplication();
        event = globalApp.getEventToView();
        event.setIsLoaded(false); // set isLoaded to false to wait until we fetch again
        event.fetchData(); // get all event data
        boolean forceHideQR = globalApp.getRole() == GlobalApp.ROLE.ENTRANT;
        viewEventView = new ViewEventView(event, this, forceHideQR);


        // Start child fragment
        loadChildFragment();

        // Initialize on click listener for qr button
        ImageButton viewQrCodeButton = findViewById(R.id.viewQrCodeButton);
        viewQrCodeButton.setOnClickListener((view) -> {

            Bundle args = new Bundle();
            args.putParcelable("image", event.createBitMap(event.getQRBitMatrix()));
            args.putString("title", event.getQRBitMatrix() == null ? "No QR Code" :
                    "QR Code for Event:");
            args.putString("negativeButton", "Close");
            DialogFragment displayQRFragment = new DisplayImageFragment();
            displayQRFragment.setArguments(args);
            displayQRFragment.show(getSupportFragmentManager(), "DisplayQRCodeFragment");
        });

        ImageButton viewPosterButton = findViewById(R.id.viewPosterButton);
        viewPosterButton.setOnClickListener(v -> {
            Bundle args = new Bundle();

            // Put together arguments for image fragment
            args.putString("title",
                    event.getEventPoster() != null ? "Event Poster" : "No Event Poster");
            args.putString("negativeButton", "Close");
            args.putParcelable("image", event.getEventPoster());
            // Show image fragment
            DialogFragment displayPosterFragment = new DisplayImageFragment();
            displayPosterFragment.setArguments(args);
            displayPosterFragment.show(getSupportFragmentManager()  , "DisplayImageFragment");
        });
    }

    @Override
    protected void onDestroy() {
        viewEventView.stopObserving();
        super.onDestroy();
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
    public void hideQrCodeButton() {
        ImageButton viewQrCodeButton = findViewById(R.id.viewQrCodeButton);
        viewQrCodeButton.setVisibility(View.GONE);
    }

    /**
     * Shows the QR code button.
     */
    public void showQrCodeButton() {
        ImageButton viewQrCodeButton = findViewById(R.id.viewQrCodeButton);
        viewQrCodeButton.setVisibility(View.VISIBLE);
    }

    public void loadChildFragment() {
        if(event.isLoaded()) {
            GlobalApp globalApp = (GlobalApp) getApplication();
            String deviceId = globalApp.getUser().getDeviceId();
            if(globalApp.getRole() == GlobalApp.ROLE.ENTRANT) {
                if(event.onInviteeList(deviceId)) {
                    // Start EntrantEventInvitedFragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.eventFragmentContainer, EntrantEventInvitedFragment.class, null)
                            .commit();
                } else if(event.onAttendeeList(deviceId)) {
                    // Start EntrantEventAttendingFragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.eventFragmentContainer, EntrantEventAttendingFragment.class, null)
                            .commit();
                } else if(event.onCancelledList(deviceId)) {
                    // Start fragment showing that event is closed
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.eventFragmentContainer, new Fragment(R.layout.fragment_entrant_closed_event), null)
                            .commit();
                } else {
                    // Start EntrantEventWaitlistFragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.eventFragmentContainer, EntrantEventWaitlistFragment.class, null)
                            .commit();
                }
            } else if(globalApp.getRole() == GlobalApp.ROLE.ORGANIZER) {
                // start organizer event fragment
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.eventFragmentContainer, OrganizerEventFragment.class, null)
                        .commit();
            } else if(globalApp.getRole() == GlobalApp.ROLE.ADMINISTRATOR) {
                // start admin event fragment
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.eventFragmentContainer, AdminEventFragment.class, null)
                        .commit();
            }
        }
    }

    /**
     * Samples attendees if the waitlist period has passed and they have not been sampled yet.
     * Will sample replacement entrants if has sampled yet and the attendee limit has not been
     * reached.
     */
    public void sampleAttendeesIfNeccessary() {
        if(!event.isLoaded()) return;
        if(event == null || event.getCreatedTimeMillis() == null) return;

        GlobalApp globalApp = (GlobalApp) getApplication();
        if(!event.haveInviteesBeenSelected()) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDate lotteryDate = LocalDate.parse(event.getLotteryDate());
            LocalTime lotteryTime = LocalTime.of(event.getLotteryHours(), event.getLotteryMinutes());
            LocalDateTime lotteryDateTime = LocalDateTime.of(lotteryDate, lotteryTime);
            if(currentDateTime.isAfter(lotteryDateTime)) {
                event.selectInviteesFirstTime();

                String notSelectedTitle = "Update re - " + event.getName();
                String notSelectedBody = "You have unfortunately not been selected to attend " +
                        "the '" + event.getName() + "' event. You will remain on the waitlist " +
                        "and may possibly be selected if another entrant declines to attend.";
                for (String deviceId : event.getWaitList()) {
                    User notSelectedUser = globalApp.getUserById(deviceId);
                    notSelectedUser.addToNotificationList(notSelectedTitle, notSelectedBody);
                }

                String selectedTitle = "Congratulations!";
                String selectedBody = "You have been selected to attend the '" + event.getName() +
                        "' event! Please open the event in the app and accept the invitation " +
                        "for the event if you would like to attend.";
                for (String deviceId : event.getInviteeList()) {
                    User selectedUser = globalApp.getUserById(deviceId);
                    selectedUser.addToNotificationList(selectedTitle, selectedBody);
                }
            }
        } else {
            // check to see if we can fill replacement invitees if there are spots left
            ArrayList<String> replacementInvitees = event.fillInvitees();

            String replacedTitle = "Congratulations!";
            String replacedBody = "You have been selected to attend the '" + event.getName() +
                    "' event as another entrant has declined their invitation. Please open the " +
                    "event in the app and accept the invitation if you would like to attend.";
            for (String deviceId : replacementInvitees) {
                User replacedUser = globalApp.getUserById(deviceId);
                replacedUser.addToNotificationList(replacedTitle, replacedBody);
            }
        }
        // reload child fragment since we now want to show invitee fragment instead of waitlist fragment
        loadChildFragment();
    }
}
