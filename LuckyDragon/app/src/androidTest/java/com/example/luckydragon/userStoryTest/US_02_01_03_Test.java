//package com.example.luckydragon.userStoryTest;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.RootMatchers.isDialog;
//import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.CoreMatchers.not;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.View;
//import android.widget.Space;
//
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.espresso.UiController;
//import androidx.test.espresso.ViewAction;
//import androidx.test.espresso.action.ViewActions;
//import androidx.test.espresso.matcher.ViewMatchers;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.filters.LargeTest;
//
//import org.hamcrest.Matcher;
//import org.junit.Test;
//import androidx.test.platform.app.InstrumentationRegistry;
//
//import com.example.luckydragon.GlobalApp;
//import com.example.luckydragon.R;
//import com.example.luckydragon.SelectRoleActivity;
//import com.example.luckydragon.User;
//
//import org.junit.runner.RunWith;
//
///**
// * Test for User Story 02.01.03.
// * Organizer - Create and manage my facility profile.
// */
//@RunWith(AndroidJUnit4.class)
//@LargeTest
//public class US_02_01_03_Test {
//    /**
//     * USER STORY TEST
//     * User opens app and selects "Organizer".
//     * User does not have a facility yet.
//     * The correct message is displayed for an organizer without a facility.
//     * User clicks edit button and EditFacilityDialogFragment is started.
//     * The text input field is empty, since the user does not have a facility yet.
//     * User enters a new facility name and selects confirm.
//     * EditFacilityDialogFragment closes.
//     * The change is reflected on the organizer's profile.
//     */
//    @Test
//    public void testSetFacilityFirstTime() {
//        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);
//
//        // Declare test facility name
//        String testFacilityName = "This is a test facility";
//
//        // Set user to a test object
//        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
//        User testUser = new User("test");
//        testUser.setOrganizer(true);
//        testUser.getOrganizer().setFacility(null);
//        testUser.setAdmin(false);
//        testUser.setIsLoaded(true);
//        globalApp.setUser(testUser);
//
//        try(final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
//            // Update views to match testUser (this must run on ui thread -- error otherwise)
//            scenario.onActivity(a -> {
//                a.runOnUiThread(testUser::notifyObservers);
//            });
//
//            // User is not admin, so admin button should not show
//            onView(ViewMatchers.withId(R.id.entrantButton)).check(matches(isDisplayed()));
//            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
//            onView(withId(R.id.adminButton)).check(matches(not(isDisplayed())));
//
//            // User clicks "Organizer"
//            onView(withId(R.id.organizerButton)).perform(click());
//
//            // Profile activity should open and organizer profile should be displayed
//            onView(withId(R.id.organizerProfileLayout)).check(matches(isDisplayed()));
//
//            // The correct message is displayed for an organizer with no facility
//            onView(withId(R.id.facilityTextView)).check(matches(withText(R.string.no_facility_message)));
//
//            // Before clicking the facility edit button, hide the space because it causes weird expresso behaviour
//            onView(withId(R.id.facilitySpace)).perform(setSpaceVisibility(false));
//
//            // User clicks facility edit button
//            onView(withId(R.id.facilityEditButton)).perform(click());
//
//            // Edit Facility dialog should be displayed now
//            onView(withId(R.id.editFacilityDialog)).check(matches(isDisplayed()));
//
//            // The facility edittext is empty, since user does not have a facility yet
//            onView(withId(R.id.edit_facility_FacilityEditText)).check(matches(withText("")));
//
//            // Set edittext to test facility
//            onView(withId(R.id.edit_facility_FacilityEditText)).perform(ViewActions.typeText(testFacilityName));
//
//            // Click confirm
//            onView(withText("Confirm"))
//                    .inRoot(isDialog())
//                    .check(matches(isDisplayed()))
//                    .perform(click());
//
//            // Check that facility text matches the new facility name
//            onView(withId(R.id.facilityTextView)).check(matches(withText(testFacilityName)));
//        }
//    }
//
//    /**
//     * USER STORY TEST
//     * User opens app and selects "Organizer".
//     * User already has a facility.
//     * The user's existing facility is displayed.
//     * User clicks edit button and EditFacilityDialogFragment is started.
//     * The text input field is the user's existing facility.
//     * User enters a new facility name and selects confirm.
//     * EditFacilityDialogFragment closes.
//     * The change is reflected on the organizer's profile.
//     */
//    @Test
//    public void testSetFacilityNotFirstTime() {
//        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);
//
//        // Declare test facility names
//        String previousFacility = "My Previous Facility";
//        String newFacility = "My New Facility";
//
//        // Set user to a test object
//        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
//        User testUser = new User("test");
//        testUser.setOrganizer(true);
//        testUser.getOrganizer().setFacility(previousFacility);
//        testUser.setAdmin(false);
//        testUser.setIsLoaded(true);
//        globalApp.setUser(testUser);
//
//        try(final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
//            // Update views to match testUser (this must run on ui thread -- error otherwise)
//            scenario.onActivity(a -> {
//                a.runOnUiThread(testUser::notifyObservers);
//            });
//
//            // Update views to match testUser (this must run on ui thread -- error otherwise)
//            scenario.onActivity(a -> {
//                a.runOnUiThread(testUser::notifyObservers);
//            });
//
//            // User is not admin, so admin button should not show
//            onView(ViewMatchers.withId(R.id.entrantButton)).check(matches(isDisplayed()));
//            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
//            onView(withId(R.id.adminButton)).check(matches(not(isDisplayed())));
//
//            // User clicks "Organizer"
//            onView(withId(R.id.organizerButton)).perform(click());
//
//            // Profile activity should open and organizer profile should be displayed
//            onView(withId(R.id.organizerProfileLayout)).check(matches(isDisplayed()));
//
//            // The organizer's facility is displayed
//            onView(withId(R.id.facilityTextView)).check(matches(withText(previousFacility)));
//
//            // Before clicking the facility edit button, hide the space because it causes weird espresso behaviour
//            onView(withId(R.id.facilitySpace)).perform(setSpaceVisibility(false));
//
//            // User clicks facility edit button
//            onView(withId(R.id.facilityEditButton)).perform(click());
//
//            // Edit Facility dialog should be displayed now
//            onView(withId(R.id.editFacilityDialog)).check(matches(isDisplayed()));
//
//            // The facility edittext is the previous facility
//            onView(withId(R.id.edit_facility_FacilityEditText)).check(matches(withText(previousFacility)));
//
//            // Set edittext to test facility
//            onView(withId(R.id.edit_facility_FacilityEditText)).perform(ViewActions.clearText());
//            onView(withId(R.id.edit_facility_FacilityEditText)).perform(ViewActions.typeText(newFacility));
//
//            // Click confirm
//            onView(withText("Confirm"))
//                    .inRoot(isDialog())
//                    .check(matches(isDisplayed()))
//                    .perform(click());
//
//            // Check that facility text matches the new facility name
//            onView(withId(R.id.facilityTextView)).check(matches(withText(newFacility)));
//        }
//    }
//
//    /**
//     * USER STORY TEST
//     * User opens app and selects "Organizer".
//     * User already has a facility.
//     * The user's existing facility is displayed.
//     * User clicks edit button and EditFacilityDialogFragment is started.
//     * The text input field is the user's existing facility.
//     * User sets text input text to "" (empty).
//     * User selects confirm.
//     * EditFacilityDialogFragment closes.
//     * No change is made to the organizer's profile.
//     */
//    @Test
//    public void testSetFacilityEmpty() {
//        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);
//
//        // Declare test facility names
//        String previousFacility = "My Previous Facility";
//
//        // Set user to a test object
//        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
//        User testUser = new User("test");
//        testUser.setOrganizer(true);
//        testUser.getOrganizer().setFacility(previousFacility);
//        testUser.setAdmin(false);
//        testUser.setIsLoaded(true);
//        globalApp.setUser(testUser);
//
//        try(final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
//            // Update views to match testUser (this must run on ui thread -- error otherwise)
//            scenario.onActivity(a -> {
//                a.runOnUiThread(testUser::notifyObservers);
//            });
//
//            // Update views to match testUser (this must run on ui thread -- error otherwise)
//            scenario.onActivity(a -> {
//                a.runOnUiThread(testUser::notifyObservers);
//            });
//
//            // User is not admin, so admin button should not show
//            onView(ViewMatchers.withId(R.id.entrantButton)).check(matches(isDisplayed()));
//            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
//            onView(withId(R.id.adminButton)).check(matches(not(isDisplayed())));
//
//            // User clicks "Organizer"
//            onView(withId(R.id.organizerButton)).perform(click());
//
//            // Profile activity should open and organizer profile should be displayed
//            onView(withId(R.id.organizerProfileLayout)).check(matches(isDisplayed()));
//
//            // The organizer's facility is displayed
//            onView(withId(R.id.facilityTextView)).check(matches(withText(previousFacility)));
//
//            // Before clicking the facility edit button, hide the space because it causes weird espresso behaviour
//            onView(withId(R.id.facilitySpace)).perform(setSpaceVisibility(false));
//
//            // User clicks facility edit button
//            onView(withId(R.id.facilityEditButton)).perform(click());
//
//            // Edit Facility dialog should be displayed now
//            onView(withId(R.id.editFacilityDialog)).check(matches(isDisplayed()));
//
//            // The facility edittext is the previous facility
//            onView(withId(R.id.edit_facility_FacilityEditText)).check(matches(withText(previousFacility)));
//
//            // Set edittext to test facility
//            onView(withId(R.id.edit_facility_FacilityEditText)).perform(ViewActions.clearText());
//
//            // Click confirm
//            onView(withText("Confirm"))
//                    .inRoot(isDialog())
//                    .check(matches(isDisplayed()))
//                    .perform(click());
//
//            // Check that facility was not changed
//            onView(withId(R.id.facilityTextView)).check(matches(withText(previousFacility)));
//        }
//    }
//
//    /**
//     * USER STORY TEST
//     * User opens app and selects "Organizer".
//     * User already has a facility.
//     * The user's existing facility is displayed.
//     * User clicks edit button and EditFacilityDialogFragment is started.
//     * The text input field is the user's existing facility.
//     * User sets text input text to a new facility.
//     * User selects cancel
//     * EditFacilityDialogFragment closes.
//     * No change is made to the organizer's profile.
//     */
//    @Test
//    public void testSetFacilityCancelled() {
//        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);
//
//        // Declare test facility names
//        String previousFacility = "My Previous Facility";
//        String newFacility = "My New Facility";
//
//        // Set user to a test object
//        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
//        User testUser = new User("test");
//        testUser.setOrganizer(true);
//        testUser.getOrganizer().setFacility(previousFacility);
//        testUser.setAdmin(false);
//        testUser.setIsLoaded(true);
//        globalApp.setUser(testUser);
//
//        try(final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
//            // Update views to match testUser (this must run on ui thread -- error otherwise)
//            scenario.onActivity(a -> {
//                a.runOnUiThread(testUser::notifyObservers);
//            });
//
//            // Update views to match testUser (this must run on ui thread -- error otherwise)
//            scenario.onActivity(a -> {
//                a.runOnUiThread(testUser::notifyObservers);
//            });
//
//            // User is not admin, so admin button should not show
//            onView(ViewMatchers.withId(R.id.entrantButton)).check(matches(isDisplayed()));
//            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
//            onView(withId(R.id.adminButton)).check(matches(not(isDisplayed())));
//
//            // User clicks "Organizer"
//            onView(withId(R.id.organizerButton)).perform(click());
//
//            // Profile activity should open and organizer profile should be displayed
//            onView(withId(R.id.organizerProfileLayout)).check(matches(isDisplayed()));
//
//            // The organizer's facility is displayed
//            onView(withId(R.id.facilityTextView)).check(matches(withText(previousFacility)));
//
//            // Before clicking the facility edit button, hide the space because it causes weird espresso behaviour
//            onView(withId(R.id.facilitySpace)).perform(setSpaceVisibility(false));
//
//            // User clicks facility edit button
//            onView(withId(R.id.facilityEditButton)).perform(click());
//
//            // Edit Facility dialog should be displayed now
//            onView(withId(R.id.editFacilityDialog)).check(matches(isDisplayed()));
//
//            // The facility edittext is the previous facility
//            onView(withId(R.id.edit_facility_FacilityEditText)).check(matches(withText(previousFacility)));
//
//            // Set edittext to test facility
//            onView(withId(R.id.edit_facility_FacilityEditText)).perform(ViewActions.clearText());
//            onView(withId(R.id.edit_facility_FacilityEditText)).perform(ViewActions.typeText(newFacility));
//
//            // Click confirm
//            onView(withText("Cancel"))
//                    .inRoot(isDialog())
//                    .check(matches(isDisplayed()))
//                    .perform(click());
//
//            // Check that facility was not changed
//            onView(withId(R.id.facilityTextView)).check(matches(withText(previousFacility)));
//        }
//    }
//
//    private static ViewAction setSpaceVisibility(final boolean value) {
//        return new ViewAction() {
//
//            @Override
//            public Matcher<View> getConstraints() {
//                return isAssignableFrom(Space.class);
//            }
//
//            @Override
//            public void perform(UiController uiController, View view) {
//                view.setVisibility(value ? View.VISIBLE : View.GONE);
//            }
//
//            @Override
//            public String getDescription() {
//                return "Show / Hide View";
//            }
//        };
//    }
//}
