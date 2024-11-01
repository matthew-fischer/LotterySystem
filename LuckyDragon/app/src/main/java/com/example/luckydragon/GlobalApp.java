package com.example.luckydragon;

import android.app.Application;
import android.provider.Settings;

public class GlobalApp extends Application {
    private User user;

    public User getUser() {
        if (user == null) {
            String deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            user = new User(deviceId);
        }
        return user;
    }
}
