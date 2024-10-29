package com.example.luckydragon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 * Notes:
 * This activity gets the user profile from the database and passes it to the profile activity.
 * This will allow profile information in ProfileActivity to be displayed immediately, rather than with a delay.
 * ProfileActivity itself shouldn't need database access, but subfragments like EntrantProfileFragment and OrganizerProfileFragment will.
 * All users should be shown Entrant and Organizer roles, but the Admin button will only show for users that were set to Administrator in the database.
 * If a user has no profile in the database they should be taken to SignupActivity to make one.
 * After making one, we could bring them back to this page to select their role.
 */


public class SelectRoleActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SelectRoleActivity activity = this;

        // Get user data
        String deviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        DocumentReference docRef = db.collection("users").document(deviceID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot userDocument = task.getResult();
                    if(userDocument.exists()) {
                        setContentView(R.layout.select_role_page); // set content to role page

                        userData = userDocument.getData();

                        // Create profile intent
                        Intent profileIntent = new Intent(activity, ProfileActivity.class);
                        profileIntent.putExtra("deviceID", deviceID);
                        profileIntent.putExtra("name", String.format("%s %s", userData.get("FirstName"), userData.get("LastName")));
                        profileIntent.putExtra("email", String.format("%s", userData.get("Email")));
                        profileIntent.putExtra("phoneNumber", String.format("%s", userData.get("PhoneNumber")));


                        // Set up on click listeners
                        Button entrantButton = findViewById(R.id.entrantButton);
                        entrantButton.setOnClickListener(v -> {
                            // TODO: Put additional entrant profile information in the intent
                            profileIntent.putExtra("role", "ENTRANT");
                            // Start profile activity
                            startActivity(profileIntent);
                        });

                        Button organizerButton = findViewById(R.id.organizerButton);
                        organizerButton.setOnClickListener(v -> {
                            // Put additional organizer profile information in the intent
                            profileIntent.putExtra("role", "ORGANIZER");
                            profileIntent.putExtra("facilityName", String.format("%s", userData.get("Facility")));
                            // Start profile activity
                            startActivity(profileIntent);
                        });
                    } else {
                        // TODO: start signup activity
                    }
                } else {
                    throw new RuntimeException("Database read failed.");
                }
            }
        });


    }
}
