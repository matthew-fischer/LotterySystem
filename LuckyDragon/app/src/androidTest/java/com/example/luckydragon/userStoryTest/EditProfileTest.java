package com.example.luckydragon.userStoryTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.MockedDb;
import com.example.luckydragon.R;
import com.example.luckydragon.Activities.SelectRoleActivity;

import org.junit.Test;

import java.util.HashMap;

public class EditProfileTest extends MockedDb {
    @Override
    protected HashMap<String, Object> getMockData() {
        // Define test user
        HashMap<String, Object> testUserData = new HashMap<>();
        // Personal info
        testUserData.put("name", "John Doe");
        testUserData.put("email", "jdoe@ualberta.ca");
        testUserData.put("phoneNumber", "780-831-3291");
        // Roles
        testUserData.put("isEntrant", true);
        testUserData.put("isOrganizer", true);
        testUserData.put("isAdmin", false);
        // Facility
        testUserData.put("facility", "The Sports Centre");

        return testUserData;
    }
    @Override
    protected HashMap<String, Object> getMockEventData() {
        return null;  // all events do not exist
    }

    /**
     * USER STORY TEST
     * > US 01.02.02 Entrant - update information such as name, email
     *      and contact information on my profile
     * User opens app and selects "Entrant".
     * User is shown the profile page
     * User clicks edit button
     * User enters in info
     * User clicks Submit
     * User is now in their profile with their provided info displayed.
     */
    @Test
    public void testEntrantEdit() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);

        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);
        try (final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            String newName = "Jake Paul";
            String newEmail = "jake@youtube.com";
            String newPhone = "1231231234";
            // Click entrant button
            onView(withId(R.id.entrantButton)).perform(click());

            // Check we are in profile
            onView(withId(R.id.nameTextView)).check(matches(isDisplayed()));

            // Click edit
            onView(withId(R.id.edit_profile_button)).perform(click());

            // Enter in info
            onView(withId(R.id.signupName)).perform(ViewActions.clearText());
            onView(withId(R.id.signupEmail)).perform(ViewActions.clearText());
            onView(withId(R.id.signupPhone)).perform(ViewActions.clearText());

            onView(withId(R.id.signupName)).perform(ViewActions.typeText(newName));
            onView(withId(R.id.signupEmail)).perform(ViewActions.typeText(newEmail));
            onView(withId(R.id.signupPhone)).perform(ViewActions.typeText(newPhone));

            // Click confirm
            onView(withText("Submit")).perform(click());

            // Check that profile displays info
            onView(withId(R.id.nameTextView)).check(matches(withText(newName)));
            onView(withId(R.id.emailTextView)).check((matches(withText(newEmail))));
            onView(withId(R.id.phoneNumberTextView)).check((matches(withText(newPhone))));
        }
    }

}
