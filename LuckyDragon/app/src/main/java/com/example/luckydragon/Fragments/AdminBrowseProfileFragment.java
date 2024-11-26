package com.example.luckydragon.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.luckydragon.Controllers.AdminBrowseProfileController;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.User;
import com.example.luckydragon.R;

import java.util.Objects;

/**
 * A fragment used by administrators to browse user profiles. This
 * fragment provides a UI to view user details and it interacts with
 * {@link AdminBrowseProfileController} to perform administrator actions
 * such as deleting an user.
 */
public class AdminBrowseProfileFragment extends Fragment {

    private User user;
    private AdminBrowseProfileController userController;

    public AdminBrowseProfileFragment() {
        super(R.layout.fragment_admin_browse_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        GlobalApp globalApp = ((GlobalApp) requireActivity().getApplication());
        user = globalApp.getUserToView();

        // Create controller
        userController = new AdminBrowseProfileController(user);

        // Set up delete profile button on click listener
        Button adminDeleteProfileButton = view.findViewById(R.id.adminDeleteProfileButton);
        adminDeleteProfileButton.setOnClickListener(v -> {
            if (Objects.equals(user.getDeviceId(), globalApp.getUser().getDeviceId())) {
                Toast.makeText(getContext(), "Cannot delete your own profile", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
            userController.deleteUser(user.getDeviceId());
            Toast.makeText(getContext(), "Profile Deleted Successfully", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
        });
    }

}
