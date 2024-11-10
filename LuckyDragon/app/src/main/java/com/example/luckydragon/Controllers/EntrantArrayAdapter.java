package com.example.luckydragon.Controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.luckydragon.Models.Entrant;
import com.example.luckydragon.Models.User;
import com.example.luckydragon.R;

import java.util.ArrayList;

public class EntrantArrayAdapter extends ArrayAdapter<User> {
    private Fragment fragment;

    public EntrantArrayAdapter(ArrayList<User> waitlistData, Context context, Fragment fragment) {
        super(context, 0, waitlistData);
        this.fragment = fragment;
    }

    public View getView(int position, View v, ViewGroup parent) {
        View rowView = LayoutInflater.from(getContext()).inflate(R.layout.content_entrant_desc, parent, false);

        User entrant = getItem(position);

        // Set name
        TextView eventNameTextView = rowView.findViewById(R.id.entrantNameTextView);
        eventNameTextView.setText(entrant.getName());
        //eventNameTextView.setText(user.getName());

        return rowView;
    }
}
