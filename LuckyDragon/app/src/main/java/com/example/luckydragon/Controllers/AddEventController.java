package com.example.luckydragon.Controllers;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.EditText;

import com.example.luckydragon.Activities.ProfileActivity;
import com.example.luckydragon.Models.Event;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * This class is a controller for the AddEventDialogFragment. Handles extracting
 * user input for the event and updating the Event model.
 */
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

    /**
     * Extracts the event name from the EditText and updates the model
     * @param et the editText for the event name field
     */
    public void extractName(EditText et) {
        String eventName = et.getText().toString();

        // Validate input
        if (eventName.isEmpty()) {
            activity.sendToast("Fields cannot be empty!");
            return;
        }

        getObservable().setName(eventName);
    }

    /**
     * Extracts the waitlist limit from the EditText and updates the model.
     * @param et the EditText for the waitlist limit
     */
    public void extractWaitLimit(EditText et) {
        String waitListLimitStr = et.getText().toString();

        int waitListLimit = -1;
        if (!waitListLimitStr.isEmpty()) {
            waitListLimit = Integer.parseInt(waitListLimitStr);
        }
        getObservable().setWaitListLimit(waitListLimit);
    }

    /**
     * Extracts the attendee limit from the EditText and updates the model.
     * @param et the EditText for the attendee limit
     */
    public void extractAttendeeLimit(EditText et) {
        String attendeeLimitStr = et.getText().toString();
        if (attendeeLimitStr.isEmpty()) {
            activity.sendToast("Fields cannot be empty!");
            return;
        }
        Integer attendeeLimit = Integer.valueOf(attendeeLimitStr);
        getObservable().setAttendeeLimit(attendeeLimit);
    }

    /**
     * Extracts the time from the MaterialTimePicker and updates the event time in the model.
     * @param picker the MaterialTimePicker for the event time
     */
    public void extractEventTime(MaterialTimePicker picker) {
        int timeHours = picker.getHour();
        int timeMinutes = picker.getMinute();
        getObservable().setTime(timeHours, timeMinutes);
    }

    /**
     * Extracts the date from a EPOCH timestamp and updates the event date in the model.
     * @param selection the time in milliseconds since epoch of the date
     */
    public void extractEventDate(Long selection) {
        Instant dateInstant = Instant.ofEpochMilli(selection);
        String date = dateInstant.toString().substring(0, 10);
        getObservable().setDate(date);
    }

    /**
     * Extracts the time from the MaterialTimePicker and updates the event time in the model.
     * @param picker the MaterialTimePicker for the event time
     */
    public void extractLotteryTime(MaterialTimePicker picker) {
        int timeHours = picker.getHour();
        int timeMinutes = picker.getMinute();
        getObservable().setLotteryTime(timeHours, timeMinutes);
    }

    /**
     * Extracts the date from a EPOCH timestamp and updates the lottery date in the model..
     * @param selection the time in milliseconds since epoch of the date
     */
    public void extractLotteryDate(Long selection) {
        Instant dateInstant = Instant.ofEpochMilli(selection);
        String date = dateInstant.toString().substring(0, 10);
        getObservable().setLotteryDate(date);
    }

    /**
     * Extracts the geolocation switch from the SwitchMaterial and updates the model.
     * @param toggle the SwitchMaterial of the geolocation switch
     */
    public void extractHasGeolocation(SwitchMaterial toggle) {
        getObservable().setHasGeolocation(toggle.isChecked());
    }

    /**
     * Extracts the creation time of the event.
     */
    public void extractCreatedTimeMillis() {
        getObservable().setCreatedTimeMillis(System.currentTimeMillis());
    }

    public void uploadEventPoster(Bitmap bitmap) {
        getObservable().setEventPoster(bitmap);
    }
}
