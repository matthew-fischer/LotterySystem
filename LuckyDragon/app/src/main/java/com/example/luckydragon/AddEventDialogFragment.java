/**
 * Defines AddEventDialogFragment which allows an organizer to create an event.
 */

package com.example.luckydragon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
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


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Now, inflate view
        View dialogView = inflater.inflate(R.layout.dialog_create_event_material, null);

        ProfileActivity activity = Objects.requireNonNull((ProfileActivity) getActivity(), "Activity is null in AddEventDialogFramgent!");
        String organizerDeviceID = activity.getUser().getDeviceId();
        String organizerName = activity.getUser().getName();
        // We know the user is an organizer if they are adding an event. Thus we can cast to Organizer.
        String facilityName = activity.getUser().getOrganizer().getFacility();

        // MVC
        // TODO: restore an in progress event creation OR, save to db if all data is valid.
        event = ((GlobalApp) activity.getApplication()).makeEvent();
        // Set event attr we know (and before it is observed)
        event.setOrganizerName(organizerName);
        event.setOrganizerDeviceId(organizerDeviceID);
        event.setFacility(facilityName);

        controller = new AddEventController(event, activity);

        Fragment parentFragment = getParentFragment();
        OrganizerProfileFragment organizerProfile = (OrganizerProfileFragment) parentFragment;

        // Set facility text to organizer's facility
        TextInputEditText facilityEditText = dialogView.findViewById(R.id.facilityEditText);
        String facility = activity.getUser().getOrganizer().getFacility();
        facilityEditText.setText(facility);

        return builder.setView(dialogView)
                .setPositiveButton("Create", (dialogInterface, i) -> {
                    Dialog dialog = getDialog();

                    // TODO: make sure we only want to extract event on submit
                    // get fields
                    TextInputEditText eventNameEditText = dialogView.findViewById(R.id.eventNameEditText);
                    TextInputEditText waitlistLimitEditText = dialogView.findViewById(R.id.waitlistLimitEditText);
                    TextInputEditText attendeeLimitEditText = dialogView.findViewById(R.id.attendeeLimitEditText);

                    // extract event (auto added to db)
                    controller.extractName(eventNameEditText);
                    controller.extractWaitLimit(waitlistLimitEditText);
                    controller.extractAttendeeLimit(attendeeLimitEditText);

                    // TODO: Make view reply if event with same info has been created upon save attempt

                    organizerProfile.addEvent(event);
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // cancel
                    }
                }).create();
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
                            .setHour(12)
                            .setMinute(10)
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
    }


}
