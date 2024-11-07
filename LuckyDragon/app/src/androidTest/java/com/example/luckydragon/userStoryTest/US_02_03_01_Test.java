package com.example.luckydragon.userStoryTest;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for User Story 02.03.01.
 * Organizer - Optionally limit the number of entrants who can join my waiting list.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class US_02_03_01_Test {
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
        // TODO write test
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


}
