package com.example.luckydragon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.example.luckydragon.Models.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a collection of tests for the Event class.
 * These tests should not use the database. The database is mocked using Mockito.
 * If a method primarily uses the database, then there is little sense in testing it because we would just be testing the database mock.
 * The focus is on testing logic methods, like adding/deleting from lists.
 */
public class EventTest {
    // Mock database
    @Mock
    private FirebaseFirestore mockFirestore;
    // User mocks
    @Mock
    private CollectionReference mockUsersCollection;
    @Mock
    private DocumentReference mockUserDocument;
    @Mock
    private DocumentSnapshot mockUserDocumentSnapshot;
    @Mock
    private Task<DocumentSnapshot> mockUserTask;
    @Mock
    private Task<Void> mockVoidTask;
    // Event mocks
    @Mock
    private CollectionReference mockEventsCollection;
    @Mock
    private DocumentReference mockEventDocument;
    @Mock
    private DocumentSnapshot mockEventDocumentSnapshot;
    @Mock
    private Task<DocumentSnapshot> mockEventTask;
    @Mock
    private Query mockEventQuery;
    @Mock
    private Task<QuerySnapshot> mockEventQueryTask;

    private HashMap<String, Object> capturedEventData;

    private HashMap<String, Object> getMockData() {
        HashMap<String, Object> testData = new HashMap<>();
        testData.put("name", "Piano Lesson");
        testData.put("facility", "The Piano");
        testData.put("hours", new Long(10));
        testData.put("minutes", new Long(30));
        return testData;
    }

    @BeforeEach
    public void setup() {
        openMocks(this);

        // Set up user mocking
        when(mockFirestore.collection("users")).thenReturn(mockUsersCollection);
        when(mockUsersCollection.document(anyString())).thenReturn(mockUserDocument);
        when(mockUserDocument.get()).thenReturn(mockUserTask);
        when(mockUserDocument.set(any(Map.class))).thenAnswer((invocation) -> {
            return mockVoidTask;
        });
        //when(mockUserDocument.set(any(Map.class))).thenReturn(mockVoidTask);
        when(mockUserTask.addOnFailureListener(any(OnFailureListener.class))).thenReturn(mockUserTask);
        doAnswer(invocation -> {
            OnSuccessListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onSuccess(mockUserDocumentSnapshot);
            return mockUserTask;
        }).when(mockUserTask).addOnSuccessListener(any(OnSuccessListener.class));
        when(mockUserDocumentSnapshot.getData()).thenReturn(new HashMap<>());
        // Set up event mocking
        // We don't want to save anything to the database, so we mock the methods that save an event to the db to do nothing
        // We also mock getId() to return "mockEventID" instead of going to the database for an id
        when(mockFirestore.collection("events")).thenReturn(mockEventsCollection);
        when(mockEventsCollection.document()).thenReturn(mockEventDocument);
        when(mockEventDocument.getId()).thenReturn("mockEventID");

        when(mockEventsCollection.document(anyString())).thenReturn(mockEventDocument);
        when(mockEventDocument.get()).thenReturn(mockEventTask);
        // when set is called, we want to do nothing
        when(mockEventDocument.set(anyMap())).thenAnswer((invocation) -> {
            capturedEventData = invocation.getArgument(0);
            return mockVoidTask;
        });

        // in the on complete listener, we want to do nothing
        when(mockEventTask.addOnCompleteListener(any())).thenAnswer((invocation) -> {
            OnCompleteListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onComplete(mockEventTask);
            return null;
        });
        when(mockEventTask.getResult()).thenReturn(mockEventDocumentSnapshot);
        when(mockEventDocumentSnapshot.getData()).thenReturn(getMockData());
        when(mockEventTask.isSuccessful()).thenReturn(true);
        when(mockEventDocumentSnapshot.exists()).thenReturn(true);
    }

