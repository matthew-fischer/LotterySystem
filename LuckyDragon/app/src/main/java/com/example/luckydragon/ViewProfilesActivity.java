package com.example.luckydragon;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ViewProfilesActivity extends AppBarActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Set<User> userSet = new HashSet<>();
    private ArrayList<User> userList;
    private UserArrayAdapter userListAdapter;
    private ListView usersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_profiles);
        getSupportActionBar().setTitle("Profiles");

        // Set up admin users listview
        usersListView = findViewById(R.id.adminProfileUsersListview);
        userList = new ArrayList<>();
        userListAdapter = new UserArrayAdapter(userList, this);
        usersListView.setAdapter(userListAdapter);

        // Initial one-time load with get()
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        userSet.clear();
                        userList.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> userData = document.getData();
                            User user = new User(
                                    document.getId(),
                                    userData.get("name") instanceof String ? (String) eventData.get("name") : null,
                                    userData.get("organizerDeviceId") instanceof String ? (String) eventData.get("organizerDeviceId") : null,
                                    userData.get("facility") instanceof String ? (String) eventData.get("facility") : null,
                                    userData.get("waitListLimit") instanceof Number ? ((Number) eventData.get("waitListLimit")).intValue() : 0,
                                    userData.get("attendeeLimit") instanceof Number ? ((Number) eventData.get("attendeeLimit")).intValue() : 0,
                                    userData.get("date") instanceof String ? (String) eventData.get("date") : null,
                                    userData.get("hours") instanceof Number ? ((Number) eventData.get("hours")).intValue() : 0,
                                    userData.get("minutes") instanceof Number ? ((Number) eventData.get("minutes")).intValue() : 0
                            );

                            userSet.add(user);
                            userList.add(user);
                        }

                        userListAdapter.notifyDataSetChanged();
                        Log.d(TAG, "Users loaded successfully with initial get()");

                    } else {
                        Log.w(TAG, "Error getting initial documents.", task.getException());
                    }
                });

    }
}
