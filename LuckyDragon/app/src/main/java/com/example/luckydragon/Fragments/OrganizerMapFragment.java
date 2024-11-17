package com.example.luckydragon.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import com.example.luckydragon.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;


public class OrganizerMapFragment extends DialogFragment {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
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
        ItemizedIconOverlay myOverlay = new ItemizedIconOverlay(items, null, requireContext());
        map.getOverlays().add(myOverlay);

        // TODO add all points to items
        GeoPoint edmontonPoint = new GeoPoint(53.546932, -113.498871); // Rogers Place
        OverlayItem edmontonOverlay = new OverlayItem("Rogers Place", "Rogers", edmontonPoint);
        items.add(edmontonOverlay);

        // Request permissions
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        requestPermissionsIfNecessary(permissions);

        dialogView.invalidate();

        return builder.setView(dialogView).setNegativeButton("Close", null).create();
    }

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
