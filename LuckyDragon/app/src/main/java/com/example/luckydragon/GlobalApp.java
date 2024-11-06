package com.example.luckydragon;

import android.app.Application;
import android.graphics.Bitmap;
import android.provider.Settings;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalApp extends Application {
    private User user;
    private Map<String, Event> events;
    private UserList users;

    final Bitmap profilePictureSize = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
    public User getUser() {
        if (user == null) {
            String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            user = new User(deviceId);
            user.fetchData();
        }
        return user;
    }

    /*public User getUser(String userId) {
        if (events == null) {
            events = new HashMap<>();
        }
        Event event = events.get(eventId);
        if (event == null || event.getId() != eventId) {
            event = new Event(eventId);
            events.put(eventId, event);
            event.fetchData();
        }
        return user;
    }*/

    public UserList getUsers() {

        users = new UserList();
        users.fetchData();
        return users;
    }

    public void setUser(User newUser) {
        user = newUser;
    }

    public Event getEvent(String eventId) {
        if (events == null) {
            events = new HashMap<>();
        }
        Event event = events.get(eventId);
        if (event == null || event.getId() != eventId) {
            event = new Event(eventId);
            events.put(eventId, event);
            event.fetchData();
        }
        return event;
    }

    public Event makeEvent() {
        // create an eventId
        Event event = new Event();

        return getEvent(event.getId());
    }
}
