package com.example.luckydragon.userStoryTest;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.luckydragon.Activities.EventActivity;
import com.example.luckydragon.Activities.ViewEventActivity;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.MockedDb;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LeaveWaitlistTest extends MockedDb {
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
        super.loadMockEventData(events);
        HashMap<String, Object> eventData = getMockEventData();

        events.put(eventId, eventData);
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

        return eventData;
    }

    /**
     * US 01.01.02 As an entrant, I want to leave the waiting list for a specific event
     * user clicks sign up to join the waitlist.
     * User then clicks cancel to leave the waitlist.
     * Opening the EntrantProfileFragment is another test file since we cannot test QR Scanner.
     */
    @Test
    public void testLeaveWaitlist() {
// Launch event activity directly
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);
        globalApp.setRole(GlobalApp.ROLE.ENTRANT);
        // Set event to view
        Event testEvent = new Event(eventId, mockFirestore);
        testEvent.parseEventDocument(getMockEventData());
        testEvent.setIsLoaded(true);
        globalApp.setEventToView(testEvent);
        // Launch event activity
        final Intent intent = new Intent(targetContext, ViewEventActivity.class);
        try(final ActivityScenario<ViewEventActivity> scenario = ActivityScenario.launch(intent)) {
            // User clicks Sign-Up
            onView(withId(R.id.waitlistActionButton)).check(matches(withText("Join Waitlist")));
            onView(withId(R.id.waitlistActionButton)).perform(click());

            // User should now be on the waitlist.
            assertEquals(1, testEvent.getWaitList().size());
            assertTrue(testEvent.getWaitList().contains(deviceId));

            // User clicks Cancel
            onView(withId(R.id.waitlistActionButton)).check(matches(withText("Cancel")));
            onView(withId(R.id.waitlistActionButton)).perform(click());

            // Waitlist should now be empty.
            assertEquals(0, testEvent.getWaitList().size());
        }
    }
}