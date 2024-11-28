package com.example.luckydragon.userStoryTest;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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

import com.example.luckydragon.Activities.SelectRoleActivity;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains tests for US 02.06.01.
 * As an Organizer I want to view a list of all chosen entrants who are invited to apply.
 */
public class OrganizerViewInviteeListTest {
    @Mock
    private FirebaseFirestore mockFirestore;
    // User mocks
    @Mock
    private CollectionReference mockUsersCollection;
    @Mock
    private DocumentReference mockUserDocument;
    @Mock
    private DocumentSnapshot mockUserDocumentSnapshot;
    @Mock
    private Task<DocumentSnapshot> mockUserTask;
    @Mock
    private Task<Void> mockVoidTask;

    // Inviteelist user mocks
    @Mock
    private DocumentReference mockInviteelistEntrant1Document;
    @Mock
    private Task<DocumentSnapshot> mockInviteelistEntrant1Task;
    @Mock
    private DocumentSnapshot mockInviteelistEntrant1DocumentSnapshot;
    @Mock
    private DocumentReference mockInviteelistEntrant2Document;
    @Mock
    private Task<DocumentSnapshot> mockInviteelistEntrant2Task;
    @Mock
    private DocumentSnapshot mockInviteelistEntrant2DocumentSnapshot;

    // Event mocks
    @Mock
    private CollectionReference mockEventsCollection;
    @Mock
    private DocumentReference mockEventDocument;
    @Mock
    private Query mockEventQuery;
    @Mock
    private Task<QuerySnapshot> mockEventQueryTask;
    @Mock
    private QuerySnapshot mockEventQuerySnapshot;
    @Mock
    private List<DocumentSnapshot> mockEventDocumentSnapshotList;
    @Mock
    private QueryDocumentSnapshot mockEventQueryDocumentSnapshot1;
    @Mock
    private QueryDocumentSnapshot mockEventQueryDocumentSnapshot2;

    @Mock
    private DocumentReference mockEventDocument1;
    @Mock
    private DocumentReference mockEventDocument2;
    @Mock
    private Task<DocumentSnapshot> mockEventTask1;
    @Mock
    private Task<DocumentSnapshot> mockEventTask2;
    @Mock
    private DocumentSnapshot mockEventDocumentSnapshot1;
    @Mock
    private DocumentSnapshot mockEventDocumentSnapshot2;

    // Event Data
    private List<Map<String, Object>> eventData = new ArrayList<>();

