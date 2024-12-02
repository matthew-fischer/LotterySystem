package com.example.luckydragon.userStoryTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.luckydragon.Activities.SelectRoleActivity;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.MockedDb;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.NotificationList;
import com.example.luckydragon.Models.User;
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
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains tests for US 02.07.01, 02.07.042, 02.07.03, and 02.05.01.
 * As an organizer I want to send notifications to all waiting/cancelled/selected/invited entrants.
 */
public class OrganizerSendNotificationsTest extends MockedDb {
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
        eventData1.put("lotteryDate", "2024-09-01");
        eventData1.put("lotteryHours", 8L);
        eventData1.put("lotteryMinuts", 0L);
        eventData1.put("waitList", List.of("ts123", "mf456"));
        eventData1.put("createdTimeMillis", 1731294000000L); // event created Nov 10 2024 8:00:00 PM

        events.put("fakeEventId", eventData1);
    }

    @Override
    protected void loadMockUserData(Map<String, Map<String, Object>> users) {
        super.loadMockUserData(users);
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
        testUserData.put("name", "Tony Sun");
        testUserData.put("email", "tonysun@ualberta.ca");
        testUserData.put("phoneNumber", "780-831-3291");
        // Roles
        testUserData.put("isEntrant", true);
        testUserData.put("isOrganizer", false);
        testUserData.put("isAdmin", false);

        users.put("ts123", testUserData2);
    }

    /**
     * USER STORY TEST
     * US 02.05.01 -- As an organizer I want to send a notification to chosen entrants to sign up for events.
     * User opens app and selects Organizer.
     * User's events are displayed.
     * User clicks on one of these events.
     * User clicks on "Send Notification" button.
     * User writes a notification title and body.
     * User chooses the attending users.
     * User clicks submit.
     */
    @Test
    public void testSendNotifToInvitedEntrants() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        String testNotifTitle = "Notification Test";
        String testNotifBody = "Testing Notifications...";

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

            // Organizer clicks on "Piano Lesson"
            onView(withText("Piano Lesson")).perform(click());

            // Event info is displayed
            onView(withId(R.id.eventNameTextView)).check(matches(withText("Piano Lesson")));
            onView(withId(R.id.eventFacilityTextView)).check(matches(withText("Piano Place")));
            onView(withId(R.id.eventDateAndTimeTextView)).check(matches(withText("6:15 PM -- 2025-01-15")));

            // Organizer clicks on "Send Notification"
            onView(withId(R.id.sendNotifButton)).perform(click());

            // Organizer writes a title and body for the notification
            onView(withId(R.id.notifTitleEditText)).perform(typeText(testNotifTitle));
            onView(withId(R.id.notifBodyEditText)).perform(typeText(testNotifBody));

            // Attending option is available, and organizer clicks on it
            onView(withId(R.id.optionInviteeList)).check(matches(withText("Invitees")));
            onView(withId(R.id.optionInviteeList)).perform(click());

            // Click SEND button
            onView(withText("SEND")).perform(click());

            // Check we are back on event page
            onView(withId(R.id.eventNameTextView)).check(matches(withText("Piano Lesson")));
        }
    }

    /**
     * USER STORY TEST
     * US 02.07.01 -- As an organizer I want to send a notification to waitlisted entrants
     * User opens app and selects Organizer.
     * User's events are displayed.
     * User clicks on one of these events.
     * User clicks on "Send Notification" button.
     * User writes a notification title and body.
     * User chooses the waitlisted users.
     * User clicks submit.
     */
    @Test
    public void testSendNotifToWaitlistedEntrants() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        String testNotifTitle = "Notification Test";
        String testNotifBody = "Testing Notifications...";

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

            // Organizer clicks on "Piano Lesson"
            onView(withText("Piano Lesson")).perform(click());

            // Event info is displayed
            onView(withId(R.id.eventNameTextView)).check(matches(withText("Piano Lesson")));
            onView(withId(R.id.eventFacilityTextView)).check(matches(withText("Piano Place")));
            onView(withId(R.id.eventDateAndTimeTextView)).check(matches(withText("6:15 PM -- 2025-01-15")));

            // Organizer clicks on "Send Notification"
            onView(withId(R.id.sendNotifButton)).perform(click());

            // Organizer writes a title and body for the notification
            onView(withId(R.id.notifTitleEditText)).perform(typeText(testNotifTitle));
            onView(withId(R.id.notifBodyEditText)).perform(typeText(testNotifBody));

            // Attending option is available, and organizer clicks on it
            onView(withId(R.id.optionWaitList)).check(matches(withText("Waitlist")));
            onView(withId(R.id.optionWaitList)).perform(click());

            // Click SEND button
            onView(withText("SEND")).perform(click());

            // Check we are back on event page
            onView(withId(R.id.eventNameTextView)).check(matches(withText("Piano Lesson")));
        }
    }

    /**
     * USER STORY TEST
     * US 02.07.02 -- As an organizer I want to send a notification to all selected entrants
     * User opens app and selects Organizer.
     * User's events are displayed.
     * User clicks on one of these events.
     * User clicks on "Send Notification" button.
     * User writes a notification title and body.
     * User chooses the waitlisted users.
     * User clicks submit.
     */
    @Test
    public void testSendNotifToAttendingEntrants() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        String testNotifTitle = "Notification Test";
        String testNotifBody = "Testing Notifications...";

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

            // Organizer clicks on "Piano Lesson"
            onView(withText("Piano Lesson")).perform(click());

            // Event info is displayed
            onView(withId(R.id.eventNameTextView)).check(matches(withText("Piano Lesson")));
            onView(withId(R.id.eventFacilityTextView)).check(matches(withText("Piano Place")));
            onView(withId(R.id.eventDateAndTimeTextView)).check(matches(withText("6:15 PM -- 2025-01-15")));

            // Organizer clicks on "Send Notification"
            onView(withId(R.id.sendNotifButton)).perform(click());

            // Organizer writes a title and body for the notification
            onView(withId(R.id.notifTitleEditText)).perform(typeText(testNotifTitle));
            onView(withId(R.id.notifBodyEditText)).perform(typeText(testNotifBody));

            // Attending option is available, and organizer clicks on it
            onView(withId(R.id.optionAttendeeList)).check(matches(withText("Attending")));
            onView(withId(R.id.optionAttendeeList)).perform(click());

            // Click SEND button
            onView(withText("SEND")).perform(click());

            // Check we are back on event page
            onView(withId(R.id.eventNameTextView)).check(matches(withText("Piano Lesson")));
        }
    }

    /**
     * USER STORY TEST
     * US 02.07.03 -- As an organizer I want to send a notification to all cancelled entrants
     * User opens app and selects Organizer.
     * User's events are displayed.
     * User clicks on one of these events.
     * User clicks on "Send Notification" button.
     * User writes a notification title and body.
     * User chooses the waitlisted users.
     * User clicks submit.
     */
    @Test
    public void testSendNotifToCancelledEntrants() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        String testNotifTitle = "Notification Test";
        String testNotifBody = "Testing Notifications...";

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

            // Organizer clicks on "Piano Lesson"
            onView(withText("Piano Lesson")).perform(click());

            // Event info is displayed
            onView(withId(R.id.eventNameTextView)).check(matches(withText("Piano Lesson")));
            onView(withId(R.id.eventFacilityTextView)).check(matches(withText("Piano Place")));
            onView(withId(R.id.eventDateAndTimeTextView)).check(matches(withText("6:15 PM -- 2025-01-15")));

            // Organizer clicks on "Send Notification"
            onView(withId(R.id.sendNotifButton)).perform(click());

            // Organizer writes a title and body for the notification
            onView(withId(R.id.notifTitleEditText)).perform(typeText(testNotifTitle));
            onView(withId(R.id.notifBodyEditText)).perform(typeText(testNotifBody));

            // Attending option is available, and organizer clicks on it
            onView(withId(R.id.optionCancelledList)).check(matches(withText("Cancelled")));
            onView(withId(R.id.optionCancelledList)).perform(click());

            // Click SEND button
            onView(withText("SEND")).perform(click());

            // Check we are back on event page
            onView(withId(R.id.eventNameTextView)).check(matches(withText("Piano Lesson")));
        }
    }
}
