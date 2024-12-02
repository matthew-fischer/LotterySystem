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
 * Contains test for US 03.01.01.
 * As an administrator, I want to be able to remove events.
 */
public class RemoveEventAdminTest extends MockedDb {

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
    public void testDeleteEvent() {

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

            // Admin profile fragment should open and view events button should be displayed
            onView(withId(R.id.viewEventsButton)).check(matches(isDisplayed()));

            // Admin clicks "View Events"
            onView(withId(R.id.viewEventsButton)).perform(click());

            // Check if the event "C301 Standup" is displayed
            onView(withText("C301 Standup")).check(matches(isDisplayed()));

            // Click on the event "C301 Standup"
            onView(withText("C301 Standup")).perform(click());

            // Admin event fragment should open and delete events button should be displayed
            onView(withId(R.id.adminDeleteEventButton)).check(matches(isDisplayed()));

            // Admin clicks "Delete Event" button
            onView(withId(R.id.adminDeleteEventButton)).perform(click());

            // Admin event fragment should close and the event list should be displayed
            onView(withId(R.id.adminProfileEventsListview)).check(matches(isDisplayed()));

            // Verify that "C301 Standup" is no longer displayed
            onView(withText("C301 Standup")).check(doesNotExist());

        }
    }

}
