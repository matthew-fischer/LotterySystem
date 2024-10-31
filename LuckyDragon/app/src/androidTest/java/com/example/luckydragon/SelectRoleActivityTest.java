package com.example.luckydragon;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
@LargeTest
public class SelectRoleActivityTest {

    /**
     * TEST
     * Tests that only "Entrant" and "Organizer" buttons show for a user without admin privileges.
     * "Administrator" button should not be visible.
     */
    @Test
    public void testNonAdministrator() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        Map<String, Object> mockUserData = new HashMap<String, Object>();
        mockUserData.put("Administrator", false);

        MockUserController mockUserController = new MockUserController(mockUserData);
        intent.putExtra("Controller", mockUserController);

        try(final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
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
    public void testAdministrator() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        Map<String, Object> mockUserData = new HashMap<String, Object>();
        mockUserData.put("Administrator", true);

        MockUserController mockUserController = new MockUserController(mockUserData);
        intent.putExtra("Controller", mockUserController);

        try(final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.entrantButton)).check(matches(isDisplayed()));
            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
            onView(withId(R.id.adminButton)).check(matches(isDisplayed()));
        }
    }
}