    /**
     * Tests save.
     * Save uses the database. We test it by checking that all information passed to firestore set() is correct.
     */
    @Test
    public void testSave() {
        // Set up static mock for FirebaseFirestore
        try (MockedStatic<FirebaseFirestore> staticMockFirebase = Mockito.mockStatic(FirebaseFirestore.class)) {
            staticMockFirebase.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            // Create event and set data
            // Event is a spy so we mock out UI and android functions as needed (e.g. notifyObservers)
            Event testEvent = Mockito.spy(new Event("0", mockFirestore));

            // Mock notify observers since it relies on android
            doAnswer((invocation -> {
                return null;
            })).when(testEvent).notifyObservers();

            testEvent.setName("Swimming Lesson");
            testEvent.setFacility("Swimming Centre");
            testEvent.setOrganizerDeviceId("abcd1234");
            testEvent.setTime(18, 0);

            testEvent.save();

            assertEquals(capturedEventData.get("name"), "Swimming Lesson");
            assertEquals(capturedEventData.get("facility"), "Swimming Centre");
            assertEquals(capturedEventData.get("organizerDeviceId"), "abcd1234");
            assertEquals(capturedEventData.get("hours"), 18);
            assertEquals(capturedEventData.get("minutes"), 0);
        }
    }

    /**
     * Tests fetchData().
     * The data returned by the database is mocked. We then check that fields are set correctly.
     */
    @Test
    public void testFetchData() {
        try (MockedStatic<FirebaseFirestore> staticMockFirebase = Mockito.mockStatic(FirebaseFirestore.class)) {
            // Create event and set data
            // Event is a spy so we mock out UI and android functions as needed (e.g. notifyObservers)
            Event testEvent = Mockito.spy(new Event("0", mockFirestore));

            // Mock notify observers since it relies on android
            doAnswer((invocation -> {
                return null;
            })).when(testEvent).notifyObservers();

            // Call fetchData
            testEvent.fetchData();

            // Check that event fields were set correctly
            HashMap<String, Object> testData = getMockData();
            String testTime = "10:30 AM";
            assertEquals(testEvent.getName(), testData.get("name"));
            assertEquals(testEvent.getFacility(), testData.get("facility"));
            assertEquals(testEvent.getTime12h(), testTime);
        }
    }

    /**
     * Tests parseEventDocument.
     */
    @Test
    public void testParseEventDocument() {
        try (MockedStatic<FirebaseFirestore> staticMockFirebase = Mockito.mockStatic(FirebaseFirestore.class)) {
            // Create event and set data
            // Event is a spy so we mock out UI and android functions as needed (e.g. notifyObservers)
            Event testEvent = Mockito.spy(new Event("0", mockFirestore));

            // Mock notify observers since it relies on android
            doAnswer((invocation -> {
                return null;
            })).when(testEvent).notifyObservers();

            // Call fetchData
            testEvent.parseEventDocument(getMockData());

            // Check that event fields were set correctly
            HashMap<String, Object> testData = getMockData();
            String testTime = "10:30 AM";
            assertEquals(testEvent.getName(), testData.get("name"));
            assertEquals(testEvent.getFacility(), testData.get("facility"));
            assertEquals(testEvent.getTime12h(), testTime);
        }
    }

    /**
     * Tests leaveWaitlist().
     */
    @Test
    public void testLeaveWaitlist() {
        try (MockedStatic<FirebaseFirestore> staticMockFirebase = Mockito.mockStatic(FirebaseFirestore.class)) {
            staticMockFirebase.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            // Create event and set data
            // Event is a spy so we mock out UI and android functions as needed (e.g. notifyObservers)
            Event testEvent = Mockito.spy(new Event("0", mockFirestore));

            // Mock notify observers since it relies on android
            doAnswer((invocation -> {
                return null;
            })).when(testEvent).notifyObservers();

            testEvent.setName("Swimming Lesson");
            testEvent.setFacility("Swimming Centre");
            testEvent.setOrganizerDeviceId("abcd1234");
            testEvent.setTime(18, 0);

            testEvent.getWaitList().add("4910");

            assertEquals(testEvent.getWaitList().size(), 1);
            assertEquals(testEvent.getWaitList().get(0), "4910");

            testEvent.leaveWaitList("4910");

            assertEquals(testEvent.getWaitList().size(), 0);
        }
    }

