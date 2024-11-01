package com.example.luckydragon;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.CoreMatchers.not;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SelectRoleActivityTest {
    @Test
    public void testButtonsForNonAdminUser() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        Map<String, Object> mockUserData = new HashMap<>();
        mockUserData.put("Administrator", false);

        /*
         * Note that the observable for the mock controller is set to 'null' here.
         * It will be set to the correct observable within SelectRoleActivity.
         * This is done because we don't have access to the Observable here until the activity is started below (which is too late)
         */
        MockSelectRoleController mockSelectRoleController = new MockSelectRoleController(null, mockUserData);
        intent.putExtra("controller", mockSelectRoleController);

        try(final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.entrantButton)).check(matches(isDisplayed()));
            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
            onView(withId(R.id.adminButton)).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void testButtonsForAdminUser() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        Map<String, Object> mockUserData = new HashMap<>();
        mockUserData.put("isAdmin", true);

        MockSelectRoleController mockSelectRoleController = new MockSelectRoleController(null, mockUserData);
        intent.putExtra("controller", mockSelectRoleController);

        try(final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.entrantButton)).check(matches(isDisplayed()));
            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
            onView(withId(R.id.adminButton)).check(matches((isDisplayed())));
        }
    }
}
