package com.example.luckydragon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

public class AdminProfileActivity extends AppBarActivity{

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        getSupportActionBar().setTitle("Profile");

        // Unpack intent
        Intent intent = getIntent();
        user = ((GlobalApp) getApplication()).getUser();

        // Set profile info views
        TextView nameView = findViewById(R.id.nameTextView);
        TextView emailView = findViewById(R.id.emailTextView);
        TextView phoneNumberView = findViewById(R.id.phoneNumberTextView);

        nameView.setText(user.getName());
        emailView.setText(user.getEmail());
        phoneNumberView.setText(user.getPhoneNumber());

        ImageButton edit_profile_button = findViewById(R.id.editProfileButton);

        // Create profile fragment
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, AdminProfileFragment.class, null)
                .commit();
    }
}
