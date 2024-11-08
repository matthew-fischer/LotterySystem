package com.example.luckydragon.userStoryTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.R;
import com.example.luckydragon.SelectRoleActivity;
import com.example.luckydragon.User;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

public class DefaultGeneratedProfilePictureTest {
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
    private Query mockEventQuery;
    @Mock
    private Task<QuerySnapshot> mockEventQueryTask;

    // Mock new user
    private HashMap<String, Object> getMockData() {
        return null;
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

        when(mockEventsCollection.get()).thenReturn(mockEventQueryTask);
        when(mockEventsCollection.document(anyString())).thenReturn(mockEventDocument);
        when(mockEventDocument.get()).thenReturn(mockEventTask);
        // when set is called, we want to do nothing
        when(mockEventDocument.set(anyMap())).thenReturn(mockVoidTask);
        // in the on complete listener, we want to do nothing
        when(mockEventTask.addOnCompleteListener(any())).thenAnswer((invocation) -> {
            return null; // do nothing
        });
        // in Organizer.fetchData(), we don't want to pull events from db
        when(mockEventsCollection
                .whereEqualTo(anyString(), any()))
                .thenReturn(mockEventQuery);
        when(mockEventQuery.get()).thenReturn(mockEventQueryTask);
        when(mockEventQueryTask.addOnCompleteListener(any()))
                .thenAnswer((invocation -> {
                    return null; // do nothing
                }));
        // In EventList.fetchData(), we don't want to pull events from db
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
     * USER STORY TEST
     * New user opens app and selects 'entrant'.
     * User signs up with name and email and does not upload profile picture.
     * User is assigned a randomly generated default profile picture based on their name.
     */
    @Test
    public void defaultProfilePictureTest() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);
        User user = globalApp.getUser();

        // Sign up information
        String name = "John Draco";
        String email = "draco@gmail.com";

        try (final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            // User is not admin, so admin button should not show
            onView(ViewMatchers.withId(R.id.entrantButton)).check(matches(isDisplayed()));
            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
            onView(withId(R.id.adminButton)).check(matches(not(isDisplayed())));

            // User clicks "Entrant"
            onView(withId(R.id.entrantButton)).perform(click());

            // User is on sign up page
            onView(withId(R.id.signupName)).check(matches(isDisplayed()));
            onView(withId(R.id.signupEmail)).check(matches(isDisplayed()));
            onView(withId(R.id.signupSubmit)).check(matches(isDisplayed()));

            // Enter details and click sign up
            onView(withId(R.id.signupName)).perform(typeText(name));
            onView(withId(R.id.signupEmail)).perform(typeText(email));
            onView(withId(R.id.signupSubmit)).perform(click());

            onView(withId(R.id.signupName)).check(doesNotExist());
            onView(withId(R.id.signupEmail)).check(doesNotExist());
            onView(withId(R.id.signupSubmit)).check(doesNotExist());

            onView(withId(R.id.profilePicture)).check(matches(isDisplayed()));

            // Verify that user has no uploaded profile picture
            assertNull(user.getUploadedProfilePicture());

            // Check that user has a default profile picture
            assertNotNull(user.getDefaultProfilePicture());

            // Check that the default profile picture is the one being used
            assertTrue(user.getDefaultProfilePicture().sameAs(user.getProfilePicture()));

            // Check that default profile picture is generated the same for the same name
            Bitmap expected = User.generateProfilePicture(name);
            assertTrue(user.getDefaultProfilePicture().sameAs(expected));
        }
    }
}
