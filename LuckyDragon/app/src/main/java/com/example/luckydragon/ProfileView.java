package com.example.luckydragon;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileView extends Observer {
    private final ProfileActivity profileActivity;

    public ProfileView(User user, ProfileActivity profileActivity) {
        this.profileActivity = profileActivity;
        startObserving(user);
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }

    @Override
    public void update(Observable whoUpdatedMe) {
        if(getObservable().isLoaded()) {
            setName(profileActivity.findViewById(R.id.nameTextView));
            setEmail(profileActivity.findViewById(R.id.emailTextView));
            setPhoneNumber(profileActivity.findViewById(R.id.phoneNumberTextView));
            setProfilePicture(profileActivity.findViewById(R.id.profilePicture));
        }
    }

    /**
     * Sets the name textview to the user's name.
     */
    public void setName(TextView nameTextView) {
        nameTextView.setText(getObservable().getName());
    }

    /**
     * Sets the email textview to the user's email (if they have one).
     * If they don't have an email, hide the email textview.
     */
    public void setEmail(TextView emailTextView) {
        if(getObservable().getEmail() != null) {
            emailTextView.setText(getObservable().getEmail());
        } else {
            emailTextView.setVisibility(View.GONE);
        }
    }

    /**
     * Sets the phone number textview to the user's phone number (if they have one).
     * If they don't have a phone number, hide the phone number textview.
     */
    public void setPhoneNumber(TextView phoneNumber) {
        if(getObservable().getPhoneNumber() != null) {
            phoneNumber.setText(getObservable().getPhoneNumber());
        } else {
            phoneNumber.setVisibility(View.GONE);
        }
    }

    /**
     * Set the profile picture to the user's profile picture.
     */
    public void setProfilePicture(ImageView profilePictureView) {
        profilePictureView.setImageBitmap(getObservable().getProfilePicture());
    }
}
