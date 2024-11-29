/*
 * DialogFragment allowing an organizer to edit their facility.
 * Outstanding Issues:
 *   - NONE
 */

/**
 * Defines EditFacilityDialogFragment which allows an organizer to edit their facility.
 */

package com.example.luckydragon.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.luckydragon.Controllers.EditFacilityDialogController;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.User;
import com.example.luckydragon.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Fragment for Organizer to send notifications to users.
 */
public class OrganizerNotificationDialogFragment extends DialogFragment {
    private Event event;

    /**
     * Creates an OrganizerNotificationDialogFragment
     */
    public OrganizerNotificationDialogFragment() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // get event
        GlobalApp globalApp = (GlobalApp) requireActivity().getApplication();
        event = ((GlobalApp) requireActivity().getApplication()).getEventToView();

        builder.setView(inflater.inflate(R.layout.dialog_organizer_send_notif, null))
                .setTitle("Send Notification")
                .setPositiveButton("Send", (dialogInterface, i) -> {
                    Dialog dialog = requireDialog();

                    TextInputEditText titleEditText = dialog.findViewById(R.id.notifTitleEditText);
                    TextInputEditText bodyEditText = dialog.findViewById(R.id.notifBodyEditText);

                    String title = Objects.requireNonNull(titleEditText.getText()).toString();
                    String body = Objects.requireNonNull(bodyEditText.getText()).toString();
                    List<String> userList = getListByRadio(dialog);

                    if (title.isEmpty() || body.isEmpty()) {
                        sendToast("Title and Body cannot be empty.");
                        return;
                    }

                    for (String deviceId : userList) {
                        User user = globalApp.getUserById(deviceId);
                        user.addToNotificationList(title, body);
                    }
                    sendToast("Sent notifications!");
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    // cancel
                });


        Dialog dialog = builder.create();
        dialog.show();

        return dialog;
    }

    /**
     * Gets the correct list of entrants based on the radio button option selected.
     * @param dialog the current dialog fragment
     * @return the list of entrants in the list type selected
     */
    private List<String> getListByRadio(Dialog dialog) {
        if (((RadioButton)dialog.findViewById(R.id.optionWaitList)).isChecked()) {
            return event.getWaitList();
        }
        if (((RadioButton)dialog.findViewById(R.id.optionInviteeList)).isChecked()) {
            return event.getInviteeList();
        }
        if (((RadioButton)dialog.findViewById(R.id.optionAttendeeList)).isChecked()) {
            return event.getAttendeeList();
        }
        if (((RadioButton)dialog.findViewById(R.id.optionCancelledList)).isChecked()) {
            return event.getInviteeList();
        }
        return null;
    }

    public void sendToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
