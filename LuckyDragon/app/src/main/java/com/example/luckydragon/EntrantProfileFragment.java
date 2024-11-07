package com.example.luckydragon;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class EntrantProfileFragment extends Fragment {
    public EntrantProfileFragment() {
        super(R.layout.fragment_entrant_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {


        Button scanQRButton = view.findViewById(R.id.scanQRButton);
        // Reference: https://www.geeksforgeeks.org/how-to-read-qr-code-using-zxing-library-in-android/
        scanQRButton.setOnClickListener((View v) -> {

            // This is for testing without scanning QR Code:
            Intent intent = new Intent(getActivity(), EventActivity.class);
            String eventId = "NR6CbgJwPFBmzmNWLYn1";
            intent.putExtra("eventID", eventId);
            String deviceID = ((GlobalApp) getActivity().getApplication()).getUser().getDeviceId();
            intent.putExtra("deviceID", deviceID);
            startActivity(intent);

            // This is for actually scanning the QR Code.
//            IntentIntegrator intentIntegrator = IntentIntegrator.forSupportFragment(EntrantProfileFragment.this);
//            intentIntegrator.setPrompt("Scan QR Code");
//            intentIntegrator.initiateScan();
        });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
//        super.onActivityResult(requestCode, resultCode, data);
//        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (intentResult != null) {
//            if (intentResult.getContents() == null) {
//                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
//            } else {
//                Intent intent = new Intent(getActivity(), EventActivity.class);
//                // Pass in event eventId (from QR CODE SCANNER)
//                String eventID = intentResult.getContents();
//                intent.putExtra("eventID", eventID);
//
//                // start EventActivity
//                startActivity(intent);
//            }
//        }
//    }
}
