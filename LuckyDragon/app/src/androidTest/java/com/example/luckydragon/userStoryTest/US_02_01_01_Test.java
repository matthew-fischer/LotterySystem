package com.example.luckydragon.userStoryTest;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for User Story 02.01.01.
 * Organizer - create a new event and generate a unique promotional QR code that links to the event description and event poster in the app.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class US_02_01_01_Test {
    // TODO write tests
    /**
     * USER STORY TEST
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
    public void testOrganizerCreateEventWithExistingFacility() {
        // TODO write test
    }

    /**
     * USER STORY TEST
     * User opens app and selects 'Organizer'.
     * User does not have an existing facility.
     * The correct message is displayed for a user without a facility.
     * User clicks "Add Event" button.
     * Edit Facility Dialog is displayed.
     * User enters a facility and clicks confirm.
     * Add Event Dialog is displayed.
     * User enters event details.
     * User clicks create event.
     * Event is created and a QR code is generated.
     */
    @Test
    public void testOrganizerCreateEventWithoutExistingFacility() {
        // TODO write test
    }


}
