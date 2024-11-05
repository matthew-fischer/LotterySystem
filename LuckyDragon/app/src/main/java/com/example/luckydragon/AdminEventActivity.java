package com.example.luckydragon;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.Map;

public class AdminEventActivity extends AppBarActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event);
        getSupportActionBar().setTitle("Event");

        // Unpack intent
        Intent intent = getIntent();
        String id = intent.getStringExtra("event");

        // Get event
        db.collection("events")
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Got document");
                        Map<String, Object> eventData;
                        DocumentSnapshot document = task.getResult();
                        eventData = document.getData();
                        event = new Event(
                                document.getId(),
                                eventData.get("name") instanceof String ? (String) eventData.get("name") : null,
                                eventData.get("organizerDeviceId") instanceof String ? (String) eventData.get("organizerDeviceId") : null,
                                eventData.get("facility") instanceof String ? (String) eventData.get("facility") : null,
                                eventData.get("waitListLimit") instanceof Number ? ((Number) eventData.get("waitListLimit")).intValue() : 0,  // default 0
                                eventData.get("attendeeLimit") instanceof Number ? ((Number) eventData.get("attendeeLimit")).intValue() : 0, // default 0
                                eventData.get("date") instanceof String ? (String) eventData.get("date") : null,
                                eventData.get("hours") instanceof Number ? ((Number) eventData.get("hours")).intValue() : 0, // default 0
                                eventData.get("minutes") instanceof Number ? ((Number) eventData.get("minutes")).intValue() : 0 // default 0
                        );
                        TextView eventName = findViewById(R.id.eventNameAdminPage);
                        TextView facilityName = findViewById(R.id.facilityNameAdminPage);
                        TextView dateAndTime = findViewById(R.id.dateAndTimeAdminPage);
                        TextView currentlyJoined = findViewById(R.id.currentAttendeeListSizeAdminPage);
                        TextView waitlistSpots = findViewById(R.id.waitlistSizeAdminPage);
                        TextView attendeeSpots = findViewById(R.id.attendeeListSizeAdminPage);

                        eventName.setText(event.getName());
                        facilityName.setText(event.getFacility());
                        dateAndTime.setText(event.getDateAndTime());
                        String currentlyJoinedText = "Currently Joined: " + String.valueOf(event.getAttendeeListSize());
                        currentlyJoined.setText(currentlyJoinedText);
                        String waitlistSpotsText = "Wailist Spots: " + String.valueOf(event.getWaitListSpots());
                        waitlistSpots.setText(waitlistSpotsText);
                        String attendeeSpotsText = "Attendee Spots: " + String.valueOf(event.getAttendeeSpots());
                        attendeeSpots.setText(attendeeSpotsText);
                    }
                });

        Button deleteEvent = findViewById(R.id.deleteEventAdminPage);

        deleteEvent.setOnClickListener((View v) -> {

            db.collection("events")
                    .document(event.getId())
                    .delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminEventActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

        });

        Button removeQr = findViewById(R.id.removeQRAdminPage);

        removeQr.setOnClickListener((View v) -> {
            db.collection("events")
                    .document(event.getId())
                    .update("hashedQR", "null")
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminEventActivity.this, "QR code removed successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        });

    }

}
