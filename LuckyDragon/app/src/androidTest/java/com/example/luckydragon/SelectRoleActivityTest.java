/**
 * Contains tests for SelectRoleActivity.
 */

package com.example.luckydragon;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.filters.LargeTest;

import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.CoreMatchers.not;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

/**
 * This is a test class for SelectRoleActivity.
 * It tests that the correct role buttons show for a user depending on whether they have admin privileges or not.
 * NOTES:
 *   - The User is set in GlobalApp before the activity is started, so no database fetching will occur in these tests.
 *   - Use the ActivityScenario.launch() syntax to run some code before the activity starts. ActivityScenarioRule does not allow this.
 *   - We need to call notifyObservers() on the test user to update the views to reflect the user. This must be done on the UI thread, or an error will occur.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SelectRoleActivityTest {
    /**
     * TEST
     * Tests that only "Entrant" and "Organizer" buttons show for a user without admin privileges.
     * "Administrator" button should not be visible.
     */
    @Test
    public void testButtonsForNonAdminUser() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        // Set user to a test object
        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        User testUser = new User("test");
        testUser.setIsLoaded(true);
        globalApp.setUser(testUser);

        try(final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            // Update views to match testUser (this must run on ui thread -- error otherwise)
            scenario.onActivity(a -> {
                a.runOnUiThread(testUser::notifyObservers);
            });

            // Assertions
            onView(withId(R.id.entrantButton)).check(matches(isDisplayed()));
            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
            onView(withId(R.id.adminButton)).check(matches(not(isDisplayed())));
        }
    }

    /**
     * TEST
     * Tests that "Entrant", "Organizer", and "Administrator" buttons show for a user with admin privileges.
     */
    @Test
    public void testButtonsForAdminUser() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        // Set user to a test object
        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        User testUser = new User("test");
        testUser.setAdmin(true);
        testUser.setIsLoaded(true);
        globalApp.setUser(testUser);

        try(final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            // Update views to match testUser (this must run on ui thread -- error otherwise)
            scenario.onActivity(a -> {
                a.runOnUiThread(testUser::notifyObservers);
            });

            // Assertions
            onView(withId(R.id.entrantButton)).check(matches(isDisplayed()));
            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
            onView(withId(R.id.adminButton)).check(matches((not(isDisplayed()))));
        }
    }
}
