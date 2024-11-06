package com.example.luckydragon;

import android.app.Application;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GlobalApp extends Application {
    private User user;
    private Map<String, Event> events;
    private FirebaseFirestore db;

    final Bitmap profilePictureSize = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
    public User getUser() {
        Log.d("GLOBALAPP", String.valueOf(db == null));
        if (db == null) {
            setDb(FirebaseFirestore.getInstance());
        }
        if (user == null) {
            String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            user = new User(deviceId, db);
            user.fetchData();
        }
        return user;
    }

    public Event getEvent(String eventId) {
        if (db == null) {
            setDb(FirebaseFirestore.getInstance());
        }
        if (events == null) {
            events = new HashMap<>();
        }
        Event event = events.get(eventId);
        if (event == null || event.getId() != eventId) {
            event = new Event(eventId, db);
            events.put(eventId, event);
            event.fetchData();
        }
        return event;
    }

    public Event makeEvent() {
        // create an eventId
        Event event = new Event(db);

        return getEvent(event.getId());
    }

    // functions for tests
    public void setDb(FirebaseFirestore db) {
        this.db = db;
    }
    public void setUser(User newUser) {
        user = newUser;
    }
}
