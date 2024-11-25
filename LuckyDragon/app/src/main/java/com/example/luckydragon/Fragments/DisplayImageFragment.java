package com.example.luckydragon.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.luckydragon.Models.Event;
import com.example.luckydragon.R;
import com.google.zxing.common.BitMatrix;

public class DisplayImageFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate View
        View dialogview = inflater.inflate(R.layout.displayqrcode_dialog, null);

        Event event = (Event) requireArguments().getSerializable("event");
        Bitmap poster = event.getEventPoster();
        // Reference: https://stackoverflow.com/questions/19337448/generate-qr-code-directly-into-imageview
        ImageView qrCode = (ImageView) dialogview.findViewById(R.id.qrCodeImageView);
        qrCode.setImageBitmap(poster);

        return builder.setView(dialogview)
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    dismiss();
                })
                .create();
    }
}


