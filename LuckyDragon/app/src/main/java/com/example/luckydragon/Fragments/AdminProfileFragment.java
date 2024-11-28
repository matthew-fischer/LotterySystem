package com.example.luckydragon.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.luckydragon.Activities.ViewEventsActivity;
import com.example.luckydragon.Activities.ViewProfilesActivity;
import com.example.luckydragon.R;

/**
 * Fragment representing the profile view for an administrator.
 * <p>
 *     This fragment displays a set of buttons that allow an
 *     administrator to navigate to various lists such as
 *     browsing events, profiles, images, and facilities.
 * </p>
 */
public class AdminProfileFragment extends Fragment {

    Intent intent;
    public AdminProfileFragment() {
        super(R.layout.fragment_admin_profile);
    }

    /**
     * Called after the fragment's view has been created. This method initializes the
     * buttons on the fragment's view and sets up click listeners for each button to navigate to
     * the lists.
     *
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        // Initialize all the buttons
        Button viewEvents = view.findViewById(R.id.viewEventsButton);
        Button viewProfiles = view.findViewById(R.id.viewProfilesButton);

        // Click listeners for all the buttons
        viewEvents.setOnClickListener((View v) -> {

            intent = new Intent(getActivity(), ViewEventsActivity.class);
            startActivity(intent);

        });

        viewProfiles.setOnClickListener((View v) -> {

            intent = new Intent(getActivity(), ViewProfilesActivity.class);
            startActivity(intent);

        });

    }
}