    /**
     * Tests leaveInviteeList().
     */
    @Test
    public void testLeaveInviteeList() {
        try (MockedStatic<FirebaseFirestore> staticMockFirebase = Mockito.mockStatic(FirebaseFirestore.class)) {
            staticMockFirebase.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            // Create event and set data
            // Event is a spy so we mock out UI and android functions as needed (e.g. notifyObservers)
            Event testEvent = Mockito.spy(new Event("0", mockFirestore));

            // Mock notify observers since it relies on android
            doAnswer((invocation -> {
                return null;
            })).when(testEvent).notifyObservers();

            testEvent.setName("Swimming Lesson");
            testEvent.setFacility("Swimming Centre");
            testEvent.setOrganizerDeviceId("abcd1234");
            testEvent.setTime(18, 0);

            testEvent.getInviteeList().add("1234");

            assertEquals(testEvent.getInviteeList().size(), 1);
            assertEquals(testEvent.getInviteeList().get(0), "1234");

            testEvent.leaveInviteeList("1234");

            assertEquals(testEvent.getInviteeList().size(), 0);
        }
    }

    /**
     * Tests joinAttendeeList().
     */
    @Test
    public void testJoinAttendeeList() {
        try (MockedStatic<FirebaseFirestore> staticMockFirebase = Mockito.mockStatic(FirebaseFirestore.class)) {
            staticMockFirebase.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            // Create event and set data
            // Event is a spy so we mock out UI and android functions as needed (e.g. notifyObservers)
            Event testEvent = Mockito.spy(new Event("0", mockFirestore));

            // Mock notify observers since it relies on android
            doAnswer((invocation -> {
                return null;
            })).when(testEvent).notifyObservers();

            testEvent.setName("Swimming Lesson");
            testEvent.setFacility("Swimming Centre");
            testEvent.setOrganizerDeviceId("abcd1234");
            testEvent.setTime(18, 0);

            testEvent.joinAttendeeList("8204");

            assertEquals(testEvent.getAttendeeList().size(), 1);
            assertEquals(testEvent.getAttendeeList().get(0), "8204");
        }
    }

    /**
     * Tests leaveInviteeList().
     */
    @Test
    public void testLeaveAttendeeList() {
        try (MockedStatic<FirebaseFirestore> staticMockFirebase = Mockito.mockStatic(FirebaseFirestore.class)) {
            staticMockFirebase.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            // Create event and set data
            // Event is a spy so we mock out UI and android functions as needed (e.g. notifyObservers)
            Event testEvent = Mockito.spy(new Event("0", mockFirestore));

            // Mock notify observers since it relies on android
            doAnswer((invocation -> {
                return null;
            })).when(testEvent).notifyObservers();

            testEvent.setName("Swimming Lesson");
            testEvent.setFacility("Swimming Centre");
            testEvent.setOrganizerDeviceId("abcd1234");
            testEvent.setTime(18, 0);

            testEvent.getAttendeeList().add("1234");

            assertEquals(testEvent.getAttendeeList().size(), 1);
            assertEquals(testEvent.getAttendeeList().get(0), "1234");

            testEvent.leaveAttendeeList("1234");

            assertEquals(testEvent.getAttendeeList().size(), 0);
        }
    }

