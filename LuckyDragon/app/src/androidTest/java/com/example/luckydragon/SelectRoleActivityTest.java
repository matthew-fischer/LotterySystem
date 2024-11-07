/**
 * Contains tests for SelectRoleActivity.
 */

package com.example.luckydragon;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a test class for SelectRoleActivity.
 * It tests that the correct role buttons show for a user depending on whether they have admin privileges or not.
 * NOTES:
 *   - The User is set in GlobalApp before the activity is started, so no database fetching will occur in these tests.
 *   - Use the ActivityScenario.launch() syntax to run some code before the activity starts. ActivityScenarioRule does not allow this.
 *   - We need to call notifyObservers() on the test user to update the views to reflect the user. This must be done on the UI thread, or an error will occur.
 */
//@RunWith(AndroidJUnit4.class)
//@LargeTest
public class SelectRoleActivityTest {
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

//    @Rule
//    public ActivityTestRule<SelectRoleActivity> scenario =
//            new ActivityScenarioRule<SelectRoleActivity>(SelectRoleActivity.class, true, false);

    @Before
    public void setup() {
        Intents.init();
        openMocks(this);
        when(mockFirestore.collection("users")).thenReturn(mockCollection);
        when(mockCollection.document(anyString())).thenReturn(mockDocument);

//        when(mockCollection.get()).thenReturn(mocktask);
        when(mockDocument.get()).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any(OnFailureListener.class))).thenReturn(mockTask);

        Map<String, Object> mockData = getMockData();
        when(mockDocumentSnapshot.getData()).thenReturn(mockData);
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testEntrantSignup() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);

        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);
        try (final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            waitForViewToAppear(R.id.entrantButton, 5000);
//        onView(withId(R.id.entrantButton)).check(matches(withText("ENTRANT")));
            // Click entrant button
            onView(withId(R.id.entrantButton)).perform(click());

            // Check we are in signup
            onView(withId(R.id.signupName)).check(matches(isDisplayed()));
        }
    }
    /**
     * TEST
     * Tests that only "Entrant" and "Organizer" buttons show for a user without admin privileges.
     * "Administrator" button should not be visible.
     */
    @Test
    public void testButtonsForNonAdminUser() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        // Set user to a test object
        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        User testUser = new User("test", FirebaseFirestore.getInstance());
        testUser.setIsLoaded(true);
        globalApp.setUser(testUser);

        try (final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            // Update views to match testUser (this must run on ui thread -- error otherwise)
            scenario.onActivity(a -> {
                a.runOnUiThread(testUser::notifyObservers);
            });

            // Assertions
            onView(withId(R.id.entrantButton)).check(matches(isDisplayed()));
            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
            onView(withId(R.id.adminButton)).check(matches(not(isDisplayed())));
        }
    }

    /**
     * TEST
     * Tests that "Entrant", "Organizer", and "Administrator" buttons show for a user with admin privileges.
     */
    @Test
    public void testButtonsForAdminUser() {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        // Set user to a test object
        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        User testUser = new User("test", FirebaseFirestore.getInstance());
        testUser.setAdmin(true);
        testUser.setIsLoaded(true);
        globalApp.setUser(testUser);

        try(final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            // Update views to match testUser (this must run on ui thread -- error otherwise)
            scenario.onActivity(a -> {
                a.runOnUiThread(testUser::notifyObservers);
            });

            // Assertions
            onView(withId(R.id.entrantButton)).check(matches(isDisplayed()));
            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
            onView(withId(R.id.adminButton)).check(matches((isDisplayed())));
        }
    }

    private HashMap<String, Object> getMockData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("isEntrant", false);
        map.put("isOrganizer", true);
        map.put("isAdmin", true);

        map.put("facility", "My Fake Facility");

        map.put("name", "Nake Fame");
        map.put("email", "email@address.co");
        map.put("phoneNumber", "1231231234");
        map.put("notifications", true);
//        map.put("profilePicture", bitmapToString(uploadedProfilePicture));
//        map.put("defaultProfilePicture", bitmapToString(defaultProfilePicture));
        return map;
    }
    // source: https://stackoverflow.com/questions/22358325/how-to-wait-till-a-view-has-gone-in-espresso-tests
    public void waitForViewToAppear(int viewId, long maxWaitingTimeMs) {
        long endTime = System.currentTimeMillis() + maxWaitingTimeMs;
        while (System.currentTimeMillis() <= endTime) {
            try {
                onView(allOf(withId(viewId), isDisplayed())).check(matches(not(doesNotExist())));
                return;
            } catch (NoMatchingViewException ignored) {
            }
        }
        throw new RuntimeException("timeout exceeded"); // or whatever exception you want
    }
}
