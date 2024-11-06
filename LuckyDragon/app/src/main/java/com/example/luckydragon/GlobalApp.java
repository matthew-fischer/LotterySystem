package com.example.luckydragon;

import static java.util.Objects.nonNull;

import android.app.Application;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalApp extends Application {
    public enum ROLE {
        ENTRANT,
        ORGANIZER,
        ADMINISTRATOR
    }
    private User user;
    private ROLE role;
    private Map<String, Event> events;
    private UserList users;
    private EventList eventList;
    private String deviceId = null;

    final Bitmap profilePictureSize = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
    public User getUser() {
        if (user == null) {
            if(deviceId == null) deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.e("DEVICE ID", deviceId);
            user = new User(deviceId);
            user.fetchData();
        }
        return user;
    }

    public ROLE getRole() {
        return role;
    }

    public UserList getUsers() {

        users = new UserList();
        users.fetchData();
        return users;
    }

    public void setUser(User newUser) {
        user = newUser;
    }

    public void setRole(ROLE role) {
        this.role = role;
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

    public EventList getEvents() {
        eventList = new EventList();
        eventList.fetchData();
        return eventList;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
