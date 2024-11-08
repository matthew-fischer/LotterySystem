/*
 * Defines ProfileView which manages ProfileActivity.
 * Updates the information shown by ProfileActivity when triggered by a change in User.
 * ISSUES:
 *   NONE
 */

package com.example.luckydragon.Views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luckydragon.Activities.ProfileActivity;
import com.example.luckydragon.Models.Observable;
import com.example.luckydragon.Models.User;
import com.example.luckydragon.R;

/**
 * This is the view that updates ProfileActivity.
 * When the User is modified, ProfileView's `update` method is triggered.
 * This updates the name, email, phone number, and profile picture shown by ProfileActivity.
 */
public class ProfileView extends Observer {
    private final ProfileActivity profileActivity;

    /**
     * Creates a ProfileView.
     * @param user the application user
     * @param profileActivity the ProfileActivity to be updated by this view
     */
    public ProfileView(User user, ProfileActivity profileActivity) {
        this.profileActivity = profileActivity;
        startObserving(user);
    }

    /**
     * Updaets ProfileActivity when triggered by a change in the User.
     * @param whoUpdatedMe the observable who triggered the change (will be a User)
     */
    @Override
    public void update(Observable whoUpdatedMe) {
        if(getObservable().isLoaded()) {
            setName(profileActivity.findViewById(R.id.nameTextView));
            setEmail(profileActivity.findViewById(R.id.emailTextView));
            setPhoneNumber(profileActivity.findViewById(R.id.phoneNumberTextView));
            setProfilePicture(profileActivity.findViewById(R.id.profilePicture));
        }
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }

    public void setName(TextView nameTextView) {
        nameTextView.setText(getObservable().getName());
    }

    public void setEmail(TextView emailTextView) {
        if(getObservable().getEmail() != null) {
            emailTextView.setText(getObservable().getEmail());
        } else {
            emailTextView.setVisibility(View.GONE);
        }
    }

    public void setPhoneNumber(TextView phoneNumber) {
        if(getObservable().getPhoneNumber() != null) {
            phoneNumber.setText(getObservable().getPhoneNumber());
        } else {
            phoneNumber.setVisibility(View.GONE);
        }
    }

    public void setProfilePicture(ImageView profilePictureView) {
        profilePictureView.setImageBitmap(getObservable().getProfilePicture());
    }
}
