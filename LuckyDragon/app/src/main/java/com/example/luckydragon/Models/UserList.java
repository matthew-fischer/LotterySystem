package com.example.luckydragon.Models;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class that manages and observes a list of User objects from a
 * firestore database.
 * <p>
 *     UserList listens for real-time updates from the firestore collection
 *     "users", as well as supports manual fetching of data.
 * </p>
 */
public class UserList extends Observable {

    private ArrayList<User> users = new ArrayList<>();
    private FirebaseFirestore db;

    /**
     * Constructs an UserList with the specified database instance.
     * <p>
     *     Sets up a real-time listener on the "events" collection to keep the
     *     users list updated with any changes from firestore.
     * </p>
     * @param db The firestore database instance
     */
    public UserList(FirebaseFirestore db) {
        Log.e("TEST", "Create userlist");
        this.db = db;

        db.collection("users").addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
            }
            if (value != null) {
                users.clear();
                /*
                for (QueryDocumentSnapshot doc: value) {
                    users.add(createUser(doc));
                }
                */
                for(int i = 0; i < value.size(); i++) {
                    users.add(createUser((QueryDocumentSnapshot) value.getDocuments().get(i)));
                }
                notifyObservers();
            }
        });
    }

    /**
     * Fetches the current users data from firestore and updates the users list.
     */
    public void fetchData() {
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        users.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            users.add(createUser(document));
                        }
                        Log.d(TAG, "Users loaded successfully with initial get()");
                        notifyObservers();
                    } else {
                        Log.w(TAG, "Error getting initial documents.", task.getException());
                    }
                });

    }

    /**
     * Returns the current lists of users.
     * @return An ArrayList of User objects.
     */
    public ArrayList<User> getUserList() {
        return users;
    }

    /**
     * Creates an User object from a firestore document.
     * @param document The firestore QueryDocumentSnapshot containing user data
     * @return An User object
     */
    public User createUser(QueryDocumentSnapshot document) {
        Map<String, Object> userData = document.getData();
        User user = new User(document.getId(), db, userData);
        return user;
    }

}
