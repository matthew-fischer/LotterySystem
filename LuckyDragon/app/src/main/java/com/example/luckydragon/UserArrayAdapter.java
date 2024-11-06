package com.example.luckydragon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class UserArrayAdapter extends ArrayAdapter<User> {

    ArrayList<User> userData;
    public UserArrayAdapter(ArrayList<User> userData, Context context) {
        super(context, 0, userData);
        this.userData = userData;
    }

    public View getView(int position, View v, ViewGroup parent) {
        View rowView = LayoutInflater.from(getContext()).inflate(R.layout.content_profile_desc, parent, false);

        // Set values
        User user = getItem(position);
        if(user == null) return rowView;

        TextView userName = rowView.findViewById(R.id.nameProfileAdminView);
        TextView userEmail = rowView.findViewById(R.id.emailProfileAdminView);
        TextView userPhone = rowView.findViewById(R.id.phoneNumberProfileAdminView);
        ImageView userProfilePicture = rowView.findViewById(R.id.profilePictureAdminView);

        userName.setText(user.getName());
        userEmail.setText(user.getEmail());
        userPhone.setText(user.getPhoneNumber());
        userProfilePicture.setImageBitmap(user.getProfilePicture());

        return rowView;
    }

}
