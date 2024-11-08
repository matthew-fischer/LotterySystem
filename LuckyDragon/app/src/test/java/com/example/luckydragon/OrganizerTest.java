/*
 * Contains unit tests for the Organizer model class.
 * ISSUES:
 *   NONE
 */

package com.example.luckydragon;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.MockitoAnnotations.openMocks;

import android.util.Log;

import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.Organizer;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

/**
 * This is a collection of unit tests for the User class.
 * fetchEvents() is not tested here since it requires the database.
 * fetchEvents() should be well-tested by our UI tests (since it is essential to the organizer profile), so this should not be an issue.
 */
public class OrganizerTest {
    @Mock
    private FirebaseFirestore mockFirestore;

    @BeforeEach
    public void setup() {
        openMocks(this);
    }

    /**
     * Tests adding an event to the organizer's list.
     * The event has a name, facility, and attendee limit, so it should be added.
     */
    @Test
    public void testAddEventValid() {
        // notifyObservers() runnable does nothing. db is null since this test will not use it
        Organizer testOrganizer = new Organizer("abcd1234", () -> {}, mockFirestore);

        Assertions.assertEquals(testOrganizer.getEvents().size(), 0);

        // Set up static mock for FirebaseFirestore
        try(MockedStatic<FirebaseFirestore> staticMockFirebase = Mockito.mockStatic(FirebaseFirestore.class)) {
            staticMockFirebase.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            Event testEvent = Mockito.spy(new Event("0", mockFirestore));

            // Mock notify observers since it relies on android
            doAnswer((invocation -> {
                return null;
            })).when(testEvent).notifyObservers();


            testEvent.setName("Swimming Lesson");
            testEvent.setFacility("Swimming Centre");
            testEvent.setAttendeeLimit(10);

            testOrganizer.addEvent(testEvent);

            Assertions.assertEquals(testOrganizer.getEvents().size(), 1);
            Assertions.assertEquals(testOrganizer.getEvents().get(0).getId(), "0");
        }
    }

    /**
     * Tests adding an event to the organizer's list.
     * The event does not have an attendee limit, so it should not be added.
     */
    @Test
    public void testAddEventInValid() {
        // notifyObservers() runnable is just an empty function
        Organizer testOrganizer = new Organizer("abcd1234", () -> {}, mockFirestore);

        Assertions.assertEquals(testOrganizer.getEvents().size(), 0);

        // Set up static mock for FirebaseFirestore
        try(MockedStatic<FirebaseFirestore> staticMockFirebase = Mockito.mockStatic(FirebaseFirestore.class)) {
            staticMockFirebase.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            Event testEvent = Mockito.spy(new Event("0", mockFirestore));

            // Mock notify observers since it relies on android
            doAnswer((invocation -> {
                return null;
            })).when(testEvent).notifyObservers();

            testEvent.setName("Swimming Lesson");
            testEvent.setFacility("Swimming Centre");

            // Add event calls Log.e if adding an event fails
            // But Log is part of android framework so we have to mock it here
            try(MockedStatic<Log> staticMockLog = Mockito.mockStatic(Log.class)) {
                staticMockLog.when(() -> Log.e(anyString(), anyString())).thenAnswer((invocation) -> {
                    // do nothing
                    return null;
                });

                testOrganizer.addEvent(testEvent);

                Assertions.assertEquals(testOrganizer.getEvents().size(), 0);
            }
        }
    }

    /**
     * Tests remove an event from the organizer's list.
     */
    @Test
    public void testRemoveEvent() {
        // notifyObservers() runnable does nothing. db is null since this test will not use it
        Organizer testOrganizer = new Organizer("abcd1234", () -> {
        }, mockFirestore);

        // Set up static mock for FirebaseFirestore
        try (MockedStatic<FirebaseFirestore> staticMockFirebase = Mockito.mockStatic(FirebaseFirestore.class)) {
            staticMockFirebase.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            Event testEvent = Mockito.spy(new Event("0", mockFirestore));

            // Mock notify observers since it relies on android
            doAnswer((invocation -> {
                return null;
            })).when(testEvent).notifyObservers();

            testEvent.setName("Swimming Lesson");
            testEvent.setFacility("Swimming Centre");
            testEvent.setAttendeeLimit(10);

            testOrganizer.getEvents().add(testEvent);

            Assertions.assertEquals(testOrganizer.getEvents().size(), 1);
            Assertions.assertEquals(testOrganizer.getEvents().get(0).getId(), "0");

            testOrganizer.removeEvent(testEvent);
            Assertions.assertEquals(testOrganizer.getEvents().size(), 0);
        }
    }
}
