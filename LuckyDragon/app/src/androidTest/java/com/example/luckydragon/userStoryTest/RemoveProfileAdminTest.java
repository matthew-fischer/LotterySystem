package com.example.luckydragon.userStoryTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.luckydragon.Activities.SelectRoleActivity;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.MockedDb;
import com.example.luckydragon.R;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains test for US 03.02.01.
 * As an administrator, I want to be able to remove profiles.
 */
public class RemoveProfileAdminTest extends MockedDb {

    @Override
    protected HashMap<String, Object> getMockUserData() {
        // Define test user
        HashMap<String, Object> testUserData = new HashMap<>();
        // Personal info
        testUserData.put("name", "John Doe");
        testUserData.put("email", "jdoe@ualberta.ca");
        testUserData.put("phoneNumber", "780-831-3291");
        // Roles
        testUserData.put("isEntrant", true);
        testUserData.put("isOrganizer", true);
        testUserData.put("isAdmin", true);
        // Facility
        testUserData.put("facility", "The Sports Centre");

        return testUserData;
    }

    @Override
    protected void loadMockUserData(Map<String, Map<String, Object>> users) {
        super.loadMockUserData(users);
        // Define test user
        HashMap<String, Object> testUserData = new HashMap<>();
        // Personal info
        testUserData.put("name", "Tony Sun");
        testUserData.put("email", "tonysun@ualberta.ca");
        testUserData.put("phoneNumber", "780-827-7821");
        // Roles
        testUserData.put("isEntrant", true);
        testUserData.put("isOrganizer", true);

        String id = "user123";
        users.put(id, testUserData);

    }

    @Override
    protected void loadMockEventData(Map<String, Map<String, Object>> events) {
        HashMap<String, Object> eventData = new HashMap<>();
        eventData.put("name", "C301 Standup");
        eventData.put("organizerDeviceId", "mockOrgId");
        eventData.put("facility", "UofA");
        eventData.put("waitListLimit", 10L);
        eventData.put("attendeeLimit", 10L);
        eventData.put("hasGeolocation", true);
        eventData.put("date", LocalDate.now().toString());
        eventData.put("hours", 10L);
        eventData.put("minutes", 30L);
        eventData.put("hashedQR", "Fake QR");
        eventData.put("waitList", new ArrayList<>());
        eventData.put("inviteeList", new ArrayList<>());
        eventData.put("attendeeList", new ArrayList<>());
        eventData.put("cancelledList", new ArrayList<>());

        events.put("asdf123", eventData);

        HashMap<String, Object> eventData2 = new HashMap<>();
        eventData2.put("name", "C401 Standup");
        eventData2.put("organizerDeviceId", "mockOrgId");
        eventData2.put("facility", "UofA");
        eventData2.put("waitListLimit", 10L);
        eventData2.put("attendeeLimit", 10L);
        eventData2.put("hasGeolocation", true);
        eventData2.put("date", LocalDate.now().toString());
        eventData2.put("hours", 10L);
        eventData2.put("minutes", 30L);
        eventData2.put("hashedQR", "Fake QR");
        eventData2.put("waitList", new ArrayList<>());
        eventData2.put("inviteeList", new ArrayList<>());
        eventData2.put("attendeeList", new ArrayList<>());
        eventData2.put("cancelledList", new ArrayList<>());

        events.put("asdf456", eventData2);
    }

    @Test
    public void testDeleteProfile() {

        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);

        try (final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            // User is admin, so admin button should show
            onView(ViewMatchers.withId(R.id.entrantButton)).check(matches(isDisplayed()));
            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
            onView(withId(R.id.adminButton)).check(matches(isDisplayed()));

            // Admin clicks "Administrator"
            onView(withId(R.id.adminButton)).perform(click());

            // Admin profile fragment should open and view profiles button should be displayed
            onView(withId(R.id.viewProfilesButton)).check(matches(isDisplayed()));

            // Admin clicks "View Profiles"
            onView(withId(R.id.viewProfilesButton)).perform(click());

            // Check if the user "Tony Sun" is displayed
            onView(withText("Tony Sun")).check(matches(isDisplayed()));

            // Click on the user "Tony Sun"
            onView(withText("Tony Sun")).perform(click());

            // Admin profile fragment should open and delete profile button should be displayed
            onView(withId(R.id.adminDeleteProfileButton)).check(matches(isDisplayed()));

            // Admin clicks "Delete Profile" button
            onView(withId(R.id.adminDeleteProfileButton)).perform(click());

            // Admin profile fragment should close and the user list should be displayed
            onView(withId(R.id.adminProfileUsersListview)).check(matches(isDisplayed()));

            // Verify that "Tony Sun" is no longer displayed
            onView(withText("Tony Sun")).check(doesNotExist());

        }
    }

}
