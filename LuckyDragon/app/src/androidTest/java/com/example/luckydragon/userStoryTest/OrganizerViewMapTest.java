package com.example.luckydragon.userStoryTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
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
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
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

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains tests for US 02.02.02 Organizer
 * See on a map where entrants joined my event waiting list from
 */
public class OrganizerViewMapTest extends MockedDb {
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
        eventData1.put("waitListLimit", new Long(10));
        eventData1.put("attendeeLimit", new Long(1));
        eventData1.put("date", "2025-01-15");
        eventData1.put("hours", new Long(18));
        eventData1.put("minutes", new Long(15));
        eventData1.put("waitList", List.of("ts123", "mf456"));
        eventData1.put("hasGeolocation", true);

        ArrayList<HashMap<String, Object>> waitlistLocations = new ArrayList<>();
        HashMap<String, Object> location1 = new HashMap<>();
        location1.put("latitude", new Double(100));
        location1.put("longitude", new Double(400));
        waitlistLocations.add(location1);
        HashMap<String, Object> location2 = new HashMap<>();
        location2.put("latitude", new Double(250));
        location2.put("longitude", new Double(10));
        waitlistLocations.add(location2);
        eventData1.put("waitListLocations", waitlistLocations);

        events.put("eventId1", eventData1);

        HashMap<String, Object> eventData2 = new HashMap<>();
        eventData2.put("name", "Group Piano Lesson");
        eventData2.put("organizerDeviceId", deviceId);
        eventData2.put("facility", "Piano Place");
        eventData2.put("waitlistLimit", new Long(20));
        eventData2.put("attendeeLimit", new Long(5));
        eventData2.put("date", "2025-01-16");
        eventData2.put("hours", new Long(18));
        eventData2.put("minutes", new Long(15));

        events.put("eventId2", eventData2);
    }
    /**
     * USER STORY TEST
     * US 02.02.02 Organizer - see on a map where entrants joined my event waiting list from
     * User opens app and selects Organizer.
     * User's events are displayed.
     * User clicks on one of these events.
     * User sees the names of the entrants on the waitlist.
     * User sees "See Map" button since geolocation is enabled
     * User clicks "See Map" button
     * Map opens
     * Map shows the correct points for the two users on the waitlist
     */
    @Test
    public void testMapDisplaysCorrectly() {
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

            // Waitlist capacity is displayed correctly
            onView(withId(R.id.waitlistCapacityTextView)).check(matches(withText("Capacity: 10")));

            // Waitlist members are shown correctly
            onView(withText("Tony Sun")).check(matches(isDisplayed()));
            onView(withText("Matthew Fischer")).check(matches(isDisplayed()));

            // See map button is displayed
            onView(withId(R.id.seeMapButton)).check(matches(isDisplayed()));

            // Click see map button
            onView(withId(R.id.seeMapButton)).perform(click());

            // Map is displayed
            onView(withId(R.id.map)).check(matches(isDisplayed()));

            // Check that map is showing correct points
            // This is a hacky solution: the perform() method here will run and we can then check that the map has the right points
            // Not sure if there is a way to create a custom matcher but this works
            onView(withId(R.id.map)).perform(new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return allOf(isDisplayed());
                }

                @Override
                public String getDescription() {
                    return "";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    // Mapview has the correct overlay points
                    MapView map = (MapView) view;
                    ItemizedIconOverlay myOverlay = (ItemizedIconOverlay) map.getOverlays().get(0);
                    assertEquals(Double.compare(myOverlay.getItem(0).getPoint().getLatitude(), 100), 0);
                    assertEquals(Double.compare(myOverlay.getItem(0).getPoint().getLongitude(), 400), 0);
                    assertEquals(Double.compare(myOverlay.getItem(1).getPoint().getLatitude(), 250), 0);
                    assertEquals(Double.compare(myOverlay.getItem(1).getPoint().getLongitude(), 10), 0);
                }
            });
        }
    }
}
