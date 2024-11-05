/**
 * Defines EditFacilityDialogFragment which allows an organizer to edit their facility.
 */

package com.example.luckydragon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditFacilityDialogFragment extends DialogFragment {
    private User user;
    private EditFacilityDialogView editFacilityDialogView;
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

        // Get user
        user = ((GlobalApp) requireActivity().getApplication()).getUser();
        // Create view
        editFacilityDialogView = new EditFacilityDialogView(user, this);

        builder.setView(inflater.inflate(R.layout.dialog_edit_facility_material, null))
                .setTitle(dialogTitle)
                .setPositiveButton("Confirm", (dialogInterface, i) -> {
                    // Get text from input
                    Dialog dialog = getDialog();
                    final TextInputEditText facilityEditText = dialog.findViewById(R.id.edit_facility_FacilityEditText);
                    String facilityName = facilityEditText.getText().toString();

                    // Validate input
                    if(facilityName.isEmpty()) {
                        Toast.makeText(getContext(), "Facility cannot be empty.", Toast.LENGTH_SHORT);
                        return;
                    }

                    // Set facility in user
                    user.getOrganizer().setFacility(facilityName);
                    user.notifyObservers();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    // cancel
                });


        Dialog dialog = builder.create();
        dialog.show();

        // Set clear button click listener
        final TextInputLayout facilityInputLayout = dialog.findViewById(R.id.edit_facility_FacilityTextInputLayout);
        final TextInputEditText facilityEditText = dialog.findViewById(R.id.edit_facility_FacilityEditText);
        facilityInputLayout.setEndIconOnClickListener((v) -> {
            facilityEditText.setText("");
        });

        // Set textedit starting text
        assert user.getOrganizer() != null;
        facilityEditText.setText(user.getOrganizer().getFacility());

        return dialog;
    }


}
