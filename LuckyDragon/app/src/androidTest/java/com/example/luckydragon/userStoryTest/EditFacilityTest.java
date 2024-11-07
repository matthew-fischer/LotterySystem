package com.example.luckydragon.userStoryTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Space;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.R;
import com.example.luckydragon.SelectRoleActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

public class EditFacilityTest {
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
    private Task<Void> mockVoidTask;

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
        when(mockFirestore.collection("users")).thenReturn(mockCollection);
        when(mockCollection.document(anyString())).thenReturn(mockDocument);

//        when(mockCollection.get()).thenReturn(mocktask);
        when(mockDocument.get()).thenReturn(mockTask);
        when(mockDocument.set(any(Map.class))).thenReturn(mockVoidTask);
        when(mockTask.addOnFailureListener(any(OnFailureListener.class))).thenReturn(mockTask);
        doAnswer(invocation -> {
            OnSuccessListener<DocumentSnapshot> listener =  invocation.getArgument(0);
            listener.onSuccess(mockDocumentSnapshot);
            return mockTask;
        }).when(mockTask).addOnSuccessListener(any(OnSuccessListener.class));
        when(mockDocumentSnapshot.getData()).thenReturn(getMockData());
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    /**
     * USER STORY TEST
     * User opens app and selects "Organizer".
     * User already has a facility.
     * The user's existing facility is displayed.
     * User clicks edit button and EditFacilityDialogFragment is started.
     * The text input field is the user's existing facility.
     * User enters a new facility name and selects confirm.
     * EditFacilityDialogFragment closes.
     * The change is reflected on the organizer's profile.
     */
    @Test
    public void testSetFacilityNotFirstTime() {
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

            // Before clicking the facility edit button, hide the space because it causes weird espresso behaviour
            onView(withId(R.id.facilitySpace)).perform(setSpaceVisibility(false));

            // User clicks facility edit button
            onView(withId(R.id.facilityEditButton)).perform(click());

            // Edit Facility dialog should be displayed now
            onView(withId(R.id.editFacilityDialog)).check(matches(isDisplayed()));

            // The facility edittext is the previous facility
            onView(withId(R.id.edit_facility_FacilityEditText)).check(matches(withText("The Sports Centre")));

            // Set edittext to test facility
            onView(withId(R.id.edit_facility_FacilityEditText)).perform(ViewActions.clearText());
            onView(withId(R.id.edit_facility_FacilityEditText)).perform(ViewActions.typeText("The Event Centre"));

            // Click confirm
            onView(withText("Confirm"))
                    .inRoot(isDialog())
                    .check(matches(isDisplayed()))
                    .perform(click());

            // Check that facility text matches the new facility name
            onView(withId(R.id.facilityTextView)).check(matches(withText("The Event Centre")));
        }
    }

    private static ViewAction setSpaceVisibility(final boolean value) {
        return new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(Space.class);
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.setVisibility(value ? View.VISIBLE : View.GONE);
            }

            @Override
            public String getDescription() {
                return "Show / Hide View";
            }
        };
    }
}
