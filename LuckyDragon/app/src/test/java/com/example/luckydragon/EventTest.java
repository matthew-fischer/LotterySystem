package com.example.luckydragon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    /**
     * just a placeholder
     */
    void testFake() {
        assertTrue(true);
    }
    /**
     * TEST
     * Tests that the removeFromWaitList method removes an entrant from the list.
     */
//    @Test
//    void testRemoveFromJoinWaitList() {
//        Event event = mockEvent();
//        String entrant = event.getWaitList().get(0);
//        event.leaveWaitList(entrant);
//
//        assertFalse(event.getWaitList().contains(entrant));
//    }

    /**
     * TEST
     * Tests that the drawEntrantFromWaitList method randomly selects
     * an entrant from the waiting list.
     */
//    @Test
//    void testDrawEntrantFromJoinWaitList() {
//        Event event = mockEvent();
//        String chosenEntrant = event.drawEntrantFromWaitList();
//
//
//        assertTrue(event.getWaitList().contains(chosenEntrant));
//
//        event.leaveWaitList(chosenEntrant);
//        chosenEntrant = event.drawEntrantFromWaitList();
//        assertNull(chosenEntrant);
//    }
}
