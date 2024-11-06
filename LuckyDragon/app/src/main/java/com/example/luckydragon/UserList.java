package com.example.luckydragon;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserList extends Observable{

    private ArrayList<User> users = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void fetchData() {

        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        users.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> userData = document.getData();
                            User user = new User(
                                    userData.get("name") instanceof String ? (String) userData.get("name") : null,
                                    userData.get("email") instanceof String ? (String) userData.get("email") : null,
                                    userData.get("phoneNumber") instanceof String ? (String) userData.get("phoneNumber") : null,
                                    (Bitmap) User.stringToBitmap((String) userData.get("defaultProfilePicture")),
                                    (Bitmap) User.stringToBitmap((String) userData.get("profilePicture"))
                            );

                            users.add(user);
                        }

                        //userListAdapter.notifyDataSetChanged();
                        Log.d(TAG, "Users loaded successfully with initial get()");
                        notifyObservers();
                    } else {
                        Log.w(TAG, "Error getting initial documents.", task.getException());
                    }
                });

    }

    public ArrayList<User> getUserList() {

        return users;

    }

}
