package com.example.luckydragon;

import android.app.Application;
import android.provider.Settings;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class GlobalApp extends Application {
    public enum ROLE {
        ENTRANT,
        ORGANIZER,
        ADMINISTRATOR
    }
    private User user;
    private ROLE role;

    public User getUser() {
        if (user == null) {
            String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            user = new User(deviceId);
            user.fetchData();
        }
        return user;
    }

    public ROLE getRole() {
        return role;
    }

    public void setUser(User newUser) {
        user = newUser;
    }

    public void setRole(ROLE role) {
        this.role = role;
    }
}
