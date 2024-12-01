package com.example.luckydragon.userStoryTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.luckydragon.Activities.SelectRoleActivity;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.MockedDb;
import com.example.luckydragon.Models.User;
import com.example.luckydragon.R;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DefaultGeneratedProfilePictureTest extends MockedDb {
    @Override
    protected HashMap<String, Object> getMockUserData() {
        return null;  // New user
    }
    @Override
    protected void loadMockEventData(Map<String, Map<String, Object>> events) {}

    /**
     * USER STORY TEST
     * > US 01.03.03 Entrant - profile picture to be deterministically generated from
     *      my profile name if I haven't uploaded a profile image yet.
     * User opens app and selects 'Entrant'.
     * User is shown the signup page
     * User enters name and email.
     * User clicks 'Submit'
     * User has no uploaded profile picture, but they have a generated profile picture.
     */
    @Test
    public void testDefaultProfilePictureGenerated() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);
        User user = globalApp.getUser();
        // Sign up information
        String name = "John Draco";
        String email = "draco@gmail.com";
        String phone = "1231231234";
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);
        try (final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            // Click entrant button
            onView(withId(R.id.entrantButton)).check(matches(isDisplayed()));
            onView(withId(R.id.entrantButton)).perform(click());

            // Check we are in signup
            onView(withId(R.id.signupName)).check(matches(isDisplayed()));

            // Enter in info
            onView(withId(R.id.signupName)).perform(ViewActions.typeText(name));
            onView(withId(R.id.signupEmail)).perform(ViewActions.typeText(email));
            onView(withId(R.id.signupPhone)).perform(typeText(phone));
            // Click submit
            onView(withId(R.id.signupSubmit)).check(matches(isDisplayed()));
            onView(withText("Submit")).check(matches(isEnabled()));
            onView(withText("Submit")).perform(click());

            // Check that user has a default profile picture
            assertNotNull(user.getDefaultProfilePicture());

            // Check that the default profile picture is the one being used
            assertTrue(user.getDefaultProfilePicture().sameAs(user.getProfilePicture()));

            // Check that default profile picture is generated the same for the same name
            Bitmap expected = User.generateProfilePicture(name);
            assertTrue(user.getDefaultProfilePicture().sameAs(expected));
        }
    }
}
