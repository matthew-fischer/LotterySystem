package com.example.luckydragon.Activities;

import android.os.Bundle;
import android.widget.ListView;

import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.UserList;
import com.example.luckydragon.R;
import com.example.luckydragon.Controllers.UserArrayAdapter;
import com.example.luckydragon.Views.ViewProfilesView;

/**
 * Activity for viewing a list of all the user profiles.
 * <p>
 *     The ViewProfilesActivity displays a list of users in a
 *     ListView with functionality to view details of every
 *     individual user.
 * </p>
 */
public class ViewProfilesActivity extends AppBarActivity {

    private UserList userList;
    private UserArrayAdapter userListAdapter;
    private ListView usersListView;
    private ViewProfilesView viewProfilesView;

    /**
     * Called when the activity is first created. Initializes the view components, including
     * setting up the user list and its adapter, and configuring the list item click behavior.
     * @param savedInstanceState
     */
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

    /**
     * Notifies the adapter that the data has changed.
     */
    public void notifyAdapter() {

        userListAdapter.notifyDataSetChanged();

    }

}
