package com.example.luckydragon.userStoryTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.luckydragon.Event;
import com.example.luckydragon.EventActivity;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.MockedDb;
import com.example.luckydragon.R;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class EntrantInvitedEventTest extends MockedDb {
    private String deviceId = "fakeDeviceId";
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
        HashMap<String, Object> eventData = new HashMap<>();
        eventData.put("name", "C301 Standup");
        eventData.put("organizerDeviceId", "mockOrgId");
        eventData.put("facility", "UofA");
        eventData.put("waitListLimit", 10L);
        eventData.put("attendeeLimit", 10L);
        eventData.put("hasGeolocation", false);
        eventData.put("date", LocalDate.now().toString());
        eventData.put("hours", 10L);
        eventData.put("minutes", 30L);
        eventData.put("hashedQR", "Fake QR");
        eventData.put("waitList", new ArrayList<>());
        ArrayList<String> inviteeList = new ArrayList<>();
        inviteeList.add(deviceId);
        eventData.put("inviteeList", inviteeList);
        eventData.put("attendeeList", new ArrayList<>());
        eventData.put("cancelledList", new ArrayList<>());

        return eventData;
    }

    /**
     * USER STORY TEST
     * >
     * US 01.05.02 Entrant - be able to accept the invitation to
     *      register/sign up when chosen to participate in an event
     * Launch activity directly on event activity
     * User clicks accept
     * User is now part of the attendee list
     * TODO: Below
     * User can see that they are part of the attendee list
     * User can see on their profile they are on the attendee list
     */
    @Test
    public void testAcceptInvite() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);
        globalApp.setDeviceId(deviceId);

        // Launch event activity directly
        final Intent intent = new Intent(targetContext, EventActivity.class);
        intent.putExtra("eventID", eventId);
        try (final ActivityScenario<EventActivity> scenario = ActivityScenario.launch(intent)) {
            // Ensure we are on invitee list
            Event event = globalApp.getEvent(eventId);
            assertTrue(event.getInviteeList().contains(deviceId));
            assertTrue(event.getAttendeeList().isEmpty());
            // Click accept
            onView(withId(R.id.eventAccept)).perform(click());
            // Check we are on attendee list
            assertTrue(event.getAttendeeList().contains(deviceId));
            // And not on invitee
            assertTrue(event.getInviteeList().isEmpty());
        }
    }


}
