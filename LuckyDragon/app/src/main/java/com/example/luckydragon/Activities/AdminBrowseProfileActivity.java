package com.example.luckydragon.Activities;

import android.os.Bundle;

import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.User;
import com.example.luckydragon.R;
import com.example.luckydragon.Views.ProfileView;

public class AdminBrowseProfileActivity extends AppBarActivity{

    private User user;
    private GlobalApp.ROLE role;
    private ProfileView profileView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_browse_profile);
        getSupportActionBar().setTitle("Profile Management");
    }

}