    private HashMap<String, Object> getMockUserData() {
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

    private HashMap<String, Object> getMockInviteelistUser1() {
        // Define test user
        HashMap<String, Object> testUserData = new HashMap<>();
        // Personal info
        testUserData.put("name", "Tony Sun");
        testUserData.put("email", "tonysun@ualberta.ca");
        testUserData.put("phoneNumber", "780-831-3291");
        // Roles
        testUserData.put("isEntrant", true);
        testUserData.put("isOrganizer", false);
        testUserData.put("isAdmin", false);

        return testUserData;
    }

    private HashMap<String, Object> getMockInviteelistUser2() {
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

        return testUserData;
    }

    @Before
    public void setup() {
        Intents.init();
        openMocks(this);

        // Initialize event data
        HashMap<String, Object> eventData1 = new HashMap<>();
        eventData1.put("name", "Piano Lesson");
        eventData1.put("organizerDeviceId", "abcd1234");
        eventData1.put("facility", "Piano Place");
        eventData1.put("waitlistLimit", new Long(10));
        eventData1.put("attendeeLimit", new Long(1));
        eventData1.put("date", "2025-01-15");
        eventData1.put("hours", new Long(18));
        eventData1.put("minutes", new Long(15));
        eventData1.put("inviteeList", List.of("ts123", "mf456"));
        eventData.add(eventData1);

        HashMap<String, Object> eventData2 = new HashMap<>();
        eventData2.put("name", "Group Piano Lesson");
        eventData2.put("organizerDeviceId", "abcd1234");
        eventData2.put("facility", "Piano Place");
        eventData2.put("waitlistLimit", new Long(20));
        eventData2.put("attendeeLimit", new Long(5));
        eventData2.put("date", "2025-01-16");
        eventData2.put("hours", new Long(18));
        eventData2.put("minutes", new Long(15));
        eventData2.put("inviteeList", List.of("ts123"));
        eventData.add(eventData2);

        // Set up user mocking for main user
        when(mockFirestore.collection("users")).thenReturn(mockUsersCollection);
        when(mockUsersCollection.document(anyString())).thenReturn(mockUserDocument);
        when(mockUserDocument.get()).thenReturn(mockUserTask);
        when(mockUserDocument.set(any(Map.class))).thenReturn(mockVoidTask);
        when(mockUserTask.addOnFailureListener(any(OnFailureListener.class))).thenReturn(mockUserTask);
        doAnswer(invocation -> {
            OnSuccessListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onSuccess(mockUserDocumentSnapshot);
            return mockUserTask;
        }).when(mockUserTask).addOnSuccessListener(any(OnSuccessListener.class));
        when(mockUserDocumentSnapshot.getData()).thenReturn(getMockUserData());

        // Set up mockito mocking for inviteelist user 1
        when(mockUsersCollection.document("ts123")).thenReturn(mockInviteelistEntrant1Document);
        when(mockInviteelistEntrant1Document.get()).thenReturn(mockInviteelistEntrant1Task);
        when(mockInviteelistEntrant1Document.set(any(Map.class))).thenReturn(mockVoidTask);
        when(mockInviteelistEntrant1Task.addOnFailureListener(any(OnFailureListener.class))).thenReturn(mockInviteelistEntrant1Task);
        doAnswer(invocation -> {
            OnSuccessListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onSuccess(mockInviteelistEntrant1DocumentSnapshot);
            return mockInviteelistEntrant1Task;
        }).when(mockInviteelistEntrant1Task).addOnSuccessListener(any(OnSuccessListener.class));
        when(mockInviteelistEntrant1DocumentSnapshot.getData()).thenReturn(getMockInviteelistUser1());

        // Set up mockito mocking for inviteelist user 2
        when(mockUsersCollection.document("mf456")).thenReturn(mockInviteelistEntrant2Document);
        when(mockInviteelistEntrant2Document.get()).thenReturn(mockInviteelistEntrant2Task);
        when(mockInviteelistEntrant2Document.set(any(Map.class))).thenReturn(mockVoidTask);
        when(mockInviteelistEntrant2Task.addOnFailureListener(any(OnFailureListener.class))).thenReturn(mockInviteelistEntrant2Task);
        doAnswer(invocation -> {
            OnSuccessListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onSuccess(mockInviteelistEntrant2DocumentSnapshot);
            return mockInviteelistEntrant2Task;
        }).when(mockInviteelistEntrant2Task).addOnSuccessListener(any(OnSuccessListener.class));
        when(mockInviteelistEntrant2DocumentSnapshot.getData()).thenReturn(getMockInviteelistUser2());

        // Set up event mocking
        when(mockFirestore.collection("events")).thenReturn(mockEventsCollection);
        when(mockEventsCollection.whereEqualTo(anyString(), anyString())).thenReturn(mockEventQuery);
        when(mockEventQuery.get()).thenReturn(mockEventQueryTask);
        when(mockEventQueryTask.isSuccessful()).thenReturn(true);
        when(mockEventQueryTask.getResult()).thenReturn(mockEventQuerySnapshot);
        when(mockEventQuerySnapshot.size()).thenReturn(2);
        when(mockEventQuerySnapshot.getDocuments()).thenReturn(mockEventDocumentSnapshotList);
        when(mockEventDocumentSnapshotList.get(anyInt())).thenAnswer((invocation) -> {
            int index = invocation.getArgument(0);
            if(index == 0) return mockEventQueryDocumentSnapshot1;
            else return mockEventQueryDocumentSnapshot2;
        });
        when(mockEventQueryDocumentSnapshot1.getId()).thenReturn(String.valueOf(0));
        when(mockEventQueryDocumentSnapshot2.getId()).thenReturn(String.valueOf(1));
        when(mockEventQueryDocumentSnapshot1.getData()).thenReturn(eventData.get(0));
        when(mockEventQueryDocumentSnapshot2.getData()).thenReturn(eventData.get(1));
        when(mockEventQueryTask.addOnCompleteListener(any()))
                .thenAnswer((invocation) -> {
                    OnCompleteListener<QuerySnapshot> listener = invocation.getArgument(0);
                    listener.onComplete(mockEventQueryTask);

                    return null;
                });
        // We don't want to save anything to the database, so we mock the methods that save an event to the db to do nothing
        // We also mock getId() to return "mockEventID" instead of going to the database for an id
        when(mockFirestore.collection("events")).thenReturn(mockEventsCollection);
        when(mockEventsCollection.document()).thenReturn(mockEventDocument);
        when(mockEventDocument.getId()).thenReturn("mockEventID");

        when(mockEventsCollection.document("0")).thenReturn(mockEventDocument1);
        when(mockEventDocument1.get()).thenReturn(mockEventTask1);
        when(mockEventDocument1.set(anyMap())).thenReturn(mockVoidTask);
        when(mockEventTask1.addOnCompleteListener(any())).thenAnswer((invocation) -> {
            OnCompleteListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onComplete(mockEventTask1);
            return null;
        });
        when(mockEventTask1.isSuccessful()).thenReturn(true);
        when(mockEventTask1.getResult()).thenReturn(mockEventDocumentSnapshot1);
        when(mockEventDocumentSnapshot1.exists()).thenReturn(true);
        when(mockEventDocumentSnapshot1.getData()).thenReturn(eventData.get(0));

        when(mockEventsCollection.document("1")).thenReturn(mockEventDocument2);
        when(mockEventDocument2.get()).thenReturn(mockEventTask2);
        when(mockEventDocument2.set(anyMap())).thenReturn(mockVoidTask);
        when(mockEventTask2.addOnCompleteListener(any())).thenAnswer((invocation) -> {
            OnCompleteListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onComplete(mockEventTask2);
            return null;
        });
        when(mockEventTask2.isSuccessful()).thenReturn(true);
        when(mockEventTask2.getResult()).thenReturn(mockEventDocumentSnapshot2);
        when(mockEventDocumentSnapshot2.exists()).thenReturn(true);
        when(mockEventDocumentSnapshot2.getData()).thenReturn(eventData.get(1));
    }

    @After
    public void tearDown() {
        // Reset global app state
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.resetState();

        Intents.release();
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
            onView(withText("Tony Sun")).check(matches(isDisplayed()));
            onView(withText("Matthew Fischer")).check(matches(isDisplayed()));
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
            onView(withText("Tony Sun")).check(matches(isDisplayed()));
            onView(withText("Matthew Fischer")).check(doesNotExist());
        }
    }
}
