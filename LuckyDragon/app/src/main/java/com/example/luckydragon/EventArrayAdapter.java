package com.example.luckydragon;

import android.app.Activity;
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
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class EventArrayAdapter extends ArrayAdapter<Event> {
    ArrayList<Event> eventData;
    private Fragment fragment;
    public EventArrayAdapter(ArrayList<Event> eventData, Context context, Fragment fragment) {
        super(context, 0, eventData);
        this.eventData = eventData;
        this.fragment = fragment;
    }

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

        // Implement onClickListener for ImageButton to show QR Code corresponding to eventID.
        displayQRCode.setOnClickListener((view) -> {
            // create a displayQRCode Dialog Fragment.
            Bundle args = new Bundle();
            args.putSerializable("event", event);
            DialogFragment displayQRFragment = new DisplayQRCodeFragment();
            displayQRFragment.setArguments(args);
            displayQRFragment.show(fragment.getParentFragmentManager(), "DisplayQRCodeFragment");
        });

        return rowView;
    }
}
