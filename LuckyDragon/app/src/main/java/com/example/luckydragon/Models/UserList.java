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

    public UserList(FirebaseFirestore db) {
        this.db = db;
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
                            Map<String, Object> userData = document.getData();
                            User user = new User(
                                    document.getId(),
                                    db,
                                    userData
                            );
                            users.add(user);
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

}
