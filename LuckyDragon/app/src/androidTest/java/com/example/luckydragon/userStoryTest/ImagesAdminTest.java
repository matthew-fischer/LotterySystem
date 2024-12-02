package com.example.luckydragon.userStoryTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.not;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.luckydragon.Activities.SelectRoleActivity;
import com.example.luckydragon.GlobalApp;
import com.example.luckydragon.MockedDb;
import com.example.luckydragon.R;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains tests for all administrator stories related to images.
 */
public class ImagesAdminTest extends MockedDb {

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
        testUserData.put("isAdmin", true);
        // Facility
        testUserData.put("facility", "The Sports Centre");
        // Profile pictures
        testUserData.put("defaultProfilePicture", "iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAYAAABw4pVUAAAAAXNSR0IArs4c6QAAAARzQklUCAgICHwIZIgAAAStSURBVHic7d3NbxtFGMfx38zueje26yAnjUJoeBGI0lKEiireTqhSJRBIvFwrJG5I/Q/6J/TGiQMgEBJHjkQ9VOJQiZYDSnsARVQKhaZJVeq0dlw7632Z4ZBYELLjpmDPPs4+n+u40SN/5d14dycVG2eWNBgZMu8B2E4chBgOQgwHIYaDEMNBiOEgxHAQYjgIMRyEGA5CDAchhoMQw0GI4SDEcBBiOAgxHIQYDkIMByGGgxDDQYjhIMRwEGLcvAf4N+dIBcEHMwAAOefvWldrPfM/TjV0pKFDBd2MoRox0uUu0uXNUY07dOSCiAmZGaJv0JqJ7qRIr3URX20j+Wnj/4w3cuSCjIKoOHCPH4B7/ADU29OIf2gi+v4uoPKebLfCnUPkTAn++zOonH0K7rFq3uPsUrggfXLOx8THh+C/dzDvUXYYu0NWutQxLzoCKEnImgNRcwFXDP5hEiidmoKc9bH5xSqQ5L8RYKyC9L5rIDrf2NuLAwn3aAXusSrc56sQVcf4UveFKspn5tH9dCX3KGMV5KGECsliG8liG/AlSifr8E/WgXL2Udo5XMbEh49i86s1y4PuVIxzSE8hOt/A/XPXB34ncU/UUDo1ZXGw3YoRZJtuxOh+8geSK23ja/w3pyAPPfx3nWEpVBAAgAI2v1xF+ms3ez2QCN6dsTvTPxQvCLAV5es16GaSuewcqcA5WrE81JZiBgGgWwl6C3eyFwVQeqNud6BthQ0CAPGlFtRKmLnmHi5DzpQsT1TwIAAQXWpmL7gC3ss1u8OAgyC+3AK62VcZnefsn0cKHwSxRnI9+zcuZz4AfLtvEQcBkP5m+LLoCrjPlq3OwkEApL9nn9gBwHk8sDgJBwEApMtd480qUfeszsJBACDW0N00c0nW7F5/5SDbdCc7CMrmy/ajwEH6DPdBxINucg0ZB+kz3ZjiIMRYfoc4CDEc5EEs32LnIH2mc0VqtwgH6fMMQWIOkgsRZL8Vumf3eVMOAgC+hKhmfyPXG9m3eUeFgwBw5n3jO6HucRDrnGfMl9jVqvlK8ChwEADOkxPZCxpIrhkeFxoRDuIJuE9nf0LU7Qi6xYcsq7xXJ43P+6aWPx0AB0HptUeMa/EV+9vfCh3Ee2US8onsW7TqRsifEJtEzYX/zrRxPbrcsjjN3wobJDg9a7xfrtZ6iC/eszzRlkIGCT6aM2/41EBvYY+7tEZg/+6gyiBqLoLTswN338Y/tpBcNe8fGbXCBPFen4T/1vTAx3rUSojw29sWp9ptXwcR0x68EzV4L9UgHxu8K0qvx+h+vgqE+f41gbEK4r1YBbTh/oQQEL6ECCRE3YM86EFOl/Z0llS3Imx+dhN6PR7uwP/BWAWR8wH8+eE+2pn80kH4zS3rl9lNxirIMOlOiujCXUQX1vMeZYfiBQkV4sU2egt3jHsM81SMIBpQN0MkP99HdLFJ5vCUZf8E0dh6+jDWUJ0UupVANSKkN0KkSx2oP6O8J9wTwf/1Ki2FvHRCGQchhoMQw0GI4SDEcBBiOAgxHIQYDkIMByGGgxDDQYjhIMRwEGI4CDEchBgOQgwHIYaDEMNBiOEgxHAQYjgIMRyEmL8AVZY0IlDexEUAAAAASUVORK5CYII=");
        testUserData.put("profilePicture", "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");


