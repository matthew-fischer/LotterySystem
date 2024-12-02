package com.example.luckydragon.Controllers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.luckydragon.Fragments.OrganizerEventFragment;
import com.example.luckydragon.Models.Entrant;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.User;
import com.example.luckydragon.R;

import java.util.ArrayList;

public class EntrantArrayAdapter extends ArrayAdapter<User> {
    private Fragment fragment;
    private ArrayList<User> userData;
    private String listType;
    public EntrantArrayAdapter(ArrayList<User> userData, Context context, Fragment fragment, String listType) {
        super(context, 0, userData);
        this.userData = userData;
        this.fragment = fragment;
        this.listType = listType;
    }

    public View getView(int position, View v, ViewGroup parent) {
        View rowView = LayoutInflater.from(getContext()).inflate(R.layout.content_entrant_desc, parent, false);

        User entrant = getItem(position);

        // Set name
        TextView eventNameTextView = rowView.findViewById(R.id.entrantNameTextView);
        ImageButton cancelButton = rowView.findViewById(R.id.cancelIcon);

        if (listType.equals("attendeeList") || listType.equals("cancelledList")) {
            cancelButton.setVisibility(View.GONE);
        } else {
            cancelButton.setVisibility(View.VISIBLE);
        }

        eventNameTextView.setText(entrant.getName());

        // Implement onClickListener for ImageButton for Organizer to remove an Entrant from a specific list.
        cancelButton.setOnClickListener((view) -> {
            OrganizerEventFragment organizerEventFragment = (OrganizerEventFragment) fragment;
            User entrantGettingRemoved = getItem(position);
            if (listType.equals("waitList")) {
                organizerEventFragment.leaveWaitlist(entrantGettingRemoved.getDeviceId());
            }
            if (listType.equals("inviteeList")) {
                organizerEventFragment.leaveInviteelist(entrantGettingRemoved.getDeviceId());
            }
        });

        return rowView;
    }
}
