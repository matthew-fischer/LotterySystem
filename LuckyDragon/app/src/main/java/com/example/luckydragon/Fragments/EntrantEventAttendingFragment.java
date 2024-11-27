package com.example.luckydragon.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.R;

public class EntrantEventAttendingFragment extends Fragment {
    private Event event;

    public EntrantEventAttendingFragment() {
        super(R.layout.fragment_entrant_attending_event);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Get event
        GlobalApp globalApp = (GlobalApp) requireActivity().getApplication();
        event = globalApp.getEventToView();

        // Set text views
        setCurrentAttendeesMessage();
        setAttendeeSpotsMessage();

        // Set up view poster button
        Button viewPosterButton = view.findViewById(R.id.viewEventPosterButton);
        viewPosterButton.setOnClickListener(v -> {
            // There is no poster uploaded
            if (event.getEventPoster() == null) {
                Toast.makeText(getContext(), "Organizer did not upload a poster.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            // Put together arguments for image fragment
            Bundle args = new Bundle();
            args.putString("title", "Event Poster");
            args.putString("negativeButton", "Close");
            args.putParcelable("image", event.getEventPoster());
            // Show image fragment
            DialogFragment displayPosterFragment = new DisplayImageFragment();
            displayPosterFragment.setArguments(args);
            displayPosterFragment.show(getChildFragmentManager(), "DisplayImageFragment");
        });
    }

    private void setCurrentAttendeesMessage() {
        TextView currentAttendeesMessageTextView = getView().findViewById(R.id.currentAttendeesMessage);
        currentAttendeesMessageTextView.setText(String.format("Current Attendees: %d", event.getAttendeeListSize()));
    }

    private void setAttendeeSpotsMessage() {
        TextView attendeeSpotsMessageTextView = getView().findViewById(R.id.attendeeSpotsMessage);
        attendeeSpotsMessageTextView.setText(String.format("Attendee Spots: %d", event.getAttendeeSpots()));
    }
}
