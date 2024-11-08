/**
 * Defines AddEventDialogFragment which allows an organizer to create an event.
 */

package com.example.luckydragon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AddEventDialogFragment extends DialogFragment {
    @Nullable private Integer timeHours = null;
    @Nullable private Integer timeMinutes = null;
    @Nullable private String date = null;
    Event event;
    AddEventController controller;
    AddEventView eventView;
    User user;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get user
        user = ((GlobalApp) requireActivity().getApplication()).getUser();
        assert user.getOrganizer() != null; // user must be an organizer by this point

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Now, inflate view
        View dialogView = inflater.inflate(R.layout.dialog_create_event_material, null);
        // Get profile activity
        ProfileActivity activity = (ProfileActivity) requireActivity();
        // Get user info
        String organizerDeviceID = user.getDeviceId();
        String organizerName = user.getName();
        String facilityName = user.getOrganizer().getFacility();

        // MVC
        // TODO: restore an in progress event creation OR, save to db if all data is valid.
        event = ((GlobalApp) requireActivity().getApplication()).makeEvent();
        // Set event attr we know (and before it is observed)
        event.setOrganizerDeviceId(organizerDeviceID);
        event.setOrganizerName(organizerName);
        event.setFacility(facilityName);

        controller = new AddEventController(event, activity);

        Fragment parentFragment = getParentFragment();
        OrganizerProfileFragment organizerProfile = (OrganizerProfileFragment) parentFragment;

        // Set facility text to organizer's facility
        TextInputEditText facilityEditText = dialogView.findViewById(R.id.facilityEditText);
        String facility = user.getOrganizer().getFacility();
        facilityEditText.setText(facility);

        return builder.setView(dialogView)
                .setPositiveButton("Create", (dialogInterface, i) -> {
                    // TODO: make sure we only want to extract event on submit
                    // get fields
                    TextInputEditText eventNameEditText = dialogView.findViewById(R.id.eventNameEditText);
                    TextInputEditText waitlistLimitEditText = dialogView.findViewById(R.id.waitlistLimitEditText);
                    TextInputEditText attendeeLimitEditText = dialogView.findViewById(R.id.attendeeLimitEditText);
                    SwitchMaterial hasGeolocationSwitch = dialogView.findViewById(R.id.geolocation_switch);

                    // extract event (auto added to db)
                    controller.extractName(eventNameEditText);
                    controller.extractWaitLimit(waitlistLimitEditText);
                    controller.extractAttendeeLimit(attendeeLimitEditText);
                    controller.extractHasGeolocation(hasGeolocationSwitch);

                    // TODO: Make view reply if event with same info has been created upon save attempt

                    Log.e("TIME", event.getTime12h());

                    user.getOrganizer().addEvent(event);
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // cancel
                        event.deleteEventFromDb();
                    }
                })
                .create();
    }

    // if dialog is cancelled, make sure the event does not remain in the db
    @Override
    public void onCancel(final DialogInterface dialog) {
        event.deleteEventFromDb();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        Objects.requireNonNull(dialog);

        // make view
        eventView = new AddEventView(event, this);

        // Set up time picker
        MaterialTextView timeTextView = dialog.findViewById(R.id.timeTextView);
        timeTextView.setOnClickListener(v -> {
            MaterialTimePicker picker =
                    new MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_12H)
                            .setHour(19)
                            .setMinute(00)
                            .setTitleText("Select Event Time")
                            .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                            .build();
            picker.addOnPositiveButtonClickListener(p -> {
                controller.extractTime(picker);
            });
            picker.show(getParentFragmentManager(), "Event time picker");
        });

        // Set up date picker
        MaterialTextView dateTextView = dialog.findViewById(R.id.dateTextView);
        dateTextView.setOnClickListener(v -> {
            MaterialDatePicker<Long> picker =
                    MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Select Event Date")
                            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                            .build();
            picker.addOnPositiveButtonClickListener(selection -> {
                controller.extractDate(selection);
            });
            picker.show(getParentFragmentManager(), "Event date picker");
        });
        Log.d("TONY", event.getOrganizerDeviceId());
    }
}
