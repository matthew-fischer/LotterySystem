package com.example.luckydragon;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Notes:
 * This activity gets the user profile from the database and passes it to the profile activity.
 * This will allow profile information in ProfileActivity to be displayed immediately, rather than with a delay.
 * ProfileActivity itself shouldn't need database access, but sub-fragments like EntrantProfileFragment and OrganizerProfileFragment will.
 * All users should be shown Entrant and Organizer roles, but the Admin button will only show for users that were set to Administrator in the database.
 * If a user has no profile in the database they should be taken to SignupActivity to make one.
 * After making one, we could bring them back to this page to select their role.
 */


public class SelectRoleActivity extends AppCompatActivity {
    User user;
    SelectRoleController selectRoleController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_role_page); // set content to role page

        // Get user data
        String deviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        user = new User(deviceID);
        selectRoleController = new SelectRoleController(user);
        user.addObserver(new SelectRoleView(user, this));

        // Make controller do initial fetching
        selectRoleController.fetchUserData();

        // Set up on entrant click listener
        Button entrantButton = findViewById(R.id.entrantButton);
        entrantButton.setOnClickListener(v -> {
            if (user.isEntrant()) {
                // TODO: Put additional entrant profile information in the intent
                // Create profile intent
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                profileIntent.putExtra("user", user);
                profileIntent.putExtra("role", "ENTRANT");
                // Start profile activity
                startActivity(profileIntent);
            } else {
                // Send to entrant signup
                Intent signupIntent = new Intent(this, SignupActivity.class);
                signupIntent.putExtra("user", user);
                startActivity(signupIntent);
            }
        });

        // Set up on organizer click listener
        Button organizerButton = findViewById(R.id.organizerButton);
        organizerButton.setOnClickListener(v -> {
            if (user.isOrganizer()) {
                // Create profile intent
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                profileIntent.putExtra("user", user);

                // Put additional organizer profile information in the intent
                profileIntent.putExtra("role", "ORGANIZER");
                // Start profile activity
                startActivity(profileIntent);
            } else {
                // TODO: Send to organizer signup
            }
        });
    }

    public void hideAdminButton() {
        Button adminButton = findViewById(R.id.adminButton);
        adminButton.setVisibility(View.GONE);
    }
}
