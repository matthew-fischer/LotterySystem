/*
 * Controller for EditFacilityDialogFragment.
 * Outstanding Issues:
 *   NONE
 */

package com.example.luckydragon;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

/**
 * Defines EditFacilityDialogController.
 * Interacts with EditFacilityDialogFragment.
 */
public class EditFacilityDialogController extends Controller {
    EditFacilityDialogFragment editFacilityDialogFragment;

    /**
     * Creates an EditFacilityDialogController instance.
     * @param user the user of the application
     * @param editFacilityDialogFragment the editFacilityDialogFragment being controlled
     */
    public EditFacilityDialogController(User user, EditFacilityDialogFragment editFacilityDialogFragment) {
        super(user);
        this.editFacilityDialogFragment = editFacilityDialogFragment;
    }

    /**
     * Extracts facility from the given text field, validates input, and updates User.
     * @param editText the text field where facility is entered
     */
    public void extractFacility(TextInputEditText editText) {
        String facilityName = Objects.requireNonNull(editText.getText()).toString();

        if(facilityName.isEmpty()) {
            editFacilityDialogFragment.sendToast("Facility name cannot be empty!");
            return;
        }

        assert getObservable().getOrganizer() != null;
        getObservable().getOrganizer().setFacility(facilityName);
    }

    @Override
    public User getObservable () {
        return (User) super.getObservable();
    }
}
