package com.example.luckydragon.userStoryTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.luckydragon.Activities.SelectRoleActivity;
import com.example.luckydragon.Activities.ViewEventActivity;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.MockedDb;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.EventList;
import com.example.luckydragon.R;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EntrantInvitedEventDenyTest extends MockedDb {
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
    protected void loadMockEventData(Map<String, Map<String, Object>> events) {
        HashMap<String, Object> eventData = getMockEventData();

        String id = String.valueOf(new Random().nextInt());
        events.put(id, eventData);
    }

    private HashMap<String, Object> getMockEventData() {
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
     * US 01.05.03
     * Entrant - be able to decline an invitation when chosen to participate in an event
     * Launch activity directly on event activity
     * User clicks decline
     * User is now part of the cancelled list
     * User sees a message saying the waitlist is closed
     * TODO below
     *      User can see that they are part of the cancelled list
     *      User can see on their profile they are on the cancelled list
     */
    @Test
    public void testDeclineInvite() {
        // Launch event activity directly
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);
        globalApp.setRole(GlobalApp.ROLE.ENTRANT);
        // Launch activity
        try(final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            // User clicks "Entrant"
            onView(withId(R.id.entrantButton)).perform(click());

            // Organizer clicks on event
            onView(withText("C301 Standup")).perform(click());

            // Ensure we are on invitee list
            EventList eventList = globalApp.getEvents();
            String testEventName = "C301 Standup";
            Event testEvent = null;
            for (Event e: eventList.getEventList()) {
                if (e.getName().equals(testEventName)) {
                    testEvent = e;
                }
            }
            assertTrue(testEvent.getInviteeList().contains(deviceId));
            assertTrue(testEvent.getAttendeeList().isEmpty());

            // Click decline
            onView(withId(R.id.invitationDeclineButton)).perform(click());

            // Check we are on cancelled list
            eventList = globalApp.getEvents();  // refresh event
            testEvent = null;
            for (Event e: eventList.getEventList()) {
                if (e.getName().equals(testEventName)) {
                    testEvent = e;
                }
            }
            assertTrue(testEvent.getCancelledList().contains(deviceId));
            // And not on invitee
            assertTrue(testEvent.getInviteeList().isEmpty());
            // And not on attending
            assertTrue(testEvent.getAttendeeList().isEmpty());
            // Check that we are taken to the waitlist closed fragment
            onView(withId(R.id.waitlistClosedMessage)).check(matches(isDisplayed()));
        }
    }
}
