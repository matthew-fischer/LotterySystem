package com.example.luckydragon.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.luckydragon.Controllers.EventController;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.R;

/**
 * Fragment showing information for an entrant who has been invited to an event.
 */
public class EntrantEventInvitedFragment extends Fragment {
    private Event event;
    private EventController eventController;
    private String deviceId;

    /**
     * Creates an EntrantEventInvitedFragment.
     */
    public EntrantEventInvitedFragment() {
        super(R.layout.fragment_entrant_invited_event);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Get event
        GlobalApp globalApp = (GlobalApp) requireActivity().getApplication();
        event = globalApp.getEventToView();

        // Get device id
        deviceId = globalApp.getUser().getDeviceId();

        // Create controller
        eventController = new EventController(event);

        // Set up accept button on click listener
        Button acceptButton = view.findViewById(R.id.invitationAcceptButton);
        acceptButton.setOnClickListener(v -> {
            eventController.acceptInvitation(deviceId);
            // replace with attending fragment
            getParentFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.eventFragmentContainer, EntrantEventAttendingFragment.class, null)
                    .commit();
        });

        // Set up decline button on click listener
        Button declineButton = view.findViewById(R.id.invitationDeclineButton);
        declineButton.setOnClickListener(v -> {
            eventController.declineInvitation(deviceId);
            // replace with closed fragment
            Fragment closedFragment = new Fragment(R.layout.fragment_entrant_closed_event);
            getParentFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.eventFragmentContainer, closedFragment, null)
                    .commit();
        });
    }
}
