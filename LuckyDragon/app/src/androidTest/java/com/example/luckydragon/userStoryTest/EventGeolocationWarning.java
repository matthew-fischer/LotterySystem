package com.example.luckydragon.userStoryTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.luckydragon.Activities.EventActivity;
import com.example.luckydragon.Activities.ViewEventActivity;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.MockedDb;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.R;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class EventGeolocationWarning extends MockedDb {
    private String deviceId = "fakeDeviceId";
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
        testUserData.put("isAdmin", false);
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
     * USER STORY TEST
     * > US 01.08.01 Entrant - be warned before joining a waitlist that requires geolocation
     * Launch activity directly on event activity
     * Event has geolocation enabled
     * Text is displayed warning the user that this event has geolocation enabled
     */
    @Test
    public void testEventGeolocationWarning() {
        // Launch event activity directly
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);
        globalApp.setDeviceId(deviceId);
        globalApp.setRole(GlobalApp.ROLE.ENTRANT);
        // Set event to view
        Event testEvent = new Event(eventId, mockFirestore);
        testEvent.parseEventDocument(getMockEventData());
        testEvent.setHasGeolocation(true); // event has geolocation
        testEvent.setIsLoaded(true);
        globalApp.setEventToView(testEvent);
        // Launch event activity
        final Intent intent = new Intent(targetContext, ViewEventActivity.class);
        try(final ActivityScenario<ViewEventActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.geolocationMessage)).check(matches(isDisplayed()));
        }
    }
}
