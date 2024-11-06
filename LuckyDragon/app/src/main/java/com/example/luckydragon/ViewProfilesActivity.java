package com.example.luckydragon;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ViewProfilesActivity extends AppBarActivity {

    private UserList userList;
    private UserArrayAdapter userListAdapter;
    private ListView usersListView;
    private ViewProfilesView viewProfilesView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_profiles);
        getSupportActionBar().setTitle("Profiles");

        // Set up admin users listview
        userList = ((GlobalApp) getApplication()).getUsers();
        usersListView = findViewById(R.id.adminProfileUsersListview);
        userListAdapter = new UserArrayAdapter(userList.getUserList(), this);
        usersListView.setAdapter(userListAdapter);

        viewProfilesView = new ViewProfilesView(userList, this);

    }

    public void notifyAdapter() {

        userListAdapter.notifyDataSetChanged();

    }

}
