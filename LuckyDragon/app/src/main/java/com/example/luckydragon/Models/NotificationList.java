package com.example.luckydragon.Models;

import android.util.Log;

import com.example.luckydragon.NotificationService;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * An observable that represents a single user's list of notifications that
 * needs to be sent
 */
public class NotificationList extends Observable {
    private User user;
    private ArrayList<Notification> notificationList = new ArrayList<>();
    private FirebaseFirestore db;

    /**
     * A small class to represent a single notification
     */
    static public class Notification {
        public String title;
        public String body;

        /**
         * Build a notification from a title and body
         * @param title the title
         * @param body the body
         */
        public Notification(String title, String body) {
            this.title = title;
            this.body = body;
        }

        /**
         * Get the map representation
         * @return the map
         */
        public HashMap<String, String> toMap() {
            HashMap<String, String> map = new HashMap<>();
            map.put("title", title);
            map.put("body", body);
            return map;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Notification that = (Notification) o;
            return Objects.equals(title, that.title) && Objects.equals(body, that.body);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, body);
        }
    }

    /**
     * Build a notification list that updates in realtime to the firestore version
     * @param db the Firestore db instance
     * @param user the user this list is associated to
     */
    public NotificationList(FirebaseFirestore db, User user) {
        this.user = user;
        this.db = db;

        db.collection("messages").document(user.getDeviceId())
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("NotificationFetching", error.toString());
                    }
                    if (value == null || value.getData() == null) {
                        return;
                    }
                    updateFromPayload(value.getData());
                });
    }

    @Override
    public void notifyObservers() {
        super.notifyObservers();
    }

    private void save() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("notificationList", toPayload());
        db.collection("messages").document(user.getDeviceId())
                .set(map).addOnFailureListener(e -> {
                    Log.e("SAVE DB", "fail at notification");
                });
    }

    /**
     * Get the representation of notification list for storage in firestore
     * @return the arraylist of notifications
     */
    public ArrayList<HashMap<String, String>> toPayload() {
        ArrayList<HashMap<String, String>> payload = new ArrayList<>();
        for (Notification notification : notificationList) {
            payload.add(notification.toMap());
        }

        return payload;
    }

    /**
     * Decode a Firestore payload and update instance state and notify observers
     * @param payload: the firestore payload
     */
    private void updateFromPayload(Map<String, Object> payload) {
        if (!(payload.get("notificationList") instanceof ArrayList)) {
            Log.d("TONY", user.getName());
            Log.e("Notifications", "notificationList was not an ArrayList!");
            return;
        }
        // TODO: resolve this cast uncheck
        ArrayList<HashMap<String, String>> arrayMap = (ArrayList<HashMap<String, String>>) payload.get("notificationList");

        if (arrayMap == null) {
            Log.w("Notifications", "arrayMap is null, meaning no notifications.");
            return;
        }

        boolean newNotifs = false;  // simple flag
        for (HashMap<String, String> item : arrayMap) {
            String title = Objects.requireNonNull(item.get("title"));
            String body = Objects.requireNonNull(item.get("body"));
            NotificationList.Notification notification = new Notification(title, body);
            if (notificationList.contains(notification)) {
                continue;
            }
            notificationList.add(notification);
            newNotifs = true;
        }

        if (newNotifs) {
            notifyObservers();
        }
    }

    /**
     * Get the underlying notification array list
     * @return the notification array list
     */
    public ArrayList<Notification> getNotificationList() {
        return notificationList;
    }

    /**
     * Add a notification to the list and save to db
     * @param title the title of the notification
     * @param body the body of the notification
     */
    public void addNotification(String title, String body) {
        notificationList.add(new Notification(title, body));
        save();
    }

    /**
     * Clear the notifications and save to db
     */
    public void clearNotifications() {
        this.notificationList.clear();
        save();
    }
}
