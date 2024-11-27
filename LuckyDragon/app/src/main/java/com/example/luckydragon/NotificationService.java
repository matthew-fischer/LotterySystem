package com.example.luckydragon;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.luckydragon.Activities.SelectRoleActivity;
import com.example.luckydragon.Models.NotificationList;
import com.example.luckydragon.Models.Observable;
import com.example.luckydragon.Views.Observer;

/**
 * A small service that observes the notification list for the device's user
 */
public class NotificationService extends Observer {
    private GlobalApp globalApp;

    /**
     * Start the notification service by observing a specified notification list
     * @param globalApp the global app reference used to send notifications
     * @param observable the notification list
     */
    public NotificationService(GlobalApp globalApp, NotificationList observable) {
        this.globalApp = globalApp;
        startObserving(observable);
    }

    @Override
    public NotificationList getObservable() {
        return (NotificationList) observable;
    }

    @Override
    public void update(Observable whoUpdatedMe) {
        Log.d("TONY", "update: ");
        Log.d("TONY", String.valueOf(getObservable().getNotificationList().size()));
        for (NotificationList.Notification notification : getObservable().getNotificationList()) {
            Log.d("Tony", String.format("Consuming %s", notification.title));
            globalApp.sendNotification(notification.title, notification.body);
        }
        getObservable().clearNotifications();
    }
}
