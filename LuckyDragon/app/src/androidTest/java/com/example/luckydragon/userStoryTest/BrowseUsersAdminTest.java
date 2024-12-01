package com.example.luckydragon.userStoryTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.luckydragon.Activities.ViewProfilesActivity;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.MockedDb;
import com.example.luckydragon.R;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class BrowseUsersAdminTest extends MockedDb{

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
    protected HashMap<String, Object> getMockEventData() {
        HashMap<String, Object> eventData = new HashMap<>();
        eventData.put("name", "C301 Standup");
        eventData.put("organizerDeviceId", "mockOrgId");
        eventData.put("facility", "UofA");
        eventData.put("waitListLimit", new Long(10));
        eventData.put("attendeeLimit", new Long(10));
        eventData.put("hasGeolocation", true);
        eventData.put("date", LocalDate.now().toString());
        eventData.put("hours", new Long(10));
        eventData.put("minutes", new Long(30));
        eventData.put("hashedQR", "Fake QR");
        eventData.put("waitList", new ArrayList<>());
        eventData.put("inviteeList", new ArrayList<>());
        eventData.put("attendeeList", new ArrayList<>());
        eventData.put("cancelledList", new ArrayList<>());

        return eventData;
    }

    /**
     * ADMIN STORY TEST
     * >
     * US 03.05.01 As an administrator, I want to be able to browse profiles.
     * Launch activity directly on ViewProfiles activity
     * Checks if listview with events is being displayed
     */
    @Test
    public void testBrowseUsers() {

        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, ViewProfilesActivity.class);

        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();

        // Launch event activity directly
        try (final ActivityScenario<ViewProfilesActivity> scenario = ActivityScenario.launch(intent)) {
            SystemClock.sleep(5000);
            onView(withId(R.id.adminProfileUsersListview)).check(matches(isDisplayed()));
        }
    }

}
