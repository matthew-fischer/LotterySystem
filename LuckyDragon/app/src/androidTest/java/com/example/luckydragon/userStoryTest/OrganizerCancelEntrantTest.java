package com.example.luckydragon.userStoryTest;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains tests for US 02.06.04.
 * As an organizer I want to cancel entrants that declined signing up for the event.
 */
public class OrganizerCancelEntrantTest extends MockedDb {
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
        eventData1.put("lotteryDate", "2025-09-01");
        eventData1.put("lotteryHours", 8L);
        eventData1.put("lotteryMinutes", 0L);
        eventData1.put("waitList", List.of("ts123"));
        eventData1.put("inviteeList", List.of("mf456"));
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
     * US 02.06.04 -- As an organizer I want to cancel entrants that declined signing up for the event.
     * User opens app and selects Organizer.
     * User's events are displayed.
     * User clicks on one of these events.
     * User sees the names of the entrants on the waitlist.
     * Cancel all entrants from waitlist.
     * User sees the names of the entrants on the cancelledlist.
     */
    @Test
    public void testOrganizerCancelsEntrants() {
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

            // waitlist member is shown correctly
            onData(anything()).inAdapterView(withId(R.id.eventWaitlistListView))
                    .atPosition(0).onChildView(withId(R.id.entrantNameTextView)).check(matches(withText("Tony Sun")));
            // inviteelist member is shown correctly
            onData(anything()).inAdapterView(withId(R.id.eventInvitelistListView))
                    .atPosition(0).onChildView(withId(R.id.entrantNameTextView)).check(matches(withText("Matthew Fischer")));

            // cancel waitlist member
            onData(anything()).inAdapterView(withId(R.id.eventWaitlistListView))
                    .atPosition(0).onChildView(withId(R.id.cancelIcon)).perform(click());

            // waitlist member is now a cancelledlist member
            onData(anything()).inAdapterView(withId(R.id.eventCancelledlistListView))
                    .atPosition(0).onChildView(withId(R.id.entrantNameTextView)).check(matches(withText("Tony Sun")));
        }
    }
}
