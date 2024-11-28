/**
 * Defines AddEventDialogFragment which allows an organizer to create an event.
 */

package com.example.luckydragon.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.luckydragon.Activities.ProfileActivity;
import com.example.luckydragon.Controllers.AddEventController;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.User;
import com.example.luckydragon.R;
import com.example.luckydragon.Views.AddEventView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Objects;

/**
 * This fragment alls an organizer the ability to fill in information for an event they want to create.
 * Updates the database based on the organizers input.
 */
public class AddEventDialogFragment extends DialogFragment {
    @Nullable private Integer timeHours = null;
    @Nullable private Integer timeMinutes = null;
    @Nullable private String date = null;
    Event event;
    AddEventController controller;
    AddEventView eventView;
    User user;
    private ActivityResultLauncher<Intent> uploadImageResultLauncher;

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
        TextView posterFile = dialogView.findViewById(R.id.uploadPosterText);
        // Upload Event Poster
        uploadImageResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data == null || data.getData() == null) return;
                            Uri image = data.getData();
                            try (Cursor cursor = getActivity().getContentResolver().query(image,
                                    null, null, null, null)) {
                                if (cursor == null) return;
                                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                cursor.moveToFirst();
                                String file = cursor.getString(nameIndex);

                                Bitmap eventPoster = MediaStore.Images.Media.getBitmap(
                                        getActivity().getContentResolver(), image);

                                // Crop image to correct width
                                int width = eventPoster.getWidth();
                                int height = eventPoster.getHeight();
                                eventPoster = Bitmap.createScaledBitmap(eventPoster, 200,
                                        (200 * height)/width, false);
                                posterFile.setText(file);
                                controller.uploadEventPoster(eventPoster);
                            } catch (Exception e) {
                                Log.e("signup", e.getMessage());
                                Log.e("signup", "error uploading event poster");
                            }
                        }
                    }
                });

        ImageButton uploadPosterButton = dialogView.findViewById(R.id.uploadPosterButton);
        uploadPosterButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            uploadImageResultLauncher.launch(intent);
        });

        ImageButton removePosterButton = dialogView.findViewById(R.id.removePosterButton);
        removePosterButton.setOnClickListener(view -> {
            controller.uploadEventPoster(null);
            posterFile.setText(getString(R.string.createEventPosterText));
        });

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

        // Set up event time picker
        MaterialTextView eventTimeTextView = dialog.findViewById(R.id.eventTimeTextView);
        eventTimeTextView.setOnClickListener(v -> {
            MaterialTimePicker picker =
                    new MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_12H)
                            .setHour(19)
                            .setMinute(00)
                            .setTitleText("Select Event Time")
                            .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                            .build();
            picker.addOnPositiveButtonClickListener(p -> {
                controller.extractEventTime(picker);
            });
            picker.show(getParentFragmentManager(), "Event time picker");
        });

        // Set up event date picker
        MaterialTextView eventDateTextView = dialog.findViewById(R.id.eventDateTextView);
        eventDateTextView.setOnClickListener(v -> {
            MaterialDatePicker<Long> picker =
                    MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Select Event Date")
                            .setSelection(MaterialDatePicker.todayInUtcMilliseconds() + 7 * 86400000L)
                            .build();
            picker.addOnPositiveButtonClickListener(selection -> {
                controller.extractEventDate(selection);
            });
            picker.show(getParentFragmentManager(), "Event date picker");
        });

        // Set up lottery time picker
        MaterialTextView lotteryTimeTextView = dialog.findViewById(R.id.lotteryTimeTextView);
        lotteryTimeTextView.setOnClickListener(v -> {
            MaterialTimePicker picker =
                    new MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_12H)
                            .setHour(19)
                            .setMinute(00)
                            .setTitleText("Select Lottery Time")
                            .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                            .build();
            picker.addOnPositiveButtonClickListener(p -> {
                controller.extractLotteryTime(picker);
            });
            picker.show(getParentFragmentManager(), "Event time picker");
        });

        // Set up lottery date picker
        MaterialTextView lotteryDateTextView = dialog.findViewById(R.id.lotteryDateTextView);
        lotteryDateTextView.setOnClickListener(v -> {
            MaterialDatePicker<Long> picker =
                    MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Select Lottery Date")
                            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                            .build();
            picker.addOnPositiveButtonClickListener(selection -> {
                controller.extractLotteryDate(selection);
            });
            picker.show(getParentFragmentManager(), "Event date picker");
        });
    }
}
