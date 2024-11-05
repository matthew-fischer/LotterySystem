package com.example.luckydragon.userStoryTest;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for User Story 01.07.01.
 * Entrant - Be identified by my device, so that I don't have to use a username and password.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class US_01_07_01_Test {
    /**
     * USER STORY TEST
     * THIS TEST USES THE DATABASE.
     * IF IT FAILS, CHECK THAT THE TEST USER HAS NOT BEEN DELETED FROM THE DATABASE.
     * Existing user opens app and selects "Entrant".
     * User profile opens.
     */
    @Test
    public void testDeviceLogInExistingUser() {
        // TODO write test
    }

    /**
     * USER STORY TEST
     * THIS TEST USES THE DATABASE.
     * IF IT FAILS, CHECK THAT THE TEST USER HAS NOT BEEN ADDED TO THE DATABASE.
     * Non-existing user opens app and selects "Entrant".
     * Sign up page opens.
     */
    @Test
    public void testDeviceLogInNonExistingUser() {
        // TODO write test
    }
}
