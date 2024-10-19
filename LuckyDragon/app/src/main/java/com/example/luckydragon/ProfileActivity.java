package com.example.luckydragon;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.Map;

public class ProfileActivity extends AppBarActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Profile");

        // Get profile info views
        TextView nameView = findViewById(R.id.nameTextView);
        TextView emailView = findViewById(R.id.emailTextView);
        TextView phoneNumberView = findViewById(R.id.phoneNumberTextView);

        // Get android device id
        // Reference: https://stackoverflow.com/questions/60503568/best-possible-way-to-get-device-id-in-android
        String id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        // Check if user exists in database
        DocumentReference docRef = db.collection("users").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) throws RuntimeException {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Map<String, Object> docData = document.getData();
                        if(docData != null) {
                            // Initialize profile info
                            nameView.setText(String.format("%s %s", docData.get("FirstName"), docData.get("LastName")));
                            emailView.setText(String.format("%s", docData.get("Email")));
                            phoneNumberView.setText(String.format("%s", docData.get("PhoneNumber")));

                            // Set lower fragment
                            // The fragment will initially be set to the highest permission that a user has
                            // Later I imagine we will have some sort of "mode" that will default to the user's highest permission, but can be changed
                            boolean administrator = (boolean) docData.get("Administrator");
                            boolean organizer = (boolean) docData.get("Organizer");
                            boolean entrant = (boolean) docData.get("Entrant");
                            if(administrator) {
                                // Set administrator profile fragment
                            } else if(organizer) {
                                // Set organizer profile fragment
                                if (savedInstanceState == null) {
                                    getSupportFragmentManager().beginTransaction()
                                            .setReorderingAllowed(true)
                                            .add(R.id.fragment_container_view, OrganizerProfileFragment.class, null)
                                            .commit();
                                }
                            } else if(entrant) {
                                // Set entrant profile fragment
                            } else {
                                throw new RuntimeException("User is not an administrator, organizer, or entrant. They must be at least one.");
                            }
                        }
                    }
                }
            }
        });


    }
}
