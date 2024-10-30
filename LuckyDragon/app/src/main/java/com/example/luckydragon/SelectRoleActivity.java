package com.example.luckydragon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    User user;
    Organizer organizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SelectRoleActivity activity = this;

        // Get user data
        String deviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        user = new User(deviceID);
        DocumentReference docRef = db.collection("users").document(deviceID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot userDocument = task.getResult();
                    if(userDocument.exists()) {
                        setContentView(R.layout.select_role_page); // set content to role page
                        Map<String, Object> userData = userDocument.getData();
                        user.setData(userData);
                        if (user.isOrganizer()) {
                            organizer = new Organizer(user, String.format("%s", userData.get("Facility")));
                        }
                    } else {
//                        // Create a new document for this user
//                        docRef
//                                .set(user.getUserData())
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        throw e;
//                                    }
//                                })

                    }

                    // Set up on entrant click listener
                    Button entrantButton = findViewById(R.id.entrantButton);
                    entrantButton.setOnClickListener(v -> {
                        if (user.isEntrant()) {
                            // TODO: Put additional entrant profile information in the intent
                            // Create profile intent
                            Intent profileIntent = new Intent(activity, ProfileActivity.class);
                            profileIntent.putExtra("user", user);
                            profileIntent.putExtra("role", "ENTRANT");
                            // Start profile activity
                            startActivity(profileIntent);
                        } else {
                            // Send to entrant signup
                            Intent profileIntent = new Intent(activity, SignupActivity.class);
                            profileIntent.putExtra("deviceID", deviceID);
                        }
                    });

                    // Set up on organizer click listener
                    Button organizerButton = findViewById(R.id.organizerButton);
                    organizerButton.setOnClickListener(v -> {
                        if (user.isOrganizer()) {
                            // Create profile intent
                            Intent profileIntent = new Intent(activity, ProfileActivity.class);
                            profileIntent.putExtra("organizer", organizer);

                            // Put additional organizer profile information in the intent
                            profileIntent.putExtra("role", "ORGANIZER");
                            // Start profile activity
                            startActivity(profileIntent);
                        } else {
                            // Send to organizer signup
                        }
                    });

                    // Set up on admin click listener
                    // Hide admin button if user is not an admin
                    if(!user.isAdmin()) {
                        Button adminButton = findViewById(R.id.adminButton);
                        adminButton.setVisibility(View.GONE);
                    };
                } else {
                    throw new RuntimeException("Database read failed.");
                }

            }
        });


    }
}
