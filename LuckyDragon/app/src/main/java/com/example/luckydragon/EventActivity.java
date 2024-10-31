package com.example.luckydragon;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class EventActivity extends AppBarActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String eventName, facilityName, dateAndTime, attendeeSpots, waitlistSpots, currentlyJoined;
    private List<String> WaitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        getSupportActionBar().setTitle("Event");

        String eventID = getIntent().getStringExtra("eventID");
        String deviceID = getIntent().getStringExtra("deviceID");
        if (eventID != null) {
            DocumentReference docRef = db.collection("events").document(eventID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot eventDocument = task.getResult();
                        if (eventDocument.exists()) {
                            Map<String, Object> eventData = eventDocument.getData();
                            if (eventData != null) {
                                eventName = (String) eventData.get("Name");
                                facilityName = (String) eventData.get("Facility");
                                dateAndTime = String.format("%s -- %s:%s", eventData.get("Date"), String.valueOf(eventData.get("Hours")), String.valueOf(eventData.get("Minutes")));
                                attendeeSpots = String.valueOf(eventData.get("AttendeeLimit"));
                                if (eventData.get("WaitlistLimit") == null) {
                                    waitlistSpots = "No Limit";
                                } else {
                                    waitlistSpots = String.valueOf(eventData.get("WaitlistLimit"));
                                }
                                WaitList = (List<String>) eventData.get("Waitlist");
                                currentlyJoined = String.valueOf(WaitList.size());

                            } else {
                                throw new RuntimeException("Event has no data.");
                            }
                        } else {
                            throw new RuntimeException("Event Document does not exist.");
                        }
                    } else {
                        throw new RuntimeException("Database read failed.");
                    }
                    // Set Event Page Information.
                    TextView eventNameView = findViewById(R.id.eventName);
                    eventNameView.setText(eventName);
                    TextView facilityNameView = findViewById(R.id.facilityName);
                    facilityNameView.setText(facilityName);
                    TextView dateAndTimeView = findViewById(R.id.dateAndTime);
                    dateAndTimeView.setText(dateAndTime);
                    TextView waitlistSpotsView = findViewById(R.id.waitlistSpots);
                    waitlistSpotsView.setText(String.format("Waitlist Spots: %s", waitlistSpots));
                    TextView attendeeSpotsView = findViewById(R.id.attendeeSpots);
                    attendeeSpotsView.setText(String.format("Attendee Spots: %s", attendeeSpots));
                    TextView currentlyJoinedView = findViewById(R.id.currentlyJoined);
                    currentlyJoinedView.setText(String.format("Currently Joined: %s", currentlyJoined));
                }
            });

            // Signup button will bring user to profile page. And put their deviceID on the waitlist.
            Button signUp = findViewById(R.id.signUpButton);
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Add deviceID to Waitlist:
                    if (!WaitList.contains(deviceID)) {
                        WaitList.add(deviceID);
                    }

                    // update Firestore:
                    docRef.update("Waitlist", WaitList)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("Firestore", "Waitlist updated.");
                                }
                            });

                    // Go back to ProfileActivity. Event will show until "Waitlisted".
                    finish();
                }
            });

            // ViewPoster button will show a dialog with an ImageView.
            Button viewPoster = findViewById(R.id.viewPosterButton);
            viewPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // ADD FUNCTIONALITY HERE.
                }
            });
        } else {
            throw new RuntimeException("EventID is NULL.");
        }
    }
}
