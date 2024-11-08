package com.example.luckydragon.Controllers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.luckydragon.Fragments.DisplayQRCodeFragment;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.R;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Custom ArrayAdapter for displaying a list of Event objects within a ListView.
 * <p>
 *     This adapter is responsible for inflating the custom view for each event and handling
 *     specific interactions within each row, such as displaying a QR code for an event.
 *     The QR code is displayed or hidden based on the user's role.
 * </p>
 */
public class EventArrayAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> eventData;
    private Fragment fragment;
    private String role;
    public EventArrayAdapter(ArrayList<Event> eventData, Context context, Fragment fragment, String role) {
        super(context, 0, eventData);
        this.eventData = eventData;
        this.fragment = fragment;
        this.role = role;
    }

    /**
     * Provides a view for an AdapterView.
     * <p>
     *     Inflates the custom layout for each item in the list and populates it with
     *     the event's details such as name, date/time and QR code if user's role is not .
     *     "ADMINISTRATOR".
     * </p>
     * @param position The position of the item within the adapter's data set
     * @param v The old view
     * @param parent The parent that this view is attached to
     * @return A view corresponding to the data at the specified position
     */
    public View getView(int position, View v, ViewGroup parent) {
        View rowView = LayoutInflater.from(getContext()).inflate(R.layout.content_event_desc, parent, false);

        // Set values
        Event event = getItem(position);
        if(event == null) return rowView;

        TextView eventNameTextView = rowView.findViewById(R.id.eventRowEventName);
        TextView eventDateTimeTextView = rowView.findViewById(R.id.eventRowEventDateTime);
        ImageButton displayQRCode = rowView.findViewById(R.id.qrCodeIcon);

        eventNameTextView.setText(event.getName());
        eventDateTimeTextView.setText(event.getDateAndTime());

        // Hide QR Code for admin browsing events
        if (Objects.equals(role, "ADMINISTRATOR")) {
            displayQRCode.setVisibility(View.GONE);
        }

        // Implement onClickListener for ImageButton to show QR Code corresponding to eventID.
        displayQRCode.setOnClickListener((view) -> {
            if (fragment != null) {
                // create a displayQRCode Dialog Fragment.
                Bundle args = new Bundle();
                args.putSerializable("event", event);
                DialogFragment displayQRFragment = new DisplayQRCodeFragment();
                displayQRFragment.setArguments(args);
                displayQRFragment.show(fragment.getParentFragmentManager(), "DisplayQRCodeFragment");
            }
        });

        return rowView;
    }

    public void setEventData(ArrayList<Event> eventData) {
        this.eventData = eventData;
    }
}
