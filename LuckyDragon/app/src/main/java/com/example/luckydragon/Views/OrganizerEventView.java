package com.example.luckydragon.Views;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.luckydragon.Fragments.OrganizerEventFragment;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.Observable;
import com.example.luckydragon.Models.UserList;
import com.example.luckydragon.R;

/**
 * This is the view that updates OrganizerEventFragment.
 * When the event is modified, the update() method is triggered.
 * This updates the event details in the OrganizerEventFragment.
 */
public class OrganizerEventView extends Observer {
    private final OrganizerEventFragment organizerEventFragment;

    /**
     * Creates an OrganizerEventView. This observes the list of users in the various lists
     * in the event.
     * @param users the users being displayed
     * @param organizerEventFragment the fragment displaying the event and its list of users
     */
    public OrganizerEventView(UserList users, OrganizerEventFragment organizerEventFragment) {
        this.organizerEventFragment = organizerEventFragment;
        startObserving(users);
    }

    /**
     * Update event details in OrganizerEventFragment.
     * @param whoUpdatedMe the observable who triggered the update (Event)
     */
    @Override
    public void update(Observable whoUpdatedMe) {
        organizerEventFragment.updateWaitlistCapacity();
        organizerEventFragment.notifyAdapter();

        TextView noWaitlisteesTextView = organizerEventFragment.getView().findViewById(R.id.noWaitlisteesTextView);
        if (organizerEventFragment.waitListUsersAdapter.getCount() == 0) {
            noWaitlisteesTextView.setVisibility(View.VISIBLE);
        } else {
            noWaitlisteesTextView.setVisibility(View.GONE);
        }
        TextView noInviteelisteesTextView = organizerEventFragment.getView().findViewById(R.id.noinviteesTextView);
        if (organizerEventFragment.inviteeListUsersAdapter.getCount() == 0) {
            noInviteelisteesTextView.setVisibility(View.VISIBLE);
        } else {
            noInviteelisteesTextView.setVisibility(View.GONE);
        }
        TextView noCancelledisteesTextView = organizerEventFragment.getView().findViewById(R.id.noCancelledTextView);
        if (organizerEventFragment.cancelledListUsersAdapter.getCount() == 0) {
            noCancelledisteesTextView.setVisibility(View.VISIBLE);
        } else {
            noCancelledisteesTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public UserList getObservable() {
        return (UserList) super.getObservable();
    }
}
