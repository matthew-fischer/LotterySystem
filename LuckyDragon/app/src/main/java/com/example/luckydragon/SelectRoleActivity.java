/**
 * Defines the SelectRoleActivity class.
 * Associated with SelectRoleView.
 */

package com.example.luckydragon;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;
import java.util.Objects;

/**
 * NOTES
 * If user is an admin, "Entrant"/"Organizer"/"Administrator" buttons should show.
 * If user is not an admin, only "Entrant"/"Organizer" buttons should show.
 */
public class SelectRoleActivity extends AppCompatActivity {
    private User user;
    private SelectRoleView selectRoleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.select_role_page);
        super.onCreate(savedInstanceState);

        // Get user
        user = ((GlobalApp) getApplication()).getUser();
        // Create view
        selectRoleView = new SelectRoleView(user, this);
    }

    /**
     * Set button visibilities and on click listeners.
     * This runs once the user data has been fetched, so that admin button can be hidden if user is not an admin.
     */
    public void initializeView() {
        // Set up on entrant click listener
        Button entrantButton = findViewById(R.id.entrantButton);
        entrantButton.setVisibility(View.VISIBLE);
        if (!entrantButton.hasOnClickListeners()) {
            entrantButton.setOnClickListener(v -> {
                // Set GlobalApp role to entrant
                ((GlobalApp) getApplication()).setRole(GlobalApp.ROLE.ENTRANT);
                if (user.isEntrant()) {
                    // Create profile intent
                    Intent profileIntent = new Intent(this, ProfileActivity.class);
                    // Start profile activity
                    startActivity(profileIntent);
                } else {
                    // Send to entrant signup
                    Intent signupIntent = new Intent(this, SignupActivity.class);
                    startActivity(signupIntent);
                }
            });
        }

        // Set up on organizer click listener
        Button organizerButton = findViewById(R.id.organizerButton);
        organizerButton.setVisibility(View.VISIBLE);
        if (!organizerButton.hasOnClickListeners()) {
            organizerButton.setOnClickListener(v -> {
                // Set GlobalApp role to organizer
                ((GlobalApp) getApplication()).setRole(GlobalApp.ROLE.ORGANIZER);
                if (user.isOrganizer()) {
                    // Create profile intent
                    Intent profileIntent = new Intent(this, ProfileActivity.class);
                    // Start profile activity
                    startActivity(profileIntent);
                } else {
                // Send to signup
                Intent signupIntent = new Intent(this, SignupActivity.class);
                startActivity(signupIntent);
            }
            });
        }

        // Admin button
        if (user.isAdmin()) {
            Button adminButton = findViewById(R.id.adminButton);
            if (!adminButton.hasOnClickListeners()) {
                adminButton.setOnClickListener(v -> {
                    // Create profile intent
                    ((GlobalApp) getApplication()).setRole(GlobalApp.ROLE.ADMINISTRATOR);
                    Intent profileIntent = new Intent(this, ProfileActivity.class);
                    // Start profile activity
                    startActivity(profileIntent);
                });
            }
        }
    }

    /**
     * Sets admin button visibility to VISIBLE..
     */
    public void showAdminButton() {
        Button adminButton = findViewById(R.id.adminButton);
        adminButton.setVisibility(View.VISIBLE);
    }
}
