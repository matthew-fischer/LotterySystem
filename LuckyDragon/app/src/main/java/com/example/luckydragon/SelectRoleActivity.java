package com.example.luckydragon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 * Notes:
 * This activity gets the user profile from the database and passes it to the profile activity.
 * This will allow profile information in ProfileActivity to be displayed immediately, rather than with a delay.
 * ProfileActivity itself shouldn't need database access, but subfragments like EntrantProfileFragment and OrganizerProfileFragment will.
 * All users should be shown Entrant and Organizer roles, but the Admin button will only show for users that were set to Administrator in the database.
 * If a user has no profile in the database they should be taken to SignupActivity to make one.
 * After making one, we could bring them back to this page to select their role.
 */


public class SelectRoleActivity extends AppCompatActivity {
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.select_role_page);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        userController = (UserController) intent.getSerializableExtra("Controller");
        if(userController == null) {
            userController = new UserController();
        }

        String deviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        /*
         * TODO Add an on failure callback for when the user does not exist yet
         * Will need to add a second Consumer arg to userController.getUser() and define another function in SelectRoleActivity
         * I am leaving this for whoever has this user story to implement
         * - Ellis
         */
        userController.getUser(deviceID, this::initializeView);
    }

    /**
     * Initialize SelectRoleActivity once user data has been fetched from the database.
     * Sets content view to select_role_page.
     * Initializes button on click listeners.
     * Hides admin button if user is not an administrator.
     * @param userData map containing user data
     */
    public void initializeView(Map<String, Object> userData) {
        setContentView(R.layout.select_role_page); // set content to role page

        // Create profile intent
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        profileIntent.putExtra("deviceID", String.format("%s", userData.get("DeviceID")));
        profileIntent.putExtra("name", String.format("%s %s", userData.get("FirstName"), userData.get("LastName")));
        profileIntent.putExtra("email", String.format("%s", userData.get("Email")));
        profileIntent.putExtra("phoneNumber", String.format("%s", userData.get("PhoneNumber")));

        // Set up on click listeners
        Button entrantButton = findViewById(R.id.entrantButton);
        entrantButton.setOnClickListener(v -> {
            // TODO: Put additional entrant profile information in the intent
            profileIntent.putExtra("role", "ENTRANT");
            // Start profile activity
            startActivity(profileIntent);
        });

        Button organizerButton = findViewById(R.id.organizerButton);
        organizerButton.setOnClickListener(v -> {
            // Put additional organizer profile information in the intent
            profileIntent.putExtra("role", "ORGANIZER");
            profileIntent.putExtra("facilityName", String.format("%s", userData.get("Facility")));
            // Start profile activity
            startActivity(profileIntent);
        });

        // Hide admin button if user is not an admin
        Object isAdmin = userData.get("Administrator");
        if(isAdmin == null || !isAdmin.toString().equals("true")) {
            Button adminButton = findViewById(R.id.adminButton);
            adminButton.setVisibility(View.GONE);
        };
    }
}
