package com.example.luckydragon;

import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;

public class ViewProfilesActivity extends AppBarActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_profiles);
        getSupportActionBar().setTitle("Profiles");

    }
}
