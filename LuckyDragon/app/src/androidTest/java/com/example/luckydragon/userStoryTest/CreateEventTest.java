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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Contains tests for US 02.01.01.
 * Organizer - create a new event and generate a unique promotional QR code that links to the event description and event poster in the app
 */
public class CreateEventTest extends MockedDb {
//    @Mock
//    protected FirebaseFirestore mockFirestore;
//    // User mocks
//    @Mock
//    private CollectionReference mockUsersCollection;
//    @Mock
//    private DocumentReference mockUserDocument;
//    @Mock
//    private DocumentSnapshot mockUserDocumentSnapshot;
//    @Mock
//    private Task<DocumentSnapshot> mockUserTask;
//    @Mock
//    private Task<Void> mockVoidTask;
//    // Event mocks
//    @Mock
//    private CollectionReference mockEventsCollection;
////    @Mock
////    private DocumentReference mockEventDocument;
////    @Mock
////    private Task<DocumentSnapshot> mockEventTask;
//    @Mock
//    private Query mockEventQuery;
//    @Mock
//    private Task<QuerySnapshot> mockEventQueryTask;
//    @Mock
//    private CollectionReference mockMessagesCollection;
//    @Mock
//    private DocumentReference mockMessagesDocument;
//    @Mock
//    private DocumentSnapshot mockMessagesSnapshot;
//    @Mock
//    private QuerySnapshot mockEventQuerySnapshot;

//    private EventListener<QuerySnapshot> eventListener;

//    private ArrayList<Map<String, Object>> events;
//    private ArrayList<QueryDocumentSnapshot> mockEventsIter;

    // need a map of id to data map
//    private HashMap<String, Map<String, Object>> events;
    // need to generate a single documentsnapshot

    // need to generate an iterator of querydocumentshapshot

    // need to update data map

    // Mock organizer with an existing facility
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
        eventData.put("organizerDeviceId", "397bbfd6781b7d4f");
        eventData.put("facility", "UofA");
        eventData.put("waitListLimit", 10L);
        eventData.put("attendeeLimit", 10L);
        eventData.put("hasGeolocation", true);
        eventData.put("date", LocalDate.now().toString());
        eventData.put("hours", 10L);
        eventData.put("minutes", 30L);
        eventData.put("hashedQR", "Fake QR");
        eventData.put("waitList", new ArrayList<>());
        eventData.put("inviteeList", new ArrayList<>());
        eventData.put("attendeeList", new ArrayList<>());
        eventData.put("cancelledList", new ArrayList<>());

        return eventData;
    }

//    private DocumentReference makeEventDocumentSnapshot(String id) {
//        Map<String, Object> data = events.get(id);
//        if (data == null) {
//            // generate a new event
//            events.put(id, new HashMap<>());
//        }
//
//        DocumentReference mockEventDocument = mock(DocumentReference.class);
//        Task<DocumentSnapshot> mockEventTask = mock(Task.class);
//        when(mockEventDocument.getId()).thenReturn(id);
//        when(mockEventDocument.get()).thenReturn(mockEventTask);  // currently do not return the actual data
//        when(mockEventDocument.set(anyMap())).thenAnswer((invocation) -> {
//            Map<String, Object> eventData = invocation.getArgument(0);
//            // cast ints to longs in the data
//            for (Map.Entry<String, Object> entry : eventData.entrySet()) {
//                if (entry.getValue() instanceof Integer) {
//                    eventData.put(entry.getKey(), Long.valueOf((Integer) entry.getValue()));
//                }
//            }
//
//            events.put(id, eventData);
////            Log.d("TONYAAA", "makeEventDocumentSnapshot: ");
//            eventListener.onEvent(mockEventQuerySnapshot, null);
//            return mockVoidTask;
//        });
//        // in the on complete listener, we want to do nothing
//        when(mockEventTask.addOnCompleteListener(any())).thenAnswer((invocation) -> {
//            return null; // do nothing
//        });
//
//        return mockEventDocument;
//
//    }
//    private Iterator<QueryDocumentSnapshot> makeEventsIter() {
//        ArrayList<QueryDocumentSnapshot> list = new ArrayList<>();
//        for (Map.Entry<String, Map<String, Object>> entry: events.entrySet()) {
//            QueryDocumentSnapshot mockEventQueryDocumentSnapshot = mock(QueryDocumentSnapshot.class);
//            when(mockEventQueryDocumentSnapshot.getData()).thenReturn(entry.getValue());
//            when(mockEventQueryDocumentSnapshot.getId()).thenReturn(entry.getKey());
//
//            list.add(mockEventQueryDocumentSnapshot);
//        }
//
//        return list.iterator();
//    }

