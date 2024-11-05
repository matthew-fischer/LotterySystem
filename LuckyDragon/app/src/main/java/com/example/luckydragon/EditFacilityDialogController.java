package com.example.luckydragon;

import android.util.Log;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class EditFacilityDialogController extends Controller {
    EditFacilityDialogFragment editFacilityDialogFragment;

    public EditFacilityDialogController(User user, EditFacilityDialogFragment editFacilityDialogFragment) {
        super(user);
        this.editFacilityDialogFragment = editFacilityDialogFragment;
    }

    @Override
    public User getObservable () {
        return (User) super.getObservable();
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
}