        return testUserData;
    }

    @Override
    protected void loadMockEventData(Map<String, Map<String, Object>> events) {
        HashMap<String, Object> eventData = new HashMap<>();
        eventData.put("name", "C301 Standup");
        eventData.put("organizerDeviceId", "mockOrgId");
        eventData.put("facility", "UofA");
        eventData.put("waitListLimit", 10L);
        eventData.put("attendeeLimit", 10L);
        eventData.put("hasGeolocation", true);
        eventData.put("date", LocalDate.now().toString());
        eventData.put("hours", 10L);
        eventData.put("minutes", 30L);
        eventData.put("hashedQR", "Fake QR");
        eventData.put("poster", "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");
        eventData.put("waitList", new ArrayList<>());
        eventData.put("inviteeList", new ArrayList<>());
        eventData.put("attendeeList", new ArrayList<>());
        eventData.put("cancelledList", new ArrayList<>());

        String id = "event123";
        events.put(id, eventData);
    }

    /**
     * Contains test for US 03.03.01.
     * As an administrator, I want to be able to remove images.
     */
    @Test
    public void testRemoveEventPoster() {

        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);

        try (final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            // User is admin, so admin button should show
            onView(ViewMatchers.withId(R.id.entrantButton)).check(matches(isDisplayed()));
            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
            onView(withId(R.id.adminButton)).check(matches(isDisplayed()));

            // Admin clicks "Administrator"
            onView(withId(R.id.adminButton)).perform(click());

            // Admin profile fragment should open and view events button should be displayed
            onView(withId(R.id.viewEventsButton)).check(matches(isDisplayed()));

            // Admin clicks "View Events"
            onView(withId(R.id.viewEventsButton)).perform(click());

            // Check if the event "C301 Standup" is displayed
            onView(withText("C301 Standup")).check(matches(isDisplayed()));

            // Click on the event "C301 Standup"
            onView(withText("C301 Standup")).perform(click());

            // Admin event fragment should open and "Remove Event Poster" button should be displayed
            onView(withId(R.id.adminRemoveEventPoster)).check(matches(isDisplayed()));

            // Admin clicks "Remove Event Poster" button
            onView(withId(R.id.adminRemoveEventPoster)).perform(click());

            // "Remove Event Poster" button should not exist anymore as event poster has been removed
            onView(withId(R.id.adminRemoveEventPoster)).check(matches(not(isDisplayed())));

        }
    }

    /**
     * Contains test for US 03.03.01.
     * As an administrator, I want to be able to remove images.
     */
    @Test
    public void testRemoveProfilePicture() {

        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final Intent intent = new Intent(targetContext, SelectRoleActivity.class);

        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(mockFirestore);

        try (final ActivityScenario<SelectRoleActivity> scenario = ActivityScenario.launch(intent)) {
            // User is admin, so admin button should show
            onView(ViewMatchers.withId(R.id.entrantButton)).check(matches(isDisplayed()));
            onView(withId(R.id.organizerButton)).check(matches(isDisplayed()));
            onView(withId(R.id.adminButton)).check(matches(isDisplayed()));

            // Admin clicks "Administrator"
            onView(withId(R.id.adminButton)).perform(click());

            // Admin profile fragment should open and view profiles button should be displayed
            onView(withId(R.id.viewProfilesButton)).check(matches(isDisplayed()));

            // Admin clicks "View Profiles"
            onView(withId(R.id.viewProfilesButton)).perform(click());

            // Check if the user "John Doe" is displayed
            onView(withText("John Doe")).check(matches(isDisplayed()));

            // Click on the user "John Doe"
            onView(withText("John Doe")).perform(click());

            // Admin user fragment should open and "Remove Profile Picture" button should be displayed
            onView(withId(R.id.adminRemoveProfilePictureButton)).check(matches(isDisplayed()));

            // Admin clicks "Remove Profile Picture" button
            onView(withId(R.id.adminRemoveProfilePictureButton)).perform(click());

            // "Remove Profile Picture" button should not exist anymore as profile picture has been removed
            onView(withId(R.id.adminRemoveEventPoster)).check(matches(not(isDisplayed())));

        }
    }

}
