/*
 * Defines ProfileActivity which is the profile page.
 * Both users and organizers have profiles.
 * The shared logic is implemented in this activity.
 * The user-specific or organizer-specific or admin-specific part of the page goes in its own fragment.
 * If the user is in entrant mode, UserProfileFragment is embedded.
 * If the user is in organizer mode, OrganizerProfileFragment is embedded.
 * If the user is in admin mode, AdminProfileFragment is embedded.
 * Associated with ProfileView.
 * Issues:
 *   NONE
 */

package com.example.luckydragon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

/**
 * This is the activity for the profile page.
 * It shows general user information like name, email, and phone number.
 * It supports editing of this information.
 * Role-specific information is embedded in this activity as a fragment (i.e. if user is in organizer mode, OrganizerProfileFragment is embedded.
 * Updated by ProfileView.
 */
public class ProfileActivity extends AppBarActivity {
    private User user;
    private GlobalApp.ROLE role;
    private ProfileView profileView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Profile");

        // Get user from global app
        user = ((GlobalApp) getApplication()).getUser();
        role = ((GlobalApp) getApplication()).getRole();

        // Create profile view
        // initializeView() uses profileView, but it will run before the ProfileView constructor returns so it still thinks ProfileView is null
        // To fix I set isLoaded to false until after profileView is set
        // I also had to add notifyObservers() to setIsLoaded() in User
        profileView = new ProfileView(user, this);

        // Create profile fragment
        if (role == GlobalApp.ROLE.ENTRANT) {
            // Create entrant profile fragment
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view, EntrantProfileFragment.class, null)
                    .commit();
        } else if (role == GlobalApp.ROLE.ORGANIZER) {
            // Check if organizer profile fragment exsits
            // Create organizer profile fragment
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view, OrganizerProfileFragment.class, null)
                    .commit();
        } else if (role == GlobalApp.ROLE.ADMINISTRATOR) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view, AdminProfileFragment.class, null)
                    .commit();
        } else {
            throw new RuntimeException("User role not set!");
        }

        // Initialize edit profile button on click
        ImageButton edit_profile_button = findViewById(R.id.edit_profile_button);
        edit_profile_button.setOnClickListener(view -> {
            Intent goToSignup = new Intent(this, SignupActivity.class);
            startActivity(goToSignup);
        });
    }

    /**
     * Displays a short toast method with a given message.
     * @param message the message to show
     */
    public void sendToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
