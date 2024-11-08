/*
 * Contains unit tests for the User model class.
 * ISSUES:
 *   NONE
 */

package com.example.luckydragon;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a collection of unit tests for the User class.
 * These tests should not use the database. The database is mocked using Mockito.
 * The User class is closely linked to the database and uses the Android framework so it is not well-suited for unit testing.
 * We test what we can here. Database and android methods must be mocked out. See the comment just below for more details.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserTest {
    /*
     * Profile pictures use android.graphics.Bitmap which is android specific and can't be used for these local unit tests.
     * For that reason, generateProfilePicture(String), bitmapToString(Bitmap), stringToBitmap(String) are not tested here
     * Since User uses the database, database mocking is required.
     * Since notifyObservers() uses android, it must be mocked in tests where it is called. See testFetchData() for an example
     */

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
    private Task<DocumentSnapshot> mockEventTask;
    @Mock
    private Query mockEventQuery;
    @Mock
    private Task<QuerySnapshot> mockEventQueryTask;

    Map<String, Object> capturedUserData;

    // Mock organizer with an existing facility
    private HashMap<String, Object> getMockData() {
        // Define test user
        HashMap<String, Object> testUserData = new HashMap<>();
        // Personal info
        testUserData.put("name", "John Doe");
        testUserData.put("email", "jdoe@ualberta.ca");
        testUserData.put("phoneNumber", "780-831-3291");
        // Roles
        testUserData.put("isEntrant", true);
        testUserData.put("isOrganizer", true);
        testUserData.put("isAdmin", false);

        return testUserData;
    }

    @BeforeEach
    public void setup() {
        openMocks(this);

        // Set up user mocking
        when(mockFirestore.collection("users")).thenReturn(mockUsersCollection);
        when(mockUsersCollection.document(anyString())).thenReturn(mockUserDocument);
        when(mockUserDocument.get()).thenReturn(mockUserTask);
        when(mockUserDocument.set(any(Map.class))).thenAnswer((invocation) -> {
            System.out.println("CAUGHT SET");
            capturedUserData = invocation.getArgument(0);
            return mockVoidTask;
        });
        //when(mockUserDocument.set(any(Map.class))).thenReturn(mockVoidTask);
        when(mockUserTask.addOnFailureListener(any(OnFailureListener.class))).thenReturn(mockUserTask);
        doAnswer(invocation -> {
            OnSuccessListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onSuccess(mockUserDocumentSnapshot);
            return mockUserTask;
        }).when(mockUserTask).addOnSuccessListener(any(OnSuccessListener.class));
        when(mockUserDocumentSnapshot.getData()).thenReturn(getMockData());
        // Set up event mocking
        // We don't want to save anything to the database, so we mock the methods that save an event to the db to do nothing
        // We also mock getId() to return "mockEventID" instead of going to the database for an id
        when(mockFirestore.collection("events")).thenReturn(mockEventsCollection);
        when(mockEventsCollection.document()).thenReturn(mockEventDocument);
        when(mockEventDocument.getId()).thenReturn("mockEventID");

        when(mockEventsCollection.document(anyString())).thenReturn(mockEventDocument);
        when(mockEventDocument.get()).thenReturn(mockEventTask);
        // when set is called, we want to do nothing
        when(mockEventDocument.set(anyMap())).thenReturn(mockVoidTask);
        // in the on complete listener, we want to do nothing
        when(mockEventTask.addOnCompleteListener(any())).thenAnswer((invocation) -> {
            return null; // do nothing
        });
        // in Organizer.fetchData(), we don't want to pull events from db
        when(mockEventsCollection
                .whereEqualTo(anyString(), any()))
                .thenReturn(mockEventQuery);
        when(mockEventQuery.get()).thenReturn(mockEventQueryTask);
        when(mockEventQueryTask.addOnCompleteListener(any()))
                .thenAnswer((invocation -> {
                    return null; // do nothing
                }));
    }

    /**
     * Tests that fetchData() correctly sets user fields.
     */
    @Test
    public void testFetchData() {
        User testUser = Mockito.spy(new User("abcd1234", mockFirestore));

        // Mock notify observers since it relies on android
        doAnswer((invocation -> {
            return null;
        })).when(testUser).notifyObservers();

        // Call fetchData
        testUser.fetchData();

        // Check that user fields were set correctly
        Map<String, Object> mockData = getMockData();
        assertEquals(testUser.getName(), mockData.get("name"));
        assertEquals(testUser.getEmail(), mockData.get("email"));
        assertEquals(testUser.getPhoneNumber(), mockData.get("phoneNumber"));
        assertEquals(testUser.isEntrant(), mockData.get("isEntrant"));
        assertEquals(testUser.isOrganizer(), mockData.get("isOrganizer"));
        assertEquals(testUser.isAdmin(), mockData.get("isAdmin"));
    }

    /**
     * Tests the buildUserFromMap correctly sets user fields.
     */
    @Test
    public void testBuildUserFromMap() {
        User testUser = new User("abcd1234", mockFirestore);
        testUser.buildUserFromMap(getMockData());

        // Check that user fields set correctly
        Map<String, Object> mockData = getMockData();
        assertEquals(testUser.getName(), mockData.get("name"));
        assertEquals(testUser.getEmail(), mockData.get("email"));
        assertEquals(testUser.getPhoneNumber(), mockData.get("phoneNumber"));
        assertEquals(testUser.isEntrant(), mockData.get("isEntrant"));
        assertEquals(testUser.isOrganizer(), mockData.get("isOrganizer"));
        assertEquals(testUser.isAdmin(), mockData.get("isAdmin"));
    }

    /**
     * Tests that save() feeds the correct map to firestore set().
     * Firestore set() is mocked out, so nothing is actually saved to the database in this test.
     */
    @Test
    public void testSave() {
        User testUser = new User("abcd1234", mockFirestore);
        testUser.buildUserFromMap(getMockData());
        testUser.save();

        // Our mock of firestore set() above sets capturedUserData to the map that is passed into set()
        // So we can test here that capturedUserData is the correct map of user info to save

        Map<String, Object> mockData = getMockData();
        assertEquals(capturedUserData.get("name"), mockData.get("name"));
        assertEquals(capturedUserData.get("email"), mockData.get("email"));
        assertEquals(capturedUserData.get("phoneNumber"), mockData.get("phoneNumber"));
        assertEquals(capturedUserData.get("isEntrant"), mockData.get("isEntrant"));
        assertEquals(capturedUserData.get("isOrganizer"), mockData.get("isOrganizer"));
        assertEquals(capturedUserData.get("isAdministrator"), mockData.get("isAdministrator"));
    }
}
