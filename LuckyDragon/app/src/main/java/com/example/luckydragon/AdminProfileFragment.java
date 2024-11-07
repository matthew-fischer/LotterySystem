package com.example.luckydragon;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class AdminProfileFragment extends Fragment {

    Intent intent;
    public AdminProfileFragment() {
        super(R.layout.fragment_admin_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

//        Log.d("HERE", "ADMIN HERE/");
        Button viewEvents = view.findViewById(R.id.viewEventsButton);
        Button viewProfiles = view.findViewById(R.id.viewProfilesButton);
        Button viewImages = view.findViewById(R.id.viewImagesButton);
        Button viewFacilities = view.findViewById(R.id.viewFacilitiesButton);

        viewEvents.setOnClickListener((View v) -> {

            intent = new Intent(getActivity(), ViewEventsActivity.class);
            startActivity(intent);

        });

        viewProfiles.setOnClickListener((View v) -> {

            intent = new Intent(getActivity(), ViewProfilesActivity.class);
            startActivity(intent);

        });

        viewImages.setOnClickListener((View v) -> {

            // TO DO

        });

        viewFacilities.setOnClickListener((View v) -> {

            // TO DO

        });

    }
}
