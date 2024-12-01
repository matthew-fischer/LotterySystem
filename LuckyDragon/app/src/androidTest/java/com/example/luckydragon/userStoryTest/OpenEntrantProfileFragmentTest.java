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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Contains tests for US 01.01.01.
 * As an entrant, I want to join the waiting list for a specific event
 */
public class OpenEntrantProfileFragmentTest {
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
    // Event mocks
    @Mock
    private CollectionReference mockEventsCollection;
    @Mock
    private DocumentReference mockEventDocument;
    @Mock
    private Task<DocumentSnapshot> mockEventTask;
    @Mock
    private Task<QuerySnapshot> mockEventQuerySnapshotTask;
    @Mock
    private Query mockEventQuery;
    @Mock
    private Task<QuerySnapshot> mockEventQueryTask;
    @Mock
    private CollectionReference mockMessagesCollection;
    @Mock
    private DocumentReference mockMessagesDocument;

    Map<String, Object> testUserData;

    // Mock organizer with an existing facility
    private HashMap<String, Object> getMockData() {
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

    @Before
    public void setup() {
        Intents.init();
        openMocks(this);

        // Set up user mocking
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
        when(mockUserDocumentSnapshot.getData()).thenReturn(getMockData());

        // Set up event mocking
        // We don't want to save anything to the database, so we mock the methods that save an event to the db to do nothing
        // We also mock getId() to return "mockEventID" instead of going to the database for an id
        when(mockFirestore.collection("events")).thenReturn(mockEventsCollection);
        when(mockEventsCollection.document()).thenReturn(mockEventDocument);
        when(mockEventDocument.getId()).thenReturn("mockEventID");

        when(mockEventsCollection.document(anyString())).thenReturn(mockEventDocument);
        when(mockEventDocument.get()).thenReturn(mockEventTask);
        // when set is called, we want to do nothing
        when(mockEventDocument.set(anyMap())).thenReturn(mockVoidTask);
        // in the on complete listener, we want to do nothing
        when(mockEventTask.addOnCompleteListener(any())).thenAnswer((invocation) -> {
            return null; // do nothing
        });
        when(mockEventsCollection.get()).thenReturn(mockEventQuerySnapshotTask);
        when(mockEventQuerySnapshotTask.addOnCompleteListener(any())).thenAnswer((invocation -> {
            return null; // do nothing
        }));
        // in Organizer.fetchData(), we don't want to pull events from db
        when(mockEventsCollection
                .whereEqualTo(anyString(), any()))
                .thenReturn(mockEventQuery);
        when(mockEventQuery.get()).thenReturn(mockEventQueryTask);
        when(mockEventQueryTask.addOnCompleteListener(any()))
                .thenAnswer((invocation -> {
                    return null; // do nothing
                }));

        // mock notifications db stuff
        when(mockFirestore.collection("messages")).thenReturn(mockMessagesCollection);
        when(mockMessagesCollection.document(any())).thenReturn(mockMessagesDocument);
        when(mockMessagesDocument.addSnapshotListener(any())).thenAnswer((invocation) -> {
            return null;
        });
        when(mockMessagesDocument.set(anyMap())).thenReturn(mockVoidTask);
    }

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
     * US 01.01.01 As an entrant, I want to join the waiting list for a specific event
     * This is the first part of this user story. We go to entrants profile fragment.
     * Since we cannot test QR scanner, the second have of this user story is in JoinWaitlistTest.
     *
     * Open Entrant Profile Fragment
     * User opens app and selects 'Entrant'.
     */
    @Test
    public void testOpenEntrantProfileFragment() {

        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);

        try (final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            // User is not admin, so admin button should not show
            onView(ViewMatchers.withId(R.id.entrantButton)).check(matches(isDisplayed()));
            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
            onView(withId(R.id.adminButton)).check(matches(not(isDisplayed())));

            // User clicks "Entrant"
            onView(withId(R.id.entrantButton)).perform(click());

            // Profile activity should open and entrant profile should be displayed
            onView(withId(R.id.EntrantProfileLayout)).check(matches(isDisplayed()));

        }
    }
}