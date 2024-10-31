/**
 * Defines EditFacilityDialogFragment which allows an organizer to edit their facility.
 */

package com.example.luckydragon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditFacilityDialogFragment extends DialogFragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String dialogTitle = null;

    public EditFacilityDialogFragment() {}

    public EditFacilityDialogFragment(String title) {
        this.dialogTitle = title;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        ProfileActivity activity = (ProfileActivity)requireActivity();
        User user = activity.getUser();

        builder.setView(inflater.inflate(R.layout.dialog_edit_facility_material, null))
                .setTitle(dialogTitle)
                .setPositiveButton("Confirm", (dialogInterface, i) -> {
                    // Get text from input
                    Dialog dialog = getDialog();
                    final TextInputEditText facilityEditText = dialog.findViewById(R.id.edit_facility_FacilityEditText);
                    String facilityName = facilityEditText.getText().toString();

                    // Validate input
                    if(facilityName.isEmpty()) return;

                    // Update facility in Organizer class
                    user.setFacility(facilityName);

                    // Update facility in user document
                    db.collection("users").document(user.getDeviceID()).update("Facility", facilityName);

                    // Update facility textview in OrganizerProfileFragment
                    OrganizerProfileFragment parent = (OrganizerProfileFragment) requireParentFragment();
                    parent.setFacilityTextView(facilityName);
                    // If facility was not previously set, change the button icon to edit button
                    parent.setFacilityButtonIcon(R.drawable.baseline_edit_24);
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    // cancel
                });


        Dialog dialog = builder.create();
        dialog.show();

        final TextInputLayout facilityInputLayout = dialog.findViewById(R.id.edit_facility_FacilityTextInputLayout);
        final TextInputEditText facilityEditText = dialog.findViewById(R.id.edit_facility_FacilityEditText);

        // Set text to existing facility value
        facilityEditText.setText(user.getFacility());

        // Set clear button click listener
        facilityInputLayout.setEndIconOnClickListener((v) -> {
            facilityEditText.setText("");
        });

        return dialog;
    }
}
