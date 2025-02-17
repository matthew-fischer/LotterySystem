package com.example.luckydragon.Activities;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import com.example.luckydragon.Controllers.SignupController;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.User;
import com.example.luckydragon.R;
import com.example.luckydragon.Views.SignupView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

/**
 * The activity used for signing up or editing user profile information.
 * Uses a View and Controller to manage the User model.
 */
public class SignupActivity extends AppBarActivity {
    private User user;
    private SignupController signupController;
    private SignupView signupView;

    private GlobalApp.ROLE role;

    private TextInputEditText editName;
    private TextInputEditText editEmail;
    private TextInputEditText editPhone;
    private SwitchMaterial switchNotifications;
    private Button submitButton;
    private ActivityResultLauncher<Intent> uploadImageResultLauncher;
    private ImageButton profilePictureButton;

    // from: https://firebase.google.com/docs/cloud-messaging/android/client
    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    onSubmit();
                    // FCM SDK (and your app) can post notifications.
                }
            });

    /**
     * Open a dialog to ask for permission to send notifications to user
     */
    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                throw new RuntimeException("entered into notification perm request with requests enabled already!");
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(POST_NOTIFICATIONS);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_material);
        getSupportActionBar().setTitle("Sign-Up");

        // Get role
        role = ((GlobalApp) getApplication()).getRole();

        // Get input fields
        editName = findViewById(R.id.signupName);
        editEmail = findViewById(R.id.signupEmail);
        editPhone = findViewById(R.id.signupPhone);
        switchNotifications = findViewById(R.id.signupNotifications);
        submitButton = findViewById(R.id.signupSubmit);
        profilePictureButton = findViewById(R.id.profilePictureIcon);

        user = ((GlobalApp) getApplication()).getUser();
        signupController = new SignupController(user);
        signupView = new SignupView(user, this, signupController);

        // Set page titles
        String titleString = getIntent().getStringExtra("title");
        String subtitleString = getIntent().getStringExtra("subtitle");
        String navbarString = getIntent().getStringExtra("navbar");
        if (titleString != null) {
            TextView titleText = findViewById(R.id.signupTitle);
            titleText.setText(titleString);
        }
        if (subtitleString != null) {
            TextView subtitleText = findViewById(R.id.signupSubtitle);
            subtitleText.setText(subtitleString);
        }
        if (navbarString != null) {
            getSupportActionBar().setTitle(navbarString);
        }

        uploadImageResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data == null || data.getData() == null) return;
                            Uri image = data.getData();
                            try {
                                Bitmap profilePicture = MediaStore.Images.Media.getBitmap(getContentResolver(), image);

                                // Crop image to square
                                int n = Math.min(profilePicture.getWidth(), profilePicture.getHeight());
                                profilePicture = Bitmap.createBitmap(profilePicture, 0, 0, n, n);

                                // Scale image to proper size
                                int width = ((GlobalApp) getApplication()).profilePictureSize.getWidth();
                                int height = ((GlobalApp) getApplication()).profilePictureSize.getHeight();
                                profilePicture = Bitmap.createScaledBitmap(profilePicture, width, height, false);
                                signupController.setProfilePicture(profilePicture);
                            } catch (Exception ignored) {
                            }
                        }
                    }
                });
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDefaults();
    }


    public void setSubmitButton(boolean enabled) {
        submitButton.setEnabled(enabled);
    }

    private void setDefaults() {
        editName.setText(user.isValid() ? user.getName() : "");
        editEmail.setText(user.isValid() ? user.getEmail() : "");
        editPhone.setText(user.isValid() ? user.getPhoneNumber() : "");
        switchNotifications.setChecked(user.isNotified());
    }

    private void setupListeners() {
        setListener(editName, () -> {
            // code that will run in x seconds
            try {
                signupController.extractName(editName);
            } catch(Exception ignored) {};
        });
        setListener(editEmail, () -> {
            // code that will run in x seconds
            try {
                signupController.extractEmail(editEmail);
            } catch (Exception ignored) {};
        });
        setListener(editPhone, () -> {
            // code that will run in x seconds
            try {
                signupController.extractPhoneNumber(editPhone);
            } catch(Exception ignored) {};
        });
        switchNotifications.setOnCheckedChangeListener((compoundButton, b) -> {
            signupController.setNotifications(switchNotifications);
        });

        // Set profile picture click to open popup menu with option to remove/upload picture
        profilePictureButton.setOnClickListener(view -> {
            PopupMenu profilePicturePopup = new PopupMenu(this, profilePictureButton);
            profilePicturePopup.getMenuInflater().inflate(R.menu.profile_picture_popup_menu,
                    profilePicturePopup.getMenu());
            profilePicturePopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    int id = menuItem.getItemId();
                    if (id == R.id.uploadPicture) {
                        // Launch intent to choose picture from phone
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        uploadImageResultLauncher.launch(intent);
                    } else {
                        // Remove picture
                        signupController.setProfilePicture(null);
                    }
                    return true;
                }
            });
            profilePicturePopup.show();
        });

        submitButton.setOnClickListener(view -> {
            onSubmit();
        });
    }

    private void onSubmit() {
        // Validate input fields
        boolean valid = true;
        try {
            signupController.extractName(editName);
        } catch (Exception e) {
            editName.setError(e.getMessage());
            valid = false;
        }
        try {
            signupController.extractEmail(editEmail);
        } catch(Exception e) {
            editEmail.setError(e.getMessage());
            valid = false;
        }
        try {
            signupController.extractPhoneNumber(editPhone);
        } catch (Exception e) {
            editEmail.setError(e.getMessage());
            valid = false;
        }
        if (!valid) return;

        // check for notifications enabled and perms not enabled
        if (switchNotifications.isChecked() && ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED) {
            askNotificationPermission();  // make sure notifs are enabled
            return;
        }

        // tell activity to launch profile
        Intent intent = new Intent(this, ProfileActivity.class);
        // Start profile activity
        startActivity(intent);
        finish();

        // become entrant and organizer
        signupController.becomeEntrantAndOrganizer();
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

    /**
     * Sets the profilePictureButton to display the profilePicture.
     * @param profilePicture the given profilePicture to be displayed
     */
    public void updateProfilePictureIcon(Bitmap profilePicture) {
        profilePictureButton.setImageBitmap(profilePicture);

        // Picture was removed, so set to edit profile picture icon
        if (profilePicture == null) {
            profilePictureButton.setImageResource(R.drawable.profile_edit);
        }
    }
    public GlobalApp.ROLE getRole() {
        return role;
    }
}