    /**
     * Tests joinCancelledList().
     */
    @Test
    public void testJoinCancelledList() {
        try (MockedStatic<FirebaseFirestore> staticMockFirebase = Mockito.mockStatic(FirebaseFirestore.class)) {
            staticMockFirebase.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            // Create event and set data
            // Event is a spy so we mock out UI and android functions as needed (e.g. notifyObservers)
            Event testEvent = Mockito.spy(new Event("0", mockFirestore));

            // Mock notify observers since it relies on android
            doAnswer((invocation -> {
                return null;
            })).when(testEvent).notifyObservers();

            testEvent.setName("Swimming Lesson");
            testEvent.setFacility("Swimming Centre");
            testEvent.setOrganizerDeviceId("abcd1234");
            testEvent.setTime(18, 0);

            testEvent.joinCancelledList("4892");

            assertEquals(testEvent.getCancelledList().size(), 1);
            assertEquals(testEvent.getCancelledList().get(0), "4892");
        }
    }

    /**
     * Tests onWaitlist().
     */
    @Test
    public void testOnWaitlist() {
        try (MockedStatic<FirebaseFirestore> staticMockFirebase = Mockito.mockStatic(FirebaseFirestore.class)) {
            staticMockFirebase.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            // Create event and set data
            // Event is a spy so we mock out UI and android functions as needed (e.g. notifyObservers)
            Event testEvent = Mockito.spy(new Event("0", mockFirestore));

            // Mock notify observers since it relies on android
            doAnswer((invocation -> {
                return null;
            })).when(testEvent).notifyObservers();

            testEvent.setName("Swimming Lesson");
            testEvent.setFacility("Swimming Centre");
            testEvent.setOrganizerDeviceId("abcd1234");
            testEvent.setTime(18, 0);

            assertFalse(testEvent.onWaitList("4910"));

            testEvent.getWaitList().add("4910");

            assertTrue(testEvent.onWaitList("4910"));
        }
    }

    /**
     * Tests onInviteeList().
     */
    @Test
    public void testOnInviteeList() {
        try (MockedStatic<FirebaseFirestore> staticMockFirebase = Mockito.mockStatic(FirebaseFirestore.class)) {
            staticMockFirebase.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            // Create event and set data
            // Event is a spy so we mock out UI and android functions as needed (e.g. notifyObservers)
            Event testEvent = Mockito.spy(new Event("0", mockFirestore));

            // Mock notify observers since it relies on android
            doAnswer((invocation -> {
                return null;
            })).when(testEvent).notifyObservers();

            testEvent.setName("Swimming Lesson");
            testEvent.setFacility("Swimming Centre");
            testEvent.setOrganizerDeviceId("abcd1234");
            testEvent.setTime(18, 0);

            assertFalse(testEvent.onInviteeList("4910"));

            testEvent.getInviteeList().add("4910");

            assertTrue(testEvent.onInviteeList("4910"));
        }
    }

    /**
     * Tests onAttendeeList().
     */
    @Test
    public void testOnAttendeeList() {
        try (MockedStatic<FirebaseFirestore> staticMockFirebase = Mockito.mockStatic(FirebaseFirestore.class)) {
            staticMockFirebase.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            // Create event and set data
            // Event is a spy so we mock out UI and android functions as needed (e.g. notifyObservers)
            Event testEvent = Mockito.spy(new Event("0", mockFirestore));

            // Mock notify observers since it relies on android
            doAnswer((invocation -> {
                return null;
            })).when(testEvent).notifyObservers();

            testEvent.setName("Swimming Lesson");
            testEvent.setFacility("Swimming Centre");
            testEvent.setOrganizerDeviceId("abcd1234");
            testEvent.setTime(18, 0);

            assertFalse(testEvent.onAttendeeList("4910"));

            testEvent.getAttendeeList().add("4910");

            assertTrue(testEvent.onAttendeeList("4910"));
        }
    }

