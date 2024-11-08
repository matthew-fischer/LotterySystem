package com.example.luckydragon;

import android.graphics.Bitmap;
import android.widget.EditText;
import com.google.android.material.switchmaterial.SwitchMaterial;

/**
 * This class is a controller for the SignupActivity. Handles extracting
 * user input from the various TextViews and updating the corresponding
 * User model.
 */
public class SignupController extends Controller {
    public SignupController(User observable) {
        super(observable);
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }

    /**
     * Extracts name from the EditText and updates the User model.
     * @param editName the editText field
     * @throws RuntimeException thrown if name is empty
     */
    public void extractName(EditText editName) throws RuntimeException {
        String name = editName.getText().toString().trim();
        if (name.isEmpty()) throw new RuntimeException("Name is required.");
        getObservable().setName(name);
    }

    /**
     * Extracts email from the EditText and updates the User model.
     * @param editEmail the editText field
     * @throws RuntimeException thrown if email is empty
     */
    public void extractEmail(EditText editEmail) throws RuntimeException {
        String email = editEmail.getText().toString().trim();
        if (email.isEmpty()) throw new RuntimeException("Email is required.");
        getObservable().setEmail(email);
    }

    /**
     * Extracts phone from the EditText and updates the User model.
     * @param editPhone the editText field
     */
    public void extractPhoneNumber(EditText editPhone) {
        // TODO: input validation
        String phoneNumber = editPhone.getText().toString().trim();
        getObservable().setPhoneNumber(phoneNumber);
    }

    /**
     * Extracts notifications from the SwitchMaterial and updates the User model.
     * @param switchNotifications the switchNotifications field
     */
    public void setNotifications(SwitchMaterial switchNotifications) {
        getObservable().setNotifications(switchNotifications.isChecked());
    }

    /**
     * Extracts the profile picture from the Bitmap.
     * @param image the profile picture image
     */
    public void setProfilePicture(Bitmap image) {
        getObservable().setUploadedProfilePicture(image);
    }

    /**
     * Sets the User to be both an Entrant and Organizer.
     */
    public void becomeEntrantAndOrganizer() {
        getObservable().setEntrant(true);
        getObservable().setOrganizer(true);
    }
}
