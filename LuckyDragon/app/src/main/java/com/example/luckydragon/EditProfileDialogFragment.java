/**
 * Defines EditProfileDialogFragment which allows an organizer to edit their facility.
 */

package com.example.luckydragon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditProfileDialogFragment extends DialogFragment {
    private TextInputLayout nameLayout;
    private TextInputEditText editName;
    private TextInputLayout emailLayout;
    private TextInputEditText editEmail;
    private TextInputLayout phoneLayout;
    private TextInputEditText editPhone;

    private EditProfileView editProfileView;
    private EditProfileController editProfileController;

    public EditProfileDialogFragment() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        ProfileActivity activity = (ProfileActivity) requireActivity();
        User user = ((GlobalApp) activity.getApplication()).getUser();
        editProfileController = new EditProfileController(user);
        editProfileView = new EditProfileView(user, this, editProfileController);

        builder.setView(inflater.inflate(R.layout.dialog_edit_profile, null))
                .setPositiveButton("Close", (dialogInterface, i) -> {
                });

        // TODO: disable positive button if invalid data/do something about invalid and closing dialog

        Dialog dialog = builder.create();
        dialog.show();

        nameLayout = dialog.findViewById(R.id.editProfileNameLayout);
        editName = dialog.findViewById(R.id.editProfileEditName);
        emailLayout = dialog.findViewById(R.id.editProfileEmailLayout);
        editEmail = dialog.findViewById(R.id.editProfileEditEmail);
        phoneLayout = dialog.findViewById(R.id.editProfilePhoneLayout);
        editPhone = dialog.findViewById(R.id.editProfileEditPhone);

        // TODO: ASK should be in view? Set text to existing value
        editName.setText(user.getName());
        editEmail.setText(user.getEmail());
        editPhone.setText(user.getPhoneNumber());

        setupListeners();

        return dialog;
    }

    private void setupListeners() {
        setListener(editName, () -> {
            // code that will run in x seconds
            editProfileController.extractName(editName);
            // TODO: input validation
        });
        setListener(editEmail, () -> {
            // code that will run in x seconds
            editProfileController.extractEmail(editEmail);
            // TODO: input validation
        });
        setListener(editPhone, () -> {
            // code that will run in x seconds
            editProfileController.extractPhoneNumber(editPhone);
            // TODO: input validation
        });
        // Set clear button click listener
        nameLayout.setEndIconOnClickListener((v) -> {
            editName.setText("");
        });
        emailLayout.setEndIconOnClickListener((v) -> {
            editEmail.setText("");
        });
        phoneLayout.setEndIconOnClickListener((v) -> {
            editPhone.setText("");
        });

//        // set listener for uploading pfp button
//        uploadProfilePictureButton.setOnClickListener(view -> {
//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            uploadImageResultLauncher.launch(intent);
//        });
//
//        submitButton.setOnClickListener(view -> {
//            // tell activity to launch profile
//            Intent intent = new Intent(this, ProfileActivity.class);
//            intent.putExtra("role", "ENTRANT");
//
//            // Start profile activity
//            startActivity(intent);
//            finish();
//
//            // become entrant
//            signupController.becomeEntrant();
//        });
    }

    private void setListener(EditText editText, Runnable runnable) {
        Handler handler = new Handler();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // clear previous requested runnable
                handler.removeCallbacks(runnable);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                handler.postDelayed(runnable, 1000);
            }
        });
    }
}
