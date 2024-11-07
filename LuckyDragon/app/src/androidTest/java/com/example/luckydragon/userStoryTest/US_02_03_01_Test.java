package com.example.luckydragon.userStoryTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.openMocks;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.R;
import com.example.luckydragon.SelectRoleActivity;
import com.example.luckydragon.TestHelpers;
import com.example.luckydragon.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

/**
 * Test for User Story 02.03.01.
 * Organizer - Optionally limit the number of entrants who can join my waiting list.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class US_02_03_01_Test {
    @Mock
    private User mockUser;

    private Map<String, Object> testUserData;

    @Mock
    private FirebaseFirestore mockFirestore;
    @Mock
    private CollectionReference mockCollection;
    @Mock
    private DocumentReference mockDocument;
    @Mock
    private DocumentSnapshot mockDocumentSnapshot;
    @Mock
    private Task<DocumentSnapshot> mockTask;
    @Mock
    private QuerySnapshot mockQuerySnapshot;
    @InjectMocks
    GlobalApp globalApp;

    @BeforeAll
    public void defineTestUser() {

    }

    @Before
    public void setup() {
        // Define test user
        testUserData = new HashMap<>();
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

        Intents.init();

        mockFirestore = Mockito.mock(FirebaseFirestore.class, Mockito.RETURNS_DEEP_STUBS);
        mockTask = Mockito.mock(Task.class);

        when(mockFirestore.collection("users")
                .document(anyString())
                .get()
                .addOnSuccessListener(any()))
                .thenAnswer((invocation) -> {
                    final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();

                    User user = globalApp.getUser();
                    user.buildUserFromMap(testUserData);
                    user.setIsLoaded(true);
                    user.notifyObservers();
                    return null;
                });
        when(mockFirestore.collection("events")
                .whereEqualTo(anyString(), anyString())
                .get()
                .addOnCompleteListener(any()))
                .thenAnswer((invocation -> {
                    return 1;
                }));
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    // TODO write tests
    /**
     * USER STORY TEST
     * User opens app and selects 'Organizer'.
     * User has an existing facility.
     * The user's facility is displayed correctly.
     * User clicks "Add Event" button.
     * Add Event Dialog is displayed.
     * User enters event details, including a waiting list limit number.
     * User clicks create event.
     * Event is created and a QR code is generated.
     */
    @Test
    public void testOrganizerCreateEventWithWaitlistLimit() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);

        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);
        try (final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            // Click organizer button
            onView(withId(R.id.organizerButton)).perform(click());
            // Check that facility is shown
            onView(withId(R.id.facilityTextView)).check(matches(withText("The Sports Centre")));
            // Click add event button
            onView(withId(R.id.addEventButton)).perform(click());
            // Add event dialog is displayed
            onView(withId(R.id.eventNameEditText)).check(matches(isDisplayed()));
        }
    }

    /**
     * USER STORY TEST
     * User opens app and selects 'Organizer'.
     * User has an existing facility.
     * The user's facility is displayed correctly.
     * User clicks "Add Event" button.
     * Add Event Dialog is displayed.
     * User enters event details without entering a waiting list number.
     * User clicks create event.
     * Event is created and a QR code is generated.
     */
    @Test
    public void testOrganizerCreateEventWithoutWaitlistLimit() {
        // TODO write test
    }

    private HashMap<String, Object> getMockData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("isEntrant", true);
        map.put("isOrganizer", true);
        map.put("isAdmin", false);

        map.put("facility", "The Event Centre");

        map.put("name", "John Doe");
        map.put("email", "email@address.co");
        map.put("phoneNumber", "1231231234");
        map.put("notifications", true);
        return map;
    }
}
