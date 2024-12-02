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
        for (NotificationList.Notification notification : getObservable().getNotificationList()) {
            globalApp.sendNotification(notification.title, notification.body);
        }
        if (!getObservable().getNotificationList().isEmpty()) {
            // only need to clear notifications if there are any
            getObservable().clearNotifications();
        }
    }
}
