package com.example.luckydragon.userStoryTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.R;
import com.example.luckydragon.SelectRoleActivity;
import com.example.luckydragon.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Instant;

/**
 * Test for User Story 01.07.01.
 * Entrant - Be identified by my device, so that I don't have to use a username and password.
 */

/*
@RunWith(AndroidJUnit4.class)
@LargeTest
public class US_01_07_01_Test {
    private final int DB_WAIT_LIMIT_MS = 10000;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * USER STORY TEST
     * THIS TEST USES THE DATABASE.
     * IF IT FAILS, CHECK THAT THE TEST USER HAS NOT BEEN DELETED FROM THE DATABASE.
     * Existing user opens app and selects "Entrant".
     * User profile opens.
     *
     * Database Test User:
     *   Device ID: US_01_07_01_Test
     *   Name: John Doe
     *   Email: jdoe@gmail.com
     *   isEntrant: true
     *
     *   We could make it so these tests create the test user in the database, run the test, and then remove the test user from database.
     */
/*
    @Test
    public void testDeviceLogInExistingUser() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        // Set user to a user with the test device ID
        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setUser(null);
        globalApp.setDeviceId("US_01_07_01_Test");

        try (final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            waitForUserToLoad(globalApp.getUser(), DB_WAIT_LIMIT_MS);

            // User is not admin, so admin button should not show
            onView(ViewMatchers.withId(R.id.entrantButton)).check(matches(isDisplayed()));
            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
            onView(withId(R.id.adminButton)).check(matches(not(isDisplayed())));

            // Click entrant
            onView(withId(R.id.entrantButton)).perform(click());

            // Check that profile name and email match the test user
            onView(withId(R.id.nameTextView)).check(matches(withText("John Doe")));
            onView(withId(R.id.emailTextView)).check(matches(withText("jdoe@gmail.com")));
        }
    }


    /**
     * USER STORY TEST
     * THIS TEST USES THE DATABASE.
     * Non-existing user opens app and selects "Entrant".
     * Sign up page opens.
     *
     * The user exists in the db (for testing purposes) but has isEntrant set to false, so app treats them as a new user.
     */
/*
    @Test
    public void testDeviceLogInNonExistingUser() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        String nonExistingDeviceID = "US_01_07_01_Test_NOTEXISTS";

        // Set user to a user with the test device ID
        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setUser(null);
        globalApp.setDeviceId(nonExistingDeviceID);

        try (final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            waitForUserToLoad(globalApp.getUser(), DB_WAIT_LIMIT_MS);

            // User is new, admin button should not show
            onView(ViewMatchers.withId(R.id.entrantButton)).check(matches(isDisplayed()));
            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
            onView(withId(R.id.adminButton)).check(matches(not(isDisplayed())));

            // Click entrant
            onView(withId(R.id.entrantButton)).perform(click());

            // Sign up page should open
            onView(withId(R.id.signupName)).check(matches(isDisplayed()));
        }
    }

    private void waitForUserToLoad(User user, long waitLimitMs) throws RuntimeException {
        long startMs = Instant.now().toEpochMilli();
        while(!user.isLoaded()) {
            long elapsed = Instant.now().toEpochMilli() - startMs;
            if(elapsed > waitLimitMs) {
                throw new RuntimeException("User was not loaded within the specified wait limit time");
            }
        }
    }
}
 */