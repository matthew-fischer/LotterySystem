package com.example.luckydragon.userStoryTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
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
import com.example.luckydragon.MockedEventList;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.EventList;
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
 * Contains tests for US 02.02.01.
 * As an organizer I want to view the list of entrants who joined my event waiting list.
 */
public class SampleInviteesTest extends MockedEventList {
    // Waitlist user mocks
    @Mock
    private DocumentReference mockWaitlistEntrant1Document;
    @Mock
    private Task<DocumentSnapshot> mockWaitlistEntrant1Task;
    @Mock
    private DocumentSnapshot mockWaitlistEntrant1DocumentSnapshot;
    @Mock
    private DocumentReference mockWaitlistEntrant2Document;
    @Mock
    private Task<DocumentSnapshot> mockWaitlistEntrant2Task;
    @Mock
    private DocumentSnapshot mockWaitlistEntrant2DocumentSnapshot;
    // Notification mocks
    @Mock
    private CollectionReference mockMessagesCollection;
    @Mock
    private DocumentReference mockMessagesDocument;

    // Event Data
    private List<Map<String, Object>> eventData = new ArrayList<>();
    @Override
    protected String getUserID() {
        return "abcd1234";
    }
    @Override
    protected Map<String, Object> getMockData() {
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

    private HashMap<String, Object> getMockWaitlistUser1() {
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

    private HashMap<String, Object> getMockWaitlistUser2() {
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

    public SampleInviteesTest() {
        // Initialize event data
        HashMap<String, Object> eventData1 = new HashMap<>();
        eventData1.put("name", "Piano Lesson");
        eventData1.put("organizerDeviceId", "abcd1234");
        eventData1.put("facility", "Piano Place");
        eventData1.put("waitListLimit", 10L);
        eventData1.put("attendeeLimit", 2L);
        eventData1.put("date", "2025-01-15");
        eventData1.put("hours", 18L);
        eventData1.put("minutes", 15L);
        eventData1.put("lotteryDate", "2024-09-01");
        eventData1.put("lotteryHours", 8L);
        eventData1.put("lotteryMinutes", 0L);
        eventData1.put("waitList", List.of("ts123", "mf456"));
        eventData1.put("createdTimeMillis", 1731294000000L); // event created Nov 10 2024 8:00:00 PM
        eventData.add(eventData1);

        HashMap<String, Object> eventData2 = new HashMap<>();
        eventData2.put("name", "Group Piano Lesson");
        eventData2.put("organizerDeviceId", "abcd1234");
        eventData2.put("facility", "Piano Place");
        eventData2.put("waitListLimit", new Long(20));
        eventData2.put("attendeeLimit", new Long(5));
        eventData2.put("date", "2025-01-16");
        eventData2.put("hours", new Long(18));
        eventData2.put("minutes", new Long(15));
        eventData.add(eventData2);
        setEventList(new ArrayList<>(List.of(eventData1, eventData2)), new ArrayList<>(List.of(eventData1, eventData2)));
    }

//    @Before
//    public void setup() {
//        Intents.init();
//        openMocks(this);

//
//        // Set up user mocking for main user
//        when(mockFirestore.collection("users")).thenReturn(mockUsersCollection);
//        when(mockUsersCollection.document(anyString())).thenReturn(mockUserDocument);
//        when(mockUserDocument.get()).thenReturn(mockUserTask);
//        when(mockUserDocument.set(any(Map.class))).thenReturn(mockVoidTask);
//        when(mockUserTask.addOnFailureListener(any(OnFailureListener.class))).thenReturn(mockUserTask);
//        doAnswer(invocation -> {
//            OnSuccessListener<DocumentSnapshot> listener = invocation.getArgument(0);
//            listener.onSuccess(mockUserDocumentSnapshot);
//            return mockUserTask;
//        }).when(mockUserTask).addOnSuccessListener(any(OnSuccessListener.class));
//        when(mockUserDocumentSnapshot.getData()).thenReturn(getMockUserData());

        // Set up mockito mocking for waitlist user 1


//        // Set up event mocking
//        when(mockFirestore.collection("events")).thenReturn(mockEventsCollection);
//        when(mockEventsCollection.whereEqualTo(anyString(), anyString())).thenReturn(mockEventQuery);
//        when(mockEventQuery.get()).thenReturn(mockEventQueryTask);
//        when(mockEventQueryTask.isSuccessful()).thenReturn(true);
//        when(mockEventQueryTask.getResult()).thenReturn(mockEventQuerySnapshot);
//        when(mockEventQuerySnapshot.size()).thenReturn(2);
//        when(mockEventQuerySnapshot.getDocuments()).thenReturn(mockEventDocumentSnapshotList);
//        when(mockEventDocumentSnapshotList.get(anyInt())).thenAnswer((invocation) -> {
//            int index = invocation.getArgument(0);
//            if(index == 0) return mockEventQueryDocumentSnapshot1;
//            else return mockEventQueryDocumentSnapshot2;
//        });
//        when(mockEventQueryDocumentSnapshot1.getId()).thenReturn(String.valueOf(0));
//        when(mockEventQueryDocumentSnapshot2.getId()).thenReturn(String.valueOf(1));
//        when(mockEventQueryDocumentSnapshot1.getData()).thenReturn(eventData.get(0));
//        when(mockEventQueryDocumentSnapshot2.getData()).thenReturn(eventData.get(1));
//        when(mockEventQueryTask.addOnCompleteListener(any()))
//                .thenAnswer((invocation) -> {
//                    OnCompleteListener<QuerySnapshot> listener = invocation.getArgument(0);
//                    listener.onComplete(mockEventQueryTask);
//
//                    return null;
//                });
        // We don't want to save anything to the database, so we mock the methods that save an event to the db to do nothing
        // We also mock getId() to return "mockEventID" instead of going to the database for an id
//        when(mockFirestore.collection("events")).thenReturn(mockEventsCollection);
//        when(mockEventsCollection.document()).thenReturn(mockEventDocument);
//        when(mockEventDocument.getId()).thenReturn("mockEventID");
//
//        when(mockEventsCollection.document("0")).thenReturn(mockEventDocument1);
//        when(mockEventDocument1.get()).thenReturn(mockEventTask1);
//        when(mockEventDocument1.set(anyMap())).thenReturn(mockVoidTask);
//        when(mockEventTask1.addOnCompleteListener(any())).thenAnswer((invocation) -> {
//            OnCompleteListener<DocumentSnapshot> listener = invocation.getArgument(0);
//            listener.onComplete(mockEventTask1);
//            return null;
//        });
//        when(mockEventTask1.isSuccessful()).thenReturn(true);
//        when(mockEventTask1.getResult()).thenReturn(mockEventDocumentSnapshot1);
//        when(mockEventDocumentSnapshot1.exists()).thenReturn(true);
//        when(mockEventDocumentSnapshot1.getData()).thenReturn(eventData.get(0));
//
//        when(mockEventsCollection.document("1")).thenReturn(mockEventDocument2);
//        when(mockEventDocument2.get()).thenReturn(mockEventTask2);
//        when(mockEventDocument2.set(anyMap())).thenReturn(mockVoidTask);
//        when(mockEventTask2.addOnCompleteListener(any())).thenAnswer((invocation) -> {
//            OnCompleteListener<DocumentSnapshot> listener = invocation.getArgument(0);
//            listener.onComplete(mockEventTask2);
//            return null;
//        });
//        when(mockEventTask2.isSuccessful()).thenReturn(true);
//        when(mockEventTask2.getResult()).thenReturn(mockEventDocumentSnapshot2);
//        when(mockEventDocumentSnapshot2.exists()).thenReturn(true);
//        when(mockEventDocumentSnapshot2.getData()).thenReturn(eventData.get(1));




//    }

    @Override
    protected void extraSetup() {
        when(mockUsersCollection.document("ts123")).thenReturn(mockWaitlistEntrant1Document);
        when(mockWaitlistEntrant1Document.get()).thenReturn(mockWaitlistEntrant1Task);
        when(mockWaitlistEntrant1Document.set(any(Map.class))).thenReturn(mockVoidTask);
        when(mockWaitlistEntrant1Task.addOnFailureListener(any(OnFailureListener.class))).thenReturn(mockWaitlistEntrant1Task);
        doAnswer(invocation -> {
            OnSuccessListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onSuccess(mockWaitlistEntrant1DocumentSnapshot);
            return mockWaitlistEntrant1Task;
        }).when(mockWaitlistEntrant1Task).addOnSuccessListener(any(OnSuccessListener.class));
        when(mockWaitlistEntrant1DocumentSnapshot.getData()).thenReturn(getMockWaitlistUser1());

        // Set up mockito mocking for waitlist user 2
        when(mockUsersCollection.document("mf456")).thenReturn(mockWaitlistEntrant2Document);
        when(mockWaitlistEntrant2Document.get()).thenReturn(mockWaitlistEntrant2Task);
        when(mockWaitlistEntrant2Document.set(any(Map.class))).thenReturn(mockVoidTask);
        when(mockWaitlistEntrant2Task.addOnFailureListener(any(OnFailureListener.class))).thenReturn(mockWaitlistEntrant2Task);
        doAnswer(invocation -> {
            OnSuccessListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onSuccess(mockWaitlistEntrant2DocumentSnapshot);
            return mockWaitlistEntrant2Task;
        }).when(mockWaitlistEntrant2Task).addOnSuccessListener(any(OnSuccessListener.class));
        when(mockWaitlistEntrant2DocumentSnapshot.getData()).thenReturn(getMockWaitlistUser2());

        // mock notifications db stuff
        when(mockFirestore.collection("messages")).thenReturn(mockMessagesCollection);
        when(mockMessagesCollection.document(any())).thenReturn(mockMessagesDocument);
        when(mockMessagesDocument.addSnapshotListener(any())).thenAnswer((invocation) -> {
            return null;
        });
        when(mockMessagesDocument.set(anyMap())).thenReturn(mockVoidTask);
        // add userlist mocking
        Task<QuerySnapshot> mockUserQuerySnapshotTask = mock(Task.class);
        QuerySnapshot mockUserQuerySnapshot = mock(QuerySnapshot.class);
        when(mockUsersCollection.addSnapshotListener(any())).thenAnswer(invocation -> {
            EventListener listener = invocation.getArgument(0);
            listener.onEvent(mockUserQuerySnapshot, null);
            return null;
        });
        when(mockUserQuerySnapshot.size()).thenReturn(2);
        List<DocumentSnapshot> mockDocumentSnapshots = mock(List.class);
        when(mockUserQuerySnapshot.getDocuments()).thenReturn(mockDocumentSnapshots);
        QueryDocumentSnapshot userDocumentSnapshot1 = Mockito.mock(QueryDocumentSnapshot.class);
        when(mockDocumentSnapshots.get(0)).thenReturn(userDocumentSnapshot1);
        QueryDocumentSnapshot userDocumentSnapshot2 = Mockito.mock(QueryDocumentSnapshot.class);
        when(mockDocumentSnapshots.get(1)).thenReturn(userDocumentSnapshot2);
        when(userDocumentSnapshot1.getData()).thenReturn(getMockWaitlistUser1());
        when(userDocumentSnapshot2.getData()).thenReturn(getMockWaitlistUser2());
        when(userDocumentSnapshot1.getId()).thenReturn("ts123");
        when(userDocumentSnapshot2.getId()).thenReturn("mf456");
        // mock UserList fetch data to do nothing
        Task<QuerySnapshot> mockQuerySnapshotVoidTask = Mockito.mock(Task.class);
        when(mockUsersCollection.get()).thenReturn(mockQuerySnapshotVoidTask);
    }


    /**
     * USER STORY TEST
     * US 02.02.01 -- As an organizer I want to view the list of entrants who joined my event waiting list.
     * User opens app and selects Organizer.
     * User's events are displayed.
     * User clicks on one of these events.
     * User sees the names of the entrants on the waitlist.
     */
    @Test
    public void testSampleInvitees() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);
        globalApp.setDeviceId(getUserID());

        EventList eventList = globalApp.getEvents();
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
            
            // Check that users are on the waitlist for "Piano Lesson" event
            String testEventName = "Piano Lesson";
            Event event = null;
            for (Event e: eventList.getEventList()) {
                if (e.getName().equals(testEventName)) {
                    event = e;
                }
            }

            assertTrue(event.getWaitList().size() == 2);
            assertTrue(event.getWaitList().contains("ts123"));
            assertTrue(event.getWaitList().contains("mf456"));

            // Organizer clicks on "Piano Lesson"
            onView(withText("Piano Lesson")).perform(click());

            // Event info is displayed
            onView(withId(R.id.eventNameTextView)).check(matches(withText("Piano Lesson")));
            onView(withId(R.id.eventFacilityTextView)).check(matches(withText("Piano Place")));
            onView(withId(R.id.eventDateAndTimeTextView)).check(matches(withText("6:15 PM -- 2025-01-15")));

            // Waitlist capacity is displayed correctly
            onView(withId(R.id.waitlistCapacityTextView)).check(matches(withText("Capacity: 10")));

            // Check that users are now in inviteeList
//            assertTrue(event.getInviteeList().contains("ts123"));
//            assertTrue(event.getInviteeList().contains("mf456"));
//            assertTrue(event.getWaitList().isEmpty());
//            assertEquals(event.getInviteeList().size(), 2);

            // TODO could check that invited users are displayed -- invitee display isn't implemented yet
            //onView(withText("Tony Sun")).check(matches(isDisplayed()));
            //onView(withText("Matthew Fischer")).check(matches(isDisplayed()));
        }
    }
}
