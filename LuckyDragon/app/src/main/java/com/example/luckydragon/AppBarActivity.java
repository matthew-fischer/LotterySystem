package com.example.luckydragon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * The Activity for the top level AppBar that is displayed on each page
 * within the app. Allows navigating back to the home page or the users
 * profile page.
 */
public abstract class AppBarActivity extends AppCompatActivity {
    private MenuItem navProfile;
    private boolean navProfileVisible = true;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SelectRoleActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        navProfile = menu.findItem(R.id.nav_profile);
        navProfile.setVisible(navProfileVisible);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_profile) {
//            Toast.makeText(this, "Click Profile Icon.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
//        } else if (item.getItemId() == R.id.nav_events) {
//            Intent intent = new Intent(getApplicationContext(), EventActivity.class);
//            startActivity(intent);
//        } else if (item.getItemId() == R.id.nav_signup) {
//            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
//            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets the profile navigation button to visible/invisible.
     * @param visible true if want to make visible, false otherwise
     */
    public void setNavProfileVisible(boolean visible) {
        if (navProfile == null) {
            Log.d("Custom", "setNavProfileVisible: Nav profile does not exist yet");
            navProfileVisible = visible;
            return;
        }
        navProfile.setVisible(visible);
        invalidateOptionsMenu();
    }
}
