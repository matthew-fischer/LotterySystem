package com.example.luckydragon;

import android.graphics.Bitmap;
import android.widget.EditText;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SignupController extends Controller {
    public SignupController(User observable) {
        super(observable);
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }

    public void extractName(EditText editName) throws RuntimeException {
        String name = editName.getText().toString().trim();
        if (name.isEmpty()) throw new RuntimeException("Name is required.");
        getObservable().setName(name);
    }

    public void extractEmail(EditText editEmail) throws RuntimeException {
        String email = editEmail.getText().toString().trim();
        if (email.isEmpty()) throw new RuntimeException("Email is required.");
        getObservable().setEmail(email);
    }

    public void extractPhoneNumber(EditText editPhone) {
        // TODO: input validation
        String phoneNumber = editPhone.getText().toString().trim();
        getObservable().setPhoneNumber(phoneNumber);
    }

    public void setNotifications(SwitchMaterial switchNotifications) {
        getObservable().setNotifications(switchNotifications.isChecked());
    }

    public void setProfilePicture(Bitmap image) {
        getObservable().setUploadedProfilePicture(image);
    }

    public void becomeEntrant() {
        getObservable().setEntrant(true);
    }
}
