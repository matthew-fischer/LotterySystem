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
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppBarActivity {
    // Mode may be ENTRANT or ORGANIZER. If there is an admin profile, then ADMIN should be added as well.
    enum Mode {
        ENTRANT,
        ORGANIZER,
    }

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Profile");

        // Unpack intent
        Intent intent = getIntent();
        user = ((GlobalApp) getApplication()).getUser();
        String role = intent.getStringExtra("role");

        // Set profile info views
        TextView nameView = findViewById(R.id.nameTextView);
        TextView emailView = findViewById(R.id.emailTextView);
        TextView phoneNumberView = findViewById(R.id.phoneNumberTextView);
        ImageView profilePictureView = findViewById(R.id.profilePicture);

        nameView.setText(user.getName());
        emailView.setText(user.getEmail());
        phoneNumberView.setText(user.getPhoneNumber());
        profilePictureView.setImageBitmap(user.getProfilePicture());
        // Create profile fragment
        if (Objects.equals(role, "ORGANIZER")) {
            // Create Organizer
//            String facility = intent.getStringExtra("facilityName");
//            user = new Organizer(deviceID, name, email, phoneNumber, facility);

            // Create organizer profile fragment
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, OrganizerProfileFragment.class, null)
                    .commit();
        } else if (Objects.equals(role, "ENTRANT")) {
            // Create entrant profile fragment
            getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragment_container_view, EntrantProfileFragment.class, null)
                            .commit();
        } else {
            throw new RuntimeException("User mode not set.");
        }
    }

    public User getUser() {
        return user;
    }
}
