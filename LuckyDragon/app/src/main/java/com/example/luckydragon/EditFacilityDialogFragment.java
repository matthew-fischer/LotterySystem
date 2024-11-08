/*
 * DialogFragment allowing an organizer to edit their facility.
 * Outstanding Issues:
 *   - NONE
 */

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

/**
 * Defines EditFacilityDialogFragment.
 * Interacts with EditFacilityDialogController.
 */
public class EditFacilityDialogFragment extends DialogFragment {
    private User user;
    private EditFacilityDialogController editFacilityDialogController;
    private String dialogTitle = null;

    /**
     * Creates an EditFacilityDialogFragment.
     */
    public EditFacilityDialogFragment() {}

    /**
     * Creates n EditFacilityDialogFragment with a title.
     * @param title the title of the dialog
     */
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
        // Create controller
        editFacilityDialogController = new EditFacilityDialogController(user, this);

        builder.setView(inflater.inflate(R.layout.dialog_edit_facility_material, null))
                .setTitle(dialogTitle)
                .setPositiveButton("Confirm", (dialogInterface, i) -> {
                    Dialog dialog = requireDialog();
                    final TextInputEditText facilityEditText = dialog.findViewById(R.id.edit_facility_FacilityEditText);
                    editFacilityDialogController.extractFacility(facilityEditText);

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

    public void sendToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
