package com.example.luckydragon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Custom ArrayAdapter for displaying a list of User objects within a ListView.
 * <p>
 *     This adapter is responsible for inflating the custom view for each user and
 *     populating it with details such as the user's name, email, phone number,
 *     and profile picture.
 * </p>
 */
public class UserArrayAdapter extends ArrayAdapter<User> {

    ArrayList<User> userData;
    public UserArrayAdapter(ArrayList<User> userData, Context context) {
        super(context, 0, userData);
        this.userData = userData;
    }

    /**
     * Provides a view for an AdapterView.
     * <p>
     *     Inflates the custom layout for each item in the list and populates
     *     it with details such as the user's name, email, phone number,
     *     and profile picture.
     * </p>
     * @param position The position of the item within the adapter
     * @param v The old view
     * @param parent The parent that this view is attached to
     * @return A view corresponding to the data at the specified position
     */
    public View getView(int position, View v, ViewGroup parent) {
        // Inflate the custom view for the user row
        View rowView = LayoutInflater.from(getContext()).inflate(R.layout.content_profile_desc, parent, false);

        // Set values
        User user = getItem(position);
        if(user == null) return rowView;

        // Populate the user details
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
