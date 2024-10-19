package com.example.luckydragon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class AddEventDialogFragment extends DialogFragment {
    private Integer timeHours;
    private Integer timeMinutes;
    private String date;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflator = requireActivity().getLayoutInflater();

        builder.setView(inflator.inflate(R.layout.dialog_create_event_material, null))
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // create event
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // cancel
                    }
                });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
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
                timeHours = picker.getHour();
                timeMinutes = picker.getMinute();
                if(timeHours == 0) {
                    timeTextView.setText(String.format("%02d:%02d AM", timeHours + 12, timeMinutes));
                }
                else if(timeHours < 12) {
                    timeTextView.setText(String.format("%02d:%02d AM", timeHours, timeMinutes));
                } else if(timeHours == 12) {
                    timeTextView.setText(String.format("%02d:%02d PM", timeHours, timeMinutes));
                } else {
                    timeTextView.setText(String.format("%02d:%02d PM", timeHours - 12, timeMinutes));
                }
            });
            picker.show(getParentFragmentManager(), "Event time picker");
        });


        MaterialTextView dateTextView = dialog.findViewById(R.id.dateTextView);
        dateTextView.setOnClickListener(v -> {
            MaterialDatePicker picker =
                    MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Select Event Date")
                            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                            .build();
            picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                @Override public void onPositiveButtonClick(Long selection) {
                    Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    utc.setTimeInMillis(selection);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    date = format.format(utc.getTime());
                    dateTextView.setText(date);
                }
            });

            picker.show(getParentFragmentManager(), "Event date picker");
        });



    }
}