//    @Before
//    @Override
//    public void setup() {
//        Intents.init();
//        openMocks(this);
//
//        // Set up user mocking
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
//
//        // ****BEGIN EVENTLIST MOCKING**** //
//        // Set up event mocking
//        // We don't want to save anything to the database, so we mock the methods that save an event to the db to do nothing
//        // We also mock getId() to return "mockEventID" instead of going to the database for an id
//        when(mockFirestore.collection("events")).thenReturn(mockEventsCollection);
//
//        when(mockEventsCollection.document()).thenAnswer((invocation) -> {
//            Random random = new Random();
//            return makeEventDocumentSnapshot(String.valueOf(random.nextInt()));
//        });
//        when(mockEventsCollection.document(anyString())).thenAnswer((invocation) -> {
//            String id = invocation.getArgument(0);  // assuming id is valid
//            return makeEventDocumentSnapshot(id);
//        });
//
//        when(mockEventsCollection.addSnapshotListener(any(EventListener.class)))
//                .thenAnswer((invocation) -> {
//                    eventListener = invocation.getArgument(0);
//                    return null;
//                });
//        // when set is called, we want to do nothing
//        events = new HashMap<>();
////        events.put("0", getMockEventData());
////        events.put("1", getMockEventData());
//
////        when(mockEventsCollection.whereEqualTo(anyString(), any())).thenReturn(mockEventQuery);
////        when(mockEventQuery.get())
////                .thenReturn(mockEventQueryTask);
//        when(mockEventsCollection.get())
//                .thenReturn(mockEventQueryTask);
//        when(mockEventQueryTask.addOnCompleteListener(any()))
//                .thenAnswer((invocation -> {
//                    OnCompleteListener<QuerySnapshot> listener = invocation.getArgument(0);
//                    listener.onComplete(mockEventQueryTask);
//                    return mockEventQueryTask;
//                }));
//
//        when(mockEventQueryTask.isSuccessful()).thenReturn(true);
//        when(mockEventQueryTask.getResult()).thenReturn(mockEventQuerySnapshot);
//        when(mockEventQuerySnapshot.iterator()).thenAnswer((invocation) -> makeEventsIter());  // generate a new iterable every call
//        // ****END EVENTLIST MOCKING**** //
//
//        // mock notifications db stuff
//        when(mockFirestore.collection("messages")).thenReturn(mockMessagesCollection);
//        when(mockMessagesCollection.document(any())).thenReturn(mockMessagesDocument);
//        when(mockMessagesDocument.addSnapshotListener(any())).thenAnswer((invocation) -> {
//            return null;
//        });
//        when(mockMessagesDocument.set(anyMap())).thenReturn(mockVoidTask);
//    }

    @After
    public void tearDown() {
        // Reset global app state
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(null);
        globalApp.setUser(null);

        Intents.release();
    }

    /**
     * USER STORY TEST
     * US 02.01.01 Organizer - create a new event and generate a unique promotional QR code that links to the event description and event poster in the app
     * User opens app and selects 'Organizer'.
     * User has an existing facility.
     * The user's facility is displayed correctly.
     * User clicks "Add Event" button.
     * Add Event Dialog is displayed.
     * User enters event details.
     * User clicks create event.
     * Event is created and a QR code is generated.
     */
    @Test
    public void testCreateEventExistingFacility() {
        // Define test event data
        String testEventName = "Piano Lesson";
        String testAttendeeLimit = "5";

        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);

        try (final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            // User is not admin, so admin button should not show
            onView(ViewMatchers.withId(R.id.entrantButton)).check(matches(isDisplayed()));
            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
            onView(withId(R.id.adminButton)).check(matches(not(isDisplayed())));

            // User clicks "Organizer"
            onView(withId(R.id.organizerButton)).perform(click());

            // Profile activity should open and organizer profile should be displayed
            onView(withId(R.id.organizerProfileLayout)).check(matches(isDisplayed()));

            // User clicks "Add Event"
            onView(withId(R.id.addEventButton)).perform(click());

            // Add event dialog is displayed
            onView(withId(R.id.eventNameEditText)).check(matches(isDisplayed()));

            // Organizer enters event details
            // We will use default date and time since they use material components that would be hard to simulate
            onView(withId(R.id.eventNameEditText)).perform(typeText(testEventName));
            onView(withId(R.id.attendeeLimitEditText)).perform(typeText(testAttendeeLimit));

            // Click CREATE
            onView(withText("CREATE")).perform(click());

            // Check that the event shows on the organizer profile
            onData(anything()).inAdapterView(withId(R.id.organizerProfileEventsListview)).atPosition(0).
                    onChildView(withId(R.id.eventRowEventName)).check(matches(withText(testEventName)));
//
//            // Check that the event is in the organizer's list and that the qr code has been generated
//            boolean eventIsPresent = false;
//
//            EventList eventList = globalApp.getEvents();
//            for(Event e : eventList.getEventList()) {
//                if(Objects.equals(e.getName(), testEventName) && (e.getAttendeeSpots() == Integer.parseInt(testAttendeeLimit))) {
//                    eventIsPresent = true;
//                    assertNotNull(e.getQrHash());
//                }
//            }
//            assertTrue(eventIsPresent);
        }
    }
}