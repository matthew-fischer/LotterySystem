package com.example.luckydragon;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a test class for the Event class.
 * It tests the methods in the Event class.
 */
public class EventTest {

    private Event mockEvent() {
        List<String> waitList = new ArrayList<String>(Arrays.asList("device-1"));
        return new Event("t_event_id");
    }

    /**
     * TEST
     * Tests that the removeFromWaitList method removes an entrant from the list.
     */
    @Test
    public void testRemoveFromWaitList() {
        Event event = mockEvent();
        String entrant = event.getWaitList().get(0);
        event.removeFromWaitList(entrant);

        assertFalse(event.getWaitList().contains(entrant));
    }

    /**
     * TEST
     * Tests that the drawEntrantFromWaitList method randomly selects
     * an entrant from the waiting list.
     */
    @Test
    public void testDrawEntrantFromWaitList() {
        Event event = mockEvent();
        String chosenEntrant = event.drawEntrantFromWaitList();


        assertTrue(event.getWaitList().contains(chosenEntrant));

        event.removeFromWaitList(chosenEntrant);
        chosenEntrant = event.drawEntrantFromWaitList();
        assertNull(chosenEntrant);
    }
}
