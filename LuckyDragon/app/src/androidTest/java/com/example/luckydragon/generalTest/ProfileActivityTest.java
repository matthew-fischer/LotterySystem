//package com.example.luckydragon.generalTest;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.CoreMatchers.not;
//
//import static java.lang.Thread.sleep;
//
//import android.content.Context;
//import android.content.Intent;
//
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.espresso.matcher.ViewMatchers;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.filters.LargeTest;
//import androidx.test.platform.app.InstrumentationRegistry;
//
//import com.example.luckydragon.GlobalApp;
//import com.example.luckydragon.ProfileActivity;
//import com.example.luckydragon.R;
//import com.example.luckydragon.User;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(AndroidJUnit4.class)
//@LargeTest
//public class ProfileActivityTest {
//    /**
//     * TEST
//     * Tests that an entrant's name, email, and phone number are shown correctly.
//     */
//    @Test
//    public void testEntrantNameEmailPhoneNumber() {
//        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        final Intent intent = new Intent(targetContext, ProfileActivity.class);
//
//        // Initialize test user information
//        String testName = "Test Name";
//        String testEmail = "Test Email";
//        String testPhoneNumber = "849-193-7491";
//
//        // Set user to a test object
//        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
//        User testUser = new User("test");
//        testUser.setName(testName);
//        testUser.setEmail(testEmail);
//        testUser.setPhoneNumber(testPhoneNumber);
//        testUser.setIsLoaded(true);
//        globalApp.setUser(testUser);
//        // Set app role
//        globalApp.setRole(GlobalApp.ROLE.ENTRANT);
//
//        // Test profile activity
//        try(final ActivityScenario<ProfileActivity> scenario = ActivityScenario.launch(intent)) {
//            // Update views to match testUser (this must run on ui thread -- error otherwise)
//            scenario.onActivity(a -> {
//                a.runOnUiThread(testUser::notifyObservers);
//            });
//
//
//
//            // Assertions
//            onView(ViewMatchers.withId(R.id.nameTextView)).check(matches(withText(testName)));
//            onView(withId(R.id.emailTextView)).check(matches(withText(testEmail)));
//            onView(withId(R.id.phoneNumberTextView)).check(matches(withText(testPhoneNumber)));
//        }
//    }
//
//    /**
//     * TEST
//     * Tests that if an entrant only has a name and email, then:
//     *   - the name and email are shown correctly
//     *   - the phone number is not displayed
//     */
//    @Test
//    public void testEntrantNameAndEmail() {
//        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        final Intent intent = new Intent(targetContext, ProfileActivity.class);
//
//        // Initialize test user information
//        String testName = "Test Name";
//        String testEmail = "Test Email";
//
//        // Set user to a test object
//        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
//        User testUser = new User("test");
//        testUser.setName(testName);
//        testUser.setEmail(testEmail);
//        testUser.setPhoneNumber(null);
//        globalApp.setUser(testUser);
//        testUser.setIsLoaded(true);
//        // Set app role
//        globalApp.setRole(GlobalApp.ROLE.ENTRANT);
//
//        // Test profile activity
//        try(final ActivityScenario<ProfileActivity> scenario = ActivityScenario.launch(intent)) {
//            // Update views to match testUser (this must run on ui thread -- error otherwise)
//            scenario.onActivity(a -> {
//                a.runOnUiThread(testUser::notifyObservers);
//            });
//
//            // Assertions
//            onView(withId(R.id.nameTextView)).check(matches(withText(testName)));
//            onView(withId(R.id.emailTextView)).check(matches(withText(testEmail)));
//            onView(withId(R.id.phoneNumberTextView)).check(matches(not(isDisplayed())));
//        }
//    }
//
//    /**
//     * TEST
//     * Tests that if an organizer only has a name, then:
//     *   - the name is displayed correctly
//     *   - the email and phone number are not displayed
//     */
//    @Test
//    public void testOrganizerName() {
//        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        final Intent intent = new Intent(targetContext, ProfileActivity.class);
//
//        // Initialize test user information
//        String testName = "Test Name";
//
//        // Set user to a test object
//        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
//        User testUser = new User("test");
//        testUser.setName(testName);
//        testUser.setEmail(null);
//        testUser.setPhoneNumber(null);
//        testUser.setIsLoaded(true);
//        globalApp.setUser(testUser);
//        //globalApp.getUser().setOrganizer(true);
//        // Set app role
//        globalApp.setRole(GlobalApp.ROLE.ENTRANT);
//
//        // Test profile activity
//        try(final ActivityScenario<ProfileActivity> scenario = ActivityScenario.launch(intent)) {
//            // Update views to match testUser (this must run on ui thread -- error otherwise)
//            scenario.onActivity(a -> {
//                a.runOnUiThread(testUser::notifyObservers);
//            });
//
//            // Assertions
//            onView(withId(R.id.nameTextView)).check(matches(withText(testName)));
//            onView(withId(R.id.emailTextView)).check(matches(not(isDisplayed())));
//            onView(withId(R.id.phoneNumberTextView)).check(matches(not(isDisplayed())));
//        }
//    }
//}
