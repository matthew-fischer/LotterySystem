package com.example.luckydragon.userStoryTest;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.luckydragon.Activities.SelectRoleActivity;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.MockedDb;
import com.example.luckydragon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains tests for US 02.06.01.
 * As an Organizer I want to view a list of all chosen entrants who are invited to apply.
 */
public class OrganizerViewInviteeListTest extends MockedDb {
    @Override
    protected Map<String, Object> getMockUserData() {
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
    protected void loadMockUserData(Map<String, Map<String, Object>> users) {
        super.loadMockUserData(users);
        // Define test user
        HashMap<String, Object> testUserData = new HashMap<>();
        // Personal info
        testUserData.put("name", "Matthew Fischer");
        testUserData.put("email", "matthewfischer@ualberta.ca");
        testUserData.put("phoneNumber", "780-831-3291");
        // Roles
        testUserData.put("isEntrant", true);
        testUserData.put("isOrganizer", false);
        testUserData.put("isAdmin", false);

        users.put("mf456", testUserData);

        HashMap<String, Object> testUserData2 = new HashMap<>();
        // Personal info
        testUserData2.put("name", "Tony Sun");
        testUserData2.put("email", "tonysun@ualberta.ca");
        testUserData2.put("phoneNumber", "780-831-3291");
        // Roles
        testUserData2.put("isEntrant", true);
        testUserData2.put("isOrganizer", false);
        testUserData2.put("isAdmin", false);

        users.put("ts123", testUserData2);
    }

    @Override
    protected void loadMockEventData(Map<String, Map<String, Object>> events) {
        super.loadMockEventData(events);
        // Initialize event data
        HashMap<String, Object> eventData1 = new HashMap<>();
        eventData1.put("name", "Piano Lesson");
        eventData1.put("organizerDeviceId", deviceId);
        eventData1.put("facility", "Piano Place");
        eventData1.put("waitListLimit", 10L);
        eventData1.put("attendeeLimit", 2L);
        eventData1.put("date", "2025-01-15");
        eventData1.put("hours", 18L);
        eventData1.put("minutes", 15L);
        eventData1.put("lotteryDate", "2060-09-01");  // a time in the future so we don't draw
        eventData1.put("lotteryHours", 8L);
        eventData1.put("lotteryMinutes", 0L);
        eventData1.put("inviteeList", List.of("ts123", "mf456"));
        eventData1.put("createdTimeMillis", 1731294000000L); // event created Nov 10 2024 8:00:00 PM

        events.put("fakeEvent1", eventData1);

        HashMap<String, Object> eventData2 = new HashMap<>();
        eventData2.put("name", "Group Piano Lesson");
        eventData2.put("organizerDeviceId", deviceId);
        eventData2.put("facility", "Piano Place");
        eventData2.put("waitListLimit", new Long(20));
        eventData2.put("attendeeLimit", new Long(5));
        eventData2.put("date", "2025-01-16");
        eventData2.put("hours", new Long(18));
        eventData2.put("minutes", new Long(15));

        events.put("fakeEvent2", eventData2);
    }

    /**
     * USER STORY TEST
     * US 02.06.01 -- As an organizer I want a view of all chosen entrants who are invited to apply.
     * User opens app and selects Organizer.
     * User's events are displayed.
     * User clicks on one of these events.
     * User sees the names of the entrants on the invited list (inviteelist).
     */
    @Test
    public void testInviteelistDisplayedEventData1() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);

        try(final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            // User is not admin, so admin button should not show
            onView(ViewMatchers.withId(R.id.entrantButton)).check(matches(isDisplayed()));
            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
            onView(withId(R.id.adminButton)).check(matches(not(isDisplayed())));

            // User clicks "Organizer"
            onView(withId(R.id.organizerButton)).perform(click());

            // Profile activity should open and organizer profile should be displayed
            onView(withId(R.id.organizerProfileLayout)).check(matches(isDisplayed()));

            // The organizer's facility is displayed
            onView(withId(R.id.facilityTextView)).check(matches(withText("The Sports Centre")));

            // The organizer's events should be displayed
            onView(withText("Piano Lesson")).check(matches(isDisplayed()));
            onView(withText("Group Piano Lesson")).check(matches(isDisplayed()));

            // Organizer clicks on "Piano Lesson"
            onView(withText("Piano Lesson")).perform(click());

            // Event info is displayed
            onView(withId(R.id.eventNameTextView)).check(matches(withText("Piano Lesson")));
            onView(withId(R.id.eventFacilityTextView)).check(matches(withText("Piano Place")));
            onView(withId(R.id.eventDateAndTimeTextView)).check(matches(withText("6:15 PM -- 2025-01-15")));

            // Inviteelist members are shown correctly
            onData(anything()).inAdapterView(withId(R.id.eventInvitelistListView))
                    .atPosition(1).onChildView(withId(R.id.entrantNameTextView)).check(matches(withText("Tony Sun")));
            onData(anything()).inAdapterView(withId(R.id.eventInvitelistListView))
                    .atPosition(0).onChildView(withId(R.id.entrantNameTextView)).check(matches(withText("Matthew Fischer")));
        }
    }
    @Test
    public void testInviteelistDisplayedEventData2() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);

        try(final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            // User is not admin, so admin button should not show
            onView(ViewMatchers.withId(R.id.entrantButton)).check(matches(isDisplayed()));
            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
            onView(withId(R.id.adminButton)).check(matches(not(isDisplayed())));

            // User clicks "Organizer"
            onView(withId(R.id.organizerButton)).perform(click());

            // Profile activity should open and organizer profile should be displayed
            onView(withId(R.id.organizerProfileLayout)).check(matches(isDisplayed()));

            // The organizer's facility is displayed
            onView(withId(R.id.facilityTextView)).check(matches(withText("The Sports Centre")));

            // The organizer's events should be displayed
            onView(withText("Piano Lesson")).check(matches(isDisplayed()));
            onView(withText("Group Piano Lesson")).check(matches(isDisplayed()));

            // Organizer clicks on "Piano Lesson"
            onView(withText("Group Piano Lesson")).perform(click());

            // Event info is displayed
            onView(withId(R.id.eventNameTextView)).check(matches(withText("Group Piano Lesson")));
            onView(withId(R.id.eventFacilityTextView)).check(matches(withText("Piano Place")));
            onView(withId(R.id.eventDateAndTimeTextView)).check(matches(withText("6:15 PM -- 2025-01-16")));

            // Inviteelist members are shown correctly
            onView(withText("Tony Sun")).check(doesNotExist());
            onView(withText("Matthew Fischer")).check(doesNotExist());
        }
    }
}
