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

package com.example.luckydragon.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.luckydragon.Fragments.AdminBrowseProfileFragment;
import com.example.luckydragon.Fragments.AdminProfileFragment;
import com.example.luckydragon.Fragments.EntrantProfileFragment;
import com.example.luckydragon.Fragments.OrganizerProfileFragment;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.User;
import com.example.luckydragon.R;
import com.example.luckydragon.Views.ProfileView;

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
        // Admin is viewing a profile
        if (role == GlobalApp.ROLE.ADMINISTRATOR && ((GlobalApp) getApplication()).getUserToView() != null) {
            user = ((GlobalApp) getApplication()).getUserToView();
        }

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
            // Check if organizer profile fragment exists
            // Create organizer profile fragment
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view, OrganizerProfileFragment.class, null)
                    .commit();
        } else if (role == GlobalApp.ROLE.ADMINISTRATOR) {
            if (((GlobalApp) getApplication()).getUserToView() != null) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, AdminBrowseProfileFragment.class, null)
                        .commit();
            }
            else {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, AdminProfileFragment.class, null)
                        .commit();
            }
        } else {
            throw new RuntimeException("User role not set!");
        }

        // Initialize edit profile button only if user is the current user, otherwise hide it.
        // Admin uses this activity for browsing users, but we don't want them to edit other peoples
        // profiles.
        ImageButton edit_profile_button = findViewById(R.id.edit_profile_button);
        if (user.getDeviceId().equals(((GlobalApp) getApplication()).getUser().getDeviceId())) {
            edit_profile_button.setOnClickListener(view -> {
                Intent goToSignup = new Intent(this, SignupActivity.class);
                goToSignup.putExtra("title", getString(R.string.ProfileEditTitle));
                goToSignup.putExtra("subtitle", getString(R.string.ProfileEditSubtitle));
                goToSignup.putExtra("navbar", getString(R.string.ProfileEditNavbar));
                startActivity(goToSignup);
            });
        } else {
            edit_profile_button.setVisibility(View.GONE);
        }
        // If an admin is viewing themselves still hide the edit button
        if (((GlobalApp) getApplication()).getUserToView() != null && Objects.equals(((GlobalApp) getApplication()).getUserToView().getDeviceId(), ((GlobalApp) getApplication()).getUser().getDeviceId())) {
            edit_profile_button.setVisibility(View.GONE);
        }
    }

    /**
     * Displays a short toast method with a given message.
     * @param message the message to show
     */
    public void sendToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
