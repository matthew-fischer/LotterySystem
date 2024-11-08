package com.example.luckydragon;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.luckydragon.Controller;
import com.example.luckydragon.ProfileActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.time.Instant;

public class AddEventController extends Controller {
    ProfileActivity activity;

    public AddEventController(Event observable, ProfileActivity activity) {
        super(observable);
        this.activity = activity;
    }

    @Override
    public Event getObservable() {
        return (Event) super.getObservable();
    }

    public void extractName(EditText et) {
        String eventName = et.getText().toString();
        Log.d("EXTRACTING", eventName);

        // Validate input
        if (eventName.isEmpty()) {
            activity.sendToast("Fields cannot be empty!");
            return;
        }

        getObservable().setName(eventName);
    }

    public void extractWaitLimit(EditText et) {
        String waitListLimitStr = et.getText().toString();
        // TODO: enforce integer
        int waitListLimit = -1;
        if (!waitListLimitStr.isEmpty()) {
            waitListLimit = Integer.parseInt(waitListLimitStr);
        }
        getObservable().setWaitListLimit(waitListLimit);
    }

    public void extractAttendeeLimit(EditText et) {
        String attendeeLimitStr = et.getText().toString();
        if (attendeeLimitStr.isEmpty()) {
            activity.sendToast("Fields cannot be empty!");
            return;
        }
        Integer attendeeLimit = Integer.valueOf(attendeeLimitStr);
        getObservable().setAttendeeLimit(attendeeLimit);
    }

    public void extractTime(MaterialTimePicker picker) {
        int timeHours = picker.getHour();
        int timeMinutes = picker.getMinute();
        getObservable().setTime(timeHours, timeMinutes);
    }

    public void extractDate(Long selection) {
        Instant dateInstant = Instant.ofEpochMilli(selection);
        String date = dateInstant.toString().substring(0, 10);
        getObservable().setDate(date);
    }

    public void extractHasGeolocation(SwitchMaterial toggle) {
        getObservable().setHasGeolocation(toggle.isEnabled());
    }
}
