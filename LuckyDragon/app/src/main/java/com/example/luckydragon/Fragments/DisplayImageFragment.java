package com.example.luckydragon.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.luckydragon.Models.Event;
import com.example.luckydragon.R;
import com.google.zxing.common.BitMatrix;

/**
 * A {@link DialogFragment} for displaying an image along with a title in a dialog.
 * Typically used to show QR codes or other bitmaps in a dialog.
 */
public class DisplayImageFragment extends DialogFragment {
    public DisplayImageFragment() {
        super();
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate View
        View dialogview = inflater.inflate(R.layout.displayqrcode_dialog, null);

        // Set image and title
        ImageView imageView = dialogview.findViewById(R.id.qrCodeImageView);
        TextView titleView = dialogview.findViewById(R.id.qrCodeTextView);

        Bitmap image = requireArguments().getParcelable("image");
        String title = requireArguments().getString("title");
        String negativeText = requireArguments().getString("negativeButton");
        if (negativeText == null) {
            negativeText = "Cancel";
        }
        imageView.setImageBitmap(image);
        titleView.setText(title);

        return builder.setView(dialogview)
                .setNegativeButton(negativeText, (dialogInterface, i) -> {
                    dismiss();
                })
                .create();
    }
}


