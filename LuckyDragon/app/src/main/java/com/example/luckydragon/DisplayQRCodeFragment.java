package com.example.luckydragon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.zxing.common.BitMatrix;

public class DisplayQRCodeFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate View
        View dialogview = inflater.inflate(R.layout.displayqrcode_dialog, null);

        Event event = (Event) requireArguments().getSerializable("event");
        BitMatrix bitmatrix = event.generateQRCode();
        Bitmap bitmap = event.createBitMap(bitmatrix);

        // Reference: https://stackoverflow.com/questions/19337448/generate-qr-code-directly-into-imageview
        ImageView qrCode = (ImageView) dialogview.findViewById(R.id.qrCodeImageView);
        qrCode.setImageBitmap(bitmap);

        return builder.setView(dialogview)
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    dismiss();
                })
                .create();
    }
}
