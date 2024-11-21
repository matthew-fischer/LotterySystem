package com.example.luckydragon.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;

import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.Location;
import com.example.luckydragon.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

// Heavily based on osmdroid docs: https://github.com/osmdroid/osmdroid/wiki/How-to-use-the-osmdroid-library-(Java)
public class OrganizerMapFragment extends DialogFragment {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private Event event;

    /**
     * Creates an OrganizerMapFragment.
     *
     * @param event the event to display the map for
     */
    public OrganizerMapFragment(Event event) {
        this.event = event;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate View
        View dialogView = inflater.inflate(R.layout.fragment_organizer_map, null);

        // load osmdroid config
        Context ctx = requireActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        // Initialize map
        map = (MapView) dialogView.findViewById(R.id.map);
        map.setHorizontalMapRepetitionEnabled(false);
        map.setVerticalMapRepetitionEnabled(false);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setZoomLevel(4); // 4 is a good starting zoom level for map to fill the dialog
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        // Place points on map
        ArrayList<OverlayItem> items = new ArrayList<>();

        boolean first = true;
        for (Location location : event.getWaitlistLocations()) {
            GeoPoint locationPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
            OverlayItem locationOverlay = new OverlayItem("Waitlisted User", "", locationPoint);
            items.add(locationOverlay);

            // Centre map on first location point
            if (first) {
                map.setExpectedCenter(locationPoint);
                first = true;
            }
        }

        ItemizedIconOverlay myOverlay = new ItemizedIconOverlay(items, null, requireContext());
        map.getOverlays().add(myOverlay);

        // Request permissions
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        requestPermissionsIfNecessary(permissions);

        dialogView.invalidate();

        return builder.setView(dialogView).setNegativeButton("Close", null).create();
    }

    /**
     * Requests the given permissions.
     *
     * @param permissions the permissions to request
     *                    <p>
     *                    Directly from osmdroid docs: https://github.com/osmdroid/osmdroid/wiki/How-to-use-the-osmdroid-library-(Java)
     */
    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}
