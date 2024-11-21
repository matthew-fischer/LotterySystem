package com.example.luckydragon;

import android.app.Application;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.util.Log;

import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.EventList;
import com.example.luckydragon.Models.User;
import com.example.luckydragon.Models.UserList;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

/**
 * The GlobalApp class stores global information of the app including the
 * current user, current event, and global utility functions like obtaining
 * the current user, current event, or setting the global database instance
 * to be used. It also allows getting a list of all users and all events.
 */
public class GlobalApp extends Application {
    public enum ROLE {
        ENTRANT,
        ORGANIZER,
        ADMINISTRATOR
    }
    private User user;
    private ROLE role;
    private Event event;
    private FirebaseFirestore db;

    private UserList users;
    private EventList eventList;
    private String deviceId = null;

    public final Bitmap profilePictureSize = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

    private Event eventToView = null; // temporary storage for eventToView to be passed into ViewEventActivity (avoids passing event through intent or having to re-fetch it unneccessarily)


    /**
     * Gets the current user of the app, fetching the DB if needed.
     * if it has not been locally set yet.
     * @return the current user of the app
     */
    public User getUser() {
        if (db == null) {
            setDb(FirebaseFirestore.getInstance());
        }
        if (user == null) {
            if(deviceId == null) deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            user = new User(deviceId, db);
            user.fetchData();
        }
        return user;
    }

    public ROLE getRole() {
        return role;
    }

    /**
     * Returns a list of all users who have registered with the app
     * according to the db.
     * @return the list of all users registered
     */
    public UserList getUsers() {
        if (db == null) {
            setDb(FirebaseFirestore.getInstance());
        }

        users = new UserList(db);
        users.fetchData();
        return users;
    }

    public void setRole(ROLE role) {
        this.role = role;
    }

    /**
     * Gets the event with eventId, and fetches
     * the db if it has not been set yet.
     * @param eventId the eventId of the event
     * @return the event object
     */
    public Event getEvent(String eventId) {
        Log.e("FETCH", "get event");
        if (db == null) {
            setDb(FirebaseFirestore.getInstance());
        }
        if (event == null || !Objects.equals(event.getId(), eventId)) {
            event = new Event(eventId, db);
            event.fetchData();
        }
        return event;
    }

    /**
     * Creates an Event in the db. If the event already existed,
     * returns the event stored in the db.
     * @return the event after it has been created
     */
    public Event makeEvent() {
        // create an eventId
        if (db == null) {
            setDb(FirebaseFirestore.getInstance());
        }
        Event event = new Event(db);

        return getEvent(event.getId());
    }

    /**
     * Sets the current db. Used in tests for mocking.
     * @param db the db to be set
     */
    public void setDb(FirebaseFirestore db) {
        this.db = db;
    }

    /**
     * Sets the current User.
     * @param newUser the user to be set
     */
    public void setUser(User newUser) {
        user = newUser;
    }

    /**
     * Gets a list of all events in the db.
     * @return an EventList of all events
     */
    public EventList getEvents() {
        if (db == null) {
            setDb(FirebaseFirestore.getInstance());
        }
        eventList = new EventList(db);
        eventList.fetchData();
        return eventList;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void resetState() {
        db = null;
        user = null;
        role = null;
        event = null;
        users = null;
        eventList = null;
        deviceId = null;
    }

    public void setEventToView(Event eventToView) {
        this.eventToView = eventToView;
    }

    public Event getEventToView() {
        return eventToView;
    }
}
