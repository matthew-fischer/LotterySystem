/*
 * Defines SelectRoleActivity which is the opening page of the app.
 * Has three buttons: entrant, organizer, administrator.
 * When a user selects "entrant", they will be using the app in entrant mode.
 * If they then want to use organizer functionality, they can return to this page and select a new mode.
 * The admin button will only be shown to users with admin privileges.
 * Associated with SelectRoleView.
 * ISSUES:
 *   NONE
 */

package com.example.luckydragon.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.Models.User;
import com.example.luckydragon.R;
import com.example.luckydragon.Views.SelectRoleView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * This is the activity for the select role page.
 * Shows "entrant", "organizer", and "administrator" buttons.
 * Allows user to choose the mode in which they wish to use the app.
 * All buttons are hidden until the user is loaded.
 */
public class SelectRoleActivity extends AppCompatActivity {
    private User user;
    private SelectRoleView selectRoleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.select_role_page);
        super.onCreate(savedInstanceState);

        // Get user
        user = ((GlobalApp) getApplication()).getUser();
        // Create view
        selectRoleView = new SelectRoleView(user, this);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TONY", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = token;
//                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("TONY1", msg);
//                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Set button visibilities and on click listeners.
     * This runs once the user data has been fetched, so that admin button can be hidden if user is not an admin.
     */
    public void initializeView() {
        // Set up on entrant click listener
        Button entrantButton = findViewById(R.id.entrantButton);
        entrantButton.setVisibility(View.VISIBLE);
        if (!entrantButton.hasOnClickListeners()) {
            entrantButton.setOnClickListener(v -> {
                // Set GlobalApp role to entrant
                ((GlobalApp) getApplication()).setRole(GlobalApp.ROLE.ENTRANT);
                if (user.isEntrant()) {
                    // Create profile intent
                    Intent profileIntent = new Intent(this, ProfileActivity.class);
                    // Start profile activity
                    startActivity(profileIntent);
                } else {
                    // Send to entrant signup
                    Intent signupIntent = new Intent(this, SignupActivity.class);
                    startActivity(signupIntent);
                }
            });
        }

        // Set up on organizer click listener
        Button organizerButton = findViewById(R.id.organizerButton);
        organizerButton.setVisibility(View.VISIBLE);
        if (!organizerButton.hasOnClickListeners()) {
            organizerButton.setOnClickListener(v -> {
                // Set GlobalApp role to organizer
                ((GlobalApp) getApplication()).setRole(GlobalApp.ROLE.ORGANIZER);
                if (user.isOrganizer()) {
                    // Create profile intent
                    Intent profileIntent = new Intent(this, ProfileActivity.class);
                    // Start profile activity
                    startActivity(profileIntent);
                } else {
                // Send to signup
                Intent signupIntent = new Intent(this, SignupActivity.class);
                startActivity(signupIntent);
            }
            });
        }

        // Admin button
        if (user.isAdmin()) {
            Button adminButton = findViewById(R.id.adminButton);
            if (!adminButton.hasOnClickListeners()) {
                adminButton.setOnClickListener(v -> {
                    // Create profile intent
                    ((GlobalApp) getApplication()).setRole(GlobalApp.ROLE.ADMINISTRATOR);
                    Intent profileIntent = new Intent(this, ProfileActivity.class);
                    // Start profile activity
                    startActivity(profileIntent);
                });
            }
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
//            }
//        }
//
//        // Based off: https://developer.android.com/develop/ui/views/notifications/build-notification
//        // Inside your activity or service
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String channelId = "my_channel_id";
//            String channelName = "My Channel";
//            String channelDescription = "Description of my channel";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//
//            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
//            channel.setDescription(channelDescription);
//
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        // Inside your activity or service
//        String channelId = "my_channel_id"; // Use the same channel ID created earlier
//        int notificationId = 1; // Unique ID for the notification
//
//        // Create an intent to open an activity when the notification is tapped
//        Intent intent = new Intent(this, ProfileActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        // Build the notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(R.drawable.baseline_notifications_24) // Replace with your drawable resource
//                .setContentTitle("My Notification Title")
//                .setContentText("This is the notification content.")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // For older devices
//                .setContentIntent(pendingIntent) // Add the intent
//                .setAutoCancel(true); // Dismiss the notification on tap
//
//        // Show the notification
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(notificationId, builder.build());
    }

    /**
     * Sets admin button visibility to VISIBLE.
     */
    public void showAdminButton() {
        Button adminButton = findViewById(R.id.adminButton);
        adminButton.setVisibility(View.VISIBLE);
    }
}
