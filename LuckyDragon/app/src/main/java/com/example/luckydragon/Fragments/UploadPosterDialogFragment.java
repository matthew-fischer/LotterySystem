/*
 * DialogFragment allowing an organizer to edit their event poster.
 */
package com.example.luckydragon.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.luckydragon.Controllers.EventController;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.R;

/**
 * Fragment for a user to upload an event poster.
 */
public class UploadPosterDialogFragment extends DialogFragment{
    private Event event;
    private Bitmap uploadedPoster;
    private EventController eventController;
    private ActivityResultLauncher<Intent> uploadImageResultLauncher;

    /**
     * Creates an UploadPosterDialogFragment.
     * @param event the event to upload poster for
     */
    public UploadPosterDialogFragment(Event event) {
        this.event = event;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_upload_poster, null);
        TextView posterFile = dialogView.findViewById(R.id.uploadPosterText);

        // Create controller
        eventController = new EventController(this.event);
        uploadImageResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data == null || data.getData() == null) return;
                            Uri image = data.getData();
                            try (Cursor cursor = getActivity().getContentResolver().query(image,
                                    null, null, null, null)) {
                                if (cursor == null) return;
                                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                cursor.moveToFirst();
                                String file = cursor.getString(nameIndex);

                                Bitmap eventPoster = MediaStore.Images.Media.getBitmap(
                                        getActivity().getContentResolver(), image);

                                // Crop image to correct width
                                int width = eventPoster.getWidth();
                                int height = eventPoster.getHeight();
                                eventPoster = Bitmap.createScaledBitmap(eventPoster, 200,
                                        (200 * height)/width, false);
                                posterFile.setText(file);
                                uploadedPoster = eventPoster;
                            } catch (Exception ignored) {};
                        }
                    }
                });
        ImageButton uploadPosterButton = dialogView.findViewById(R.id.uploadPosterButton);
        uploadPosterButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            uploadImageResultLauncher.launch(intent);
        });
        builder.setView(dialogView)
                .setPositiveButton("Confirm", (dialogInterface, i) -> {
                    if (uploadedPoster == null) {
                        Toast.makeText(getContext(), "Event poster was not changed", Toast.LENGTH_SHORT).show();
                    } else {
                        eventController.setEventPoster(uploadedPoster);
                    }
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                });

        Dialog dialog = builder.create();
        dialog.show();
        return dialog;
    }
}
