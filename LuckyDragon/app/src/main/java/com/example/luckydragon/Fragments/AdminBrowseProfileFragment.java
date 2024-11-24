package com.example.luckydragon.Fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.User;
import com.example.luckydragon.R;

public class AdminBrowseProfileFragment extends Fragment {

    private User user;

    public AdminBrowseProfileFragment() {
        super(R.layout.fragment_admin_browse_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        GlobalApp globalApp = ((GlobalApp) requireActivity().getApplication());
        user = globalApp.getUserToView();
    }

}
