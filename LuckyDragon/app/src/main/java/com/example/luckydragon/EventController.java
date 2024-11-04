package com.example.luckydragon;

import android.graphics.Bitmap;
import android.widget.EditText;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class EventController extends Controller {
    public EventController(Event observable) {
        super(observable);
    }

    @Override
    public Event getObservable() {
        return (Event) super.getObservable();
    }

    public void waitList(String deviceId) {
        // Add deviceID to Waitlist:
        getObservable().waitList(deviceId);
    }

    public void leaveWaitList(String deviceId) {
        getObservable().leaveWaitList(deviceId);
    }

//    public void extractEmail(EditText editEmail) {
//        // TODO: input validation
//        String email = editEmail.getText().toString();
//        getObservable().setEmail(email);
//    }
//
//    public void extractPhoneNumber(EditText editPhone) {
//        // TODO: input validation
//        String phoneNumber = editPhone.getText().toString();
//        getObservable().setPhoneNumber(phoneNumber);
//    }
//
//    public void setNotifications(SwitchMaterial switchNotifications) {
//        getObservable().setNotifications(switchNotifications.isChecked());
//    }
//
//    public void setProfilePicture(Bitmap image) {
//        getObservable().uploadProfilePicture(image);
//    }
//
//    public void becomeEntrant() {
//        getObservable().setEntrant(true);
//    }
}
