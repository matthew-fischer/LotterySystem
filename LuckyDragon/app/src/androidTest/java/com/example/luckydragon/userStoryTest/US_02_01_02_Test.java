package com.example.luckydragon.userStoryTest;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for User Story 02.01.02.
 * Organizer - Store hash data of the generated QR code in my database.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class US_02_01_02_Test {
    /**
     * USER STORY TEST
     * THIS TEST USES THE DATABASE.
     * IF IT FAILS, CHECK THAT THE TEST USER HAS NOT BEEN DELETED FROM THE DATABASE.
     * User opens app and selects "Organizer".
     * User clicks "Add Event".
     * User enters event details.
     * Check that the QR code hash data has been stored in the database.
     */
    @Test
    public void testQRCodeStoredInDB() {
        // TODO write test
    }
}
