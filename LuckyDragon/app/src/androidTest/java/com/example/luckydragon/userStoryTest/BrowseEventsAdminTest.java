package com.example.luckydragon.userStoryTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.ListView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.luckydragon.Event;
import com.example.luckydragon.EventActivity;
import com.example.luckydragon.EventList;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.MockedDb;
import com.example.luckydragon.R;
import com.example.luckydragon.SelectRoleActivity;
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
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BrowseEventsAdminTest extends MockedDb{

    //    @Mock
//    private FirebaseFirestore mockFirestore;
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
//    @Mock
//    private DocumentReference mockEventDocument;
//    @Mock
//    private Task<DocumentSnapshot> mockEventTask;
//    @Mock
//    private Query mockEventQuery;
//    @Mock
//    private Task<QuerySnapshot> mockEventQueryTask;
//    @Mock
//    private EventList eventList;
//    @Mock
//    private Event event;
//    @Mock
//    private Task<QuerySnapshot> mockEventsTask;
//
//    Map<String, Object> testUserData;
//
//    // Mock organizer with an existing facility
//    private HashMap<String, Object> getMockData() {
//        // Define test user
//        HashMap<String, Object> testUserData = new HashMap<>();
//        // Personal info
//        testUserData.put("name", "John Doe");
//        testUserData.put("email", "jdoe@ualberta.ca");
//        testUserData.put("phoneNumber", "780-831-3291");
//        // Roles
//        testUserData.put("isEntrant", true);
//        testUserData.put("isOrganizer", true);
//        testUserData.put("isAdmin", true);
//        // Facility
//        testUserData.put("facility", "The Sports Centre");
//
//        return testUserData;
//    }
//
//    @Before
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
//        when(mockUserDocumentSnapshot.getData()).thenReturn(getMockData());
//
//        // Set up event mocking
//        // We don't want to save anything to the database, so we mock the methods that save an event to the db to do nothing
//        // We also mock getId() to return "mockEventID" instead of going to the database for an id
//        when(mockFirestore.collection("events")).thenReturn(mockEventsCollection);
//        when(mockEventsCollection.document()).thenReturn(mockEventDocument);
//        when(mockEventDocument.getId()).thenReturn("mockEventID");
//
//        when(mockEventsCollection.document(anyString())).thenReturn(mockEventDocument);
//        when(mockEventDocument.get()).thenReturn(mockEventTask);
//        // when set is called, we want to do nothing
//        when(mockEventDocument.set(anyMap())).thenReturn(mockVoidTask);
//        // in the on complete listener, we want to do nothing
//        when(mockEventTask.addOnCompleteListener(any())).thenAnswer((invocation) -> {
//            return null; // do nothing
//        });
//        // in Organizer.fetchData(), we don't want to pull events from db
//        when(mockEventsCollection
//                .whereEqualTo(anyString(), any()))
//                .thenReturn(mockEventQuery);
//        when(mockEventQuery.get()).thenReturn(mockEventQueryTask);
//        when(mockEventQueryTask.addOnCompleteListener(any()))
//                .thenAnswer((invocation -> {
//                    return null; // do nothing
//                }));
//        when(mockEventsCollection.get()).thenReturn(mockEventsTask);
//        when(mockEventsTask.addOnCompleteListener(any())).thenAnswer((innovation) -> {
//            return null; // do nothing
//        });
//
//    }
//
//    @After
//    public void tearDown() {
//        // Reset global app state
//        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
//        globalApp.setDb(null);
//        globalApp.setUser(null);
//
//        Intents.release();
//    }
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
        testUserData.put("isAdmin", true);
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
        eventData.put("waitListLimit", new Long(10));
        eventData.put("attendeeLimit", new Long(10));
        eventData.put("hasGeolocation", true);
        eventData.put("date", LocalDate.now().toString());
        eventData.put("hours", new Long(10));
        eventData.put("minutes", new Long(30));
        eventData.put("hashedQR", "Fake QR");
        eventData.put("waitList", new ArrayList<>());
        eventData.put("inviteeList", new ArrayList<>());
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
    public void testBrowseEvents() {

        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);

        // Launch event activity directly
        try (final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            //eventList = new EventList(mockFirestore);
            onView(withId(R.id.adminButton)).check(matches(isDisplayed()));
            //SystemClock.sleep(2000);
            onView(withId(R.id.adminButton)).perform(click());
            onView(withId(R.id.viewEventsButton)).check(matches(isDisplayed()));
            //onView(withId(R.id.viewProfilesButton)).check(matches(isDisplayed()));
            onView(withId(R.id.viewEventsButton)).perform(click());
            onView(withId(R.id.viewEventsButton)).check(doesNotExist());
            onView(withId(R.id.viewProfilesButton)).check(doesNotExist());
            //SystemClock.sleep(2000);
            //eventList.getEventList().add(new Event("id", "Test Event", "orgID", "Test Facility", 5, 10, "22-09-05", 8, 5));
            //eventList.notifyObservers();
            onView(withId(R.id.adminProfileEventsListview)).check(matches(isDisplayed()));
        }
    }

}
