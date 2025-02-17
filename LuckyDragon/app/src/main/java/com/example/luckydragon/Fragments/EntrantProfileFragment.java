package com.example.luckydragon.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.luckydragon.Activities.EventActivity;
import com.example.luckydragon.Activities.ProfileActivity;
import com.example.luckydragon.Activities.ViewEventActivity;
import com.example.luckydragon.Controllers.EventArrayAdapter;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.EventList;
import com.example.luckydragon.R;
import com.example.luckydragon.Views.EntrantEventsView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;

/**
 * Fragment uses to display Entrant Profile that shows all their information.
 * This is where entrants have the ability to scan a qr code to view/sign-up for an event.
 */
public class EntrantProfileFragment extends Fragment {
    public EntrantProfileFragment() {
        super(R.layout.fragment_entrant_profile);
    }

    private EventList eventList;
    private EntrantEventsView entrantEventsView;

    private EventArrayAdapter attendingListAdapter;
    private EventArrayAdapter waitlistedListAdapter;
    private EventArrayAdapter invitedListAdapter;
    private EventArrayAdapter cancelledListAdapter;

    private ListView attendingEventsListView;
    private ListView waitlistedEventsListView;
    private ListView invitedEventsListView;
    private ListView cancelledEventsListView;

    private ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(new ScanContract(),
                    o -> {
                        if (o.getContents() == null) {
                            Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(getActivity(), ViewEventActivity.class);
                            // Pass in event eventId (from QR CODE SCANNER)
                            String eventID = o.getContents();
                            GlobalApp globalApp = (GlobalApp) requireActivity().getApplication();
                            Event event = globalApp.getEvent(eventID);
                            globalApp.setEventToView(event);

                            // start ViewEventActivity
                            startActivity(intent);
                        }
                    });
    /**
     * Creates an onClickListener for QR Scanning. Fetches all event data and connects to all event listviews.
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        GlobalApp globalApp = ((GlobalApp) requireActivity().getApplication());


        Button scanQRButton = view.findViewById(R.id.scanQRButton);
        // Reference: https://www.geeksforgeeks.org/how-to-read-qr-code-using-zxing-library-in-android/
        scanQRButton.setOnClickListener((View v) -> {
            // This is for testing without having to use the QR scanner.
//            Event event = globalApp.getEvent("jvnkGe6dUhSUubdoWQVp");
//            globalApp.setEventToView(event);
//            startActivity(new Intent(getContext(), ViewEventActivity.class));

            // Launch the QR code scanner
            barcodeLauncher.launch(new ScanOptions());

        });

        // Set up list views for the entrant's attending, waitlisted,
        // invited, and cancelled events and the adapters
        attendingEventsListView = view.findViewById(R.id.entrantProfileAttendingListview);
        waitlistedEventsListView = view.findViewById(R.id.entrantProfileWaitlistedListview);
        invitedEventsListView = view.findViewById(R.id.entrantProfileInvitedListview);
        cancelledEventsListView = view.findViewById(R.id.entrantProfileCancelledListview);

        attendingListAdapter = new EventArrayAdapter(new ArrayList<>(),requireActivity().getApplicationContext(),
                this, "ENTRANT");
        waitlistedListAdapter = new EventArrayAdapter(new ArrayList<>(),requireActivity().getApplicationContext(),
                this, "ENTRANT");
        invitedListAdapter = new EventArrayAdapter(new ArrayList<>(),requireActivity().getApplicationContext(),
                this, "ENTRANT");
        cancelledListAdapter = new EventArrayAdapter(new ArrayList<>(),requireActivity().getApplicationContext(),
                this, "ENTRANT");

        // make each listview use the adapter and set the onclicklistener
        setupAdapter(attendingListAdapter, attendingEventsListView);
        setupAdapter(waitlistedListAdapter, waitlistedEventsListView);
        setupAdapter(invitedListAdapter, invitedEventsListView);
        setupAdapter(cancelledListAdapter, cancelledEventsListView);

        // get all event data
        eventList = ((GlobalApp) requireActivity().getApplication()).getEvents();

        // view that will observe the eventList
        entrantEventsView = new EntrantEventsView(eventList, this);
    }

    /**
     * Calls to update the adapters for each of the four listViews.
     */
    public void notifyAdapter() {
        updateAdapter(attendingListAdapter, attendingEventsListView, "attendeeList");
        updateAdapter(waitlistedListAdapter, waitlistedEventsListView, "waitList");
        updateAdapter(invitedListAdapter, invitedEventsListView, "inviteeList");
        updateAdapter(cancelledListAdapter, cancelledEventsListView, "cancelledList");
    }

    /**
     * Setups an adapter with its corresponding listView and initializes the
     * onItemClickListener for the listView;
     * @param adapter the adapter for the listView
     * @param listView the corresponding listView
     */
    private void setupAdapter(EventArrayAdapter adapter, ListView listView) {
        // set the listview's adapter
        listView.setAdapter(adapter);

        // set the on click listener
        AdapterView.OnItemClickListener listener = (adapterView, v, position, l) -> {
            Event event = (Event) adapterView.getItemAtPosition(position);
            GlobalApp globalApp = ((GlobalApp) requireActivity().getApplication());
            globalApp.setEventToView(event);
            startActivity(new Intent(getContext(), ViewEventActivity.class));
        };
        listView.setOnItemClickListener(listener);
    }

    /**
     * Given an adapter, its listView, and the type of list, updates the
     * adapter to store the current events where the user is in the specified
     * listType.
     * @param adapter the adapter for the listView
     * @param listView the corresponding listView
     * @param listType the type of list, can be one of: <attendee|wait|invitee|cancelled>List
     */
    private void updateAdapter(EventArrayAdapter adapter, ListView listView, String listType) {
        // if the fragment is not on an activity, we don't need to update the adapter
        if (!isAdded()) return;

        ArrayList<Event> eventData = new ArrayList<>();
        String deviceID = ((GlobalApp) requireActivity().getApplication()).getUser().getDeviceId();
        for (Event event : eventList.getEventList()) {
            if ((listType.equals("attendeeList") && event.onAttendeeList(deviceID))
                    ||  (listType.equals("waitList") && event.onWaitList(deviceID))
                    ||  (listType.equals("inviteeList") && event.onInviteeList(deviceID))
                    ||  (listType.equals("cancelledList") && event.onCancelledList(deviceID))) {
                eventData.add(event);
            }
        }
        adapter.clear();
        adapter.addAll(eventData);
        adapter.notifyDataSetChanged();
    }
}