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

import com.example.luckydragon.Activities.ViewEventsActivity;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.MockedDb;
import com.example.luckydragon.R;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class BrowseEventsAdminTest extends MockedDb{

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

        return eventData;
    }

    /**
     * ADMIN STORY TEST
     * >
     * US 03.04.01 As an administrator, I want to be able to browse events.
     * Launch activity directly on ViewEvents activity
     * Checks if listview with user profiles is being displayed
     */
    @Test
    public void testBrowseEvents() {

        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, ViewEventsActivity.class);

        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);
        // Launch event activity directly
        try (final ActivityScenario<ViewEventsActivity> scenario = ActivityScenario.launch(intent)) {
            SystemClock.sleep(5000);
            onView(withId(R.id.adminProfileEventsListview)).check(matches(isDisplayed()));
        }
    }

}
