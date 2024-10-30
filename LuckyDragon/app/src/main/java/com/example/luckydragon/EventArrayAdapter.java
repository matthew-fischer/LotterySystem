package com.example.luckydragon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EventArrayAdapter extends ArrayAdapter<Event> {
    ArrayList<Event> eventData;
    public EventArrayAdapter(ArrayList<Event> eventData, Context context) {
        super(context, 0, eventData);
        this.eventData = eventData;
    }

    public View getView(int position, View v, ViewGroup parent) {
        View rowView = LayoutInflater.from(getContext()).inflate(R.layout.content_event_desc, parent, false);

        // Set values
        Event event = getItem(position);
        if(event == null) return rowView;

        TextView eventNameTextView = rowView.findViewById(R.id.eventRowEventName);
        TextView eventDateTimeTextView = rowView.findViewById(R.id.eventRowEventDateTime);

        eventNameTextView.setText(event.getName());
        eventDateTimeTextView.setText(event.getDateAndTime());

        return rowView;
    }
}
