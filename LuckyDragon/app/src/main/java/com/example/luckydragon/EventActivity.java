package com.example.luckydragon;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Set;

public class EventActivity extends AppBarActivity {
    private Event event;
    private EventView eventView;
    private EventController eventController;


    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        getSupportActionBar().setTitle("Event");

        String deviceId = ((GlobalApp) getApplication()).getUser().getDeviceId();
        String eventId = getIntent().getStringExtra("eventID");

        if (eventId == null) {
            throw new RuntimeException("Event Id is Null!");
        }

        event = ((GlobalApp) getApplication()).getEvent(eventId);  // TODO: Might have to move this onResume in case viewing diff event does not work
        eventController = new EventController(event);

        eventView = new EventView(event, deviceId, this);

        Button signUp = findViewById(R.id.signUpButton);
        Button cancel = findViewById(R.id.eventCancel);
        Button decline = findViewById(R.id.eventDecline);
        Button accept = findViewById(R.id.eventAccept);
        // Signup button will put their deviceID on the waitlist.
        signUp.setOnClickListener(v -> {
            eventController.waitList(deviceId);
        });

        cancel.setOnClickListener(v -> {
            eventController.cancel(deviceId);
        });

        decline.setOnClickListener(v -> {
            eventController.declineInvitation(deviceId);
        });

        accept.setOnClickListener(v -> {
            eventController.acceptInvitation(deviceId);
        });

        // ViewPoster button will show a dialog with an ImageView.
        Button viewPoster = findViewById(R.id.viewPosterButton);
        viewPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ADD FUNCTIONALITY HERE.
            }
        });
    }
}