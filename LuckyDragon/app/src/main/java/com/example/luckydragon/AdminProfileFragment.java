package com.example.luckydragon;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class AdminProfileFragment extends Fragment {

    public AdminProfileFragment() {
        super(R.layout.fragment_admin_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        Button viewEvents = view.findViewById(R.id.viewEventsButton);
        Button viewProfiles = view.findViewById(R.id.viewProfilesButton);
        Button viewImages = view.findViewById(R.id.viewImagesButton);
        Button viewFacilities = view.findViewById(R.id.viewFacilitiesButton);

        viewEvents.setOnClickListener((View v) -> {

            Intent intent = new Intent(getActivity(), ViewEventsActivity.class);
            startActivity(intent);

        });

        viewProfiles.setOnClickListener((View v) -> {

            // TO DO

        });

        viewImages.setOnClickListener((View v) -> {

            // TO DO

        });

        viewFacilities.setOnClickListener((View v) -> {

            // TO DO

        });

    }
}
