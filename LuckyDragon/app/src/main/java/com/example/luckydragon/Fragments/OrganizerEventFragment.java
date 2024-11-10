package com.example.luckydragon.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.luckydragon.Controllers.EntrantArrayAdapter;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.User;
import com.example.luckydragon.R;

import java.util.ArrayList;

public class OrganizerEventFragment extends Fragment {
    private Event event;
    private EntrantArrayAdapter entrantArrayAdapter;

    public OrganizerEventFragment() {
        super(R.layout.fragment_organizer_event);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Get event
        GlobalApp globalApp = (GlobalApp) requireActivity().getApplication();
        event = globalApp.getEventToView();
        globalApp.setEventToView(null); // no longer need to store event in globalApp

        // Set up waitlist list view
        ListView waitlistListView = view.findViewById(R.id.eventWaitlistListView);
        // Need a user array adapter
        entrantArrayAdapter = new EntrantArrayAdapter(event.getWaitlistUsers(), requireActivity().getApplicationContext(), this);
        waitlistListView.setAdapter(entrantArrayAdapter);
    }
}
