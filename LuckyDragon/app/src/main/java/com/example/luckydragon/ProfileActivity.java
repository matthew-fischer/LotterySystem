/**
 * Defines ProfileActivity which is the profile page.
 * Both users and organizers have profiles.
 * The shared logic is implemented in this activity.
 * The User-specific or organizer-specific part of the page goes in its own fragment.
 * If the user is in entrant mode, UserProfileFragment is embedded. (TODO: Implement UserProfileFragment)
 * If the user is in organizer mode, OrganizerProfileFragment is embedded.
 * Not sure if we will have an Admin profile. If so, admin details should go in AdminProfileFragment.
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

public class ProfileActivity extends AppBarActivity {
    // Mode may be ENTRANT or ORGANIZER. If there is an admin profile, then ADMIN should be added as well.
    enum Mode {
        ENTRANT,
        ORGANIZER,
        ADMIN,
    }
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
        profileView = new ProfileView(user, this);

        // If user exists, update view
        if(user != null) {
            user.notifyObservers();
        }
    }

    public void initializeView() {
        // Set profile info views
        TextView nameView = findViewById(R.id.nameTextView);
        TextView emailView = findViewById(R.id.emailTextView);
        TextView phoneNumberView = findViewById(R.id.phoneNumberTextView);

        // Set name
        nameView.setText(user.getName());
        // Set email if it exists, otherwise hide the textview
        if(user.getEmail() != null) {
            emailView.setText(user.getEmail());
        } else {
            emailView.setVisibility(View.GONE);
        }
        // Set phone number if it exists, otherwise hide the textview
        if(user.getPhoneNumber() != null) {
            phoneNumberView.setText(user.getPhoneNumber());
        } else {
            Log.e("USER", "no phone number");
            phoneNumberView.setVisibility(View.GONE);
        }

        ImageView profilePictureView = findViewById(R.id.profilePicture);

        nameView.setText(user.getName());
        emailView.setText(user.getEmail());
        phoneNumberView.setText(user.getPhoneNumber());
        profilePictureView.setImageBitmap(user.getProfilePicture());

        ImageButton edit_profile_button = findViewById(R.id.edit_profile_button);
        edit_profile_button.setOnClickListener(view -> {
            // Create intent to go to signup
            Intent signupIntent = new Intent(this, SignupActivity.class);
            signupIntent.putExtra("role", role);
            startActivity(signupIntent);
        });
        // Create profile fragment
        if (role == GlobalApp.ROLE.ENTRANT) {
            // Create entrant profile fragment
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view, EntrantProfileFragment.class, null)
                    .commit();
        } else if (role == GlobalApp.ROLE.ORGANIZER) {
            // Create organizer profile fragment
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, OrganizerProfileFragment.class, null)
                    .commit();
        } else {
            throw new RuntimeException("User mode not set.");
        }
    }


    public void sendToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