    /**
     * Tests onCancelledList().
     */
    @Test
    public void testOnCancelledList() {
        try (MockedStatic<FirebaseFirestore> staticMockFirebase = Mockito.mockStatic(FirebaseFirestore.class)) {
            staticMockFirebase.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            // Create event and set data
            // Event is a spy so we mock out UI and android functions as needed (e.g. notifyObservers)
            Event testEvent = Mockito.spy(new Event("0", mockFirestore));

            // Mock notify observers since it relies on android
            doAnswer((invocation -> {
                return null;
            })).when(testEvent).notifyObservers();

            testEvent.setName("Swimming Lesson");
            testEvent.setFacility("Swimming Centre");
            testEvent.setOrganizerDeviceId("abcd1234");
            testEvent.setTime(18, 0);

            assertFalse(testEvent.onCancelledList("4910"));

            testEvent.getCancelledList().add("4910");

            assertTrue(testEvent.onCancelledList("4910"));
        }
    }

    /**
     * Tests sampleEntrantsFromWaitlist()
     */
    @Test
    public void testSampleEntrantsFromWaitlist() {
        try (MockedStatic<FirebaseFirestore> staticMockFirebase = Mockito.mockStatic(FirebaseFirestore.class)) {
            staticMockFirebase.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            // Create event and set data
            // Event is a spy so we mock out UI and android functions as needed (e.g. notifyObservers)
            Event testEvent = Mockito.spy(new Event("0", mockFirestore));

            // Mock notify observers since it relies on android
            doAnswer((invocation -> {
                return null;
            })).when(testEvent).notifyObservers();

            testEvent.setName("Swimming Lesson");
            testEvent.setFacility("Swimming Centre");
            testEvent.setOrganizerDeviceId("abcd1234");
            testEvent.setTime(18, 0);
            testEvent.setAttendeeLimit(1);

            testEvent.getWaitList().add("4910");
            testEvent.getWaitList().add("1234");

            assertEquals(testEvent.getWaitList().size(), 2);

            testEvent.sampleEntrantsFromWaitList();

            assertEquals(testEvent.getWaitList().size(), 1);
            assertEquals(testEvent.getInviteeList().size(), 1);
        }
    }

    /**
     * Tests drawEntrantFromWaitlist() with one entrant in waitlist.
     */
    @Test
    public void testDrawEntrantFromWaitlist() {
        try (MockedStatic<FirebaseFirestore> staticMockFirebase = Mockito.mockStatic(FirebaseFirestore.class)) {
            staticMockFirebase.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            // Create event and set data
            // Event is a spy so we mock out UI and android functions as needed (e.g. notifyObservers)
            Event testEvent = Mockito.spy(new Event("0", mockFirestore));

            // Mock notify observers since it relies on android
            doAnswer((invocation -> {
                return null;
            })).when(testEvent).notifyObservers();

            testEvent.setName("Swimming Lesson");
            testEvent.setFacility("Swimming Centre");
            testEvent.setOrganizerDeviceId("abcd1234");
            testEvent.setTime(18, 0);

            testEvent.getWaitList().add("1");

            assertEquals(testEvent.drawEntrantFromWaitList(), "1");
        }
    }

    /**
     * Tests drawEntrantFromWaitlist() with no entrants in waitlist.
     */
    @Test
    public void testDrawEntrantFromEmtpyWaitlist() {
        try (MockedStatic<FirebaseFirestore> staticMockFirebase = Mockito.mockStatic(FirebaseFirestore.class)) {
            staticMockFirebase.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            // Create event and set data
            // Event is a spy so we mock out UI and android functions as needed (e.g. notifyObservers)
            Event testEvent = Mockito.spy(new Event("0", mockFirestore));

            // Mock notify observers since it relies on android
            doAnswer((invocation -> {
                return null;
            })).when(testEvent).notifyObservers();

            testEvent.setName("Swimming Lesson");
            testEvent.setFacility("Swimming Centre");
            testEvent.setOrganizerDeviceId("abcd1234");
            testEvent.setTime(18, 0);

            assertEquals(testEvent.drawEntrantFromWaitList(), null);
        }
    }
}
