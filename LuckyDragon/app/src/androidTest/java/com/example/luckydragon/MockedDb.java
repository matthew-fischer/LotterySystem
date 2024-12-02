package com.example.luckydragon;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.luckydragon.Activities.SelectRoleActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.After;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class MockedDb {
    @Mock
    protected FirebaseFirestore mockFirestore;
    @Mock
    private Task<Void> mockVoidTask;

    // User mocks
    @Mock
    private CollectionReference mockUsersCollection;
    @Mock
    private Query mockUserQuery;
    @Mock
    private Task<QuerySnapshot> mockUserQueryTask;
    @Mock
    private QuerySnapshot mockUserQuerySnapshot;

    // Event mocks
    @Mock
    private CollectionReference mockEventsCollection;
    @Mock
    private Query mockEventQuery;
    @Mock
    private Task<QuerySnapshot> mockEventQueryTask;
    @Mock
    private QuerySnapshot mockEventQuerySnapshot;

    // Message mocks
    @Mock
    private CollectionReference mockMessagesCollection;
    @Mock
    private DocumentReference mockMessagesDocument;


    protected EventListener<QuerySnapshot> eventListener;
    protected EventListener<QuerySnapshot> userListener;

    // need a map of id to data map
    protected HashMap<String, Map<String, Object>> events = new HashMap<>();
    protected HashMap<String, Map<String, Object>> users = new HashMap<>();

    protected String eventId = "fakeEventId";  // what is this for?
    protected String deviceId = "fakeDeviceId";

    protected abstract Map<String, Object> getMockUserData();
    protected void loadMockEventData(Map<String, Map<String, Object>> events) {}
    protected void loadMockUserData(Map<String, Map<String, Object>> users) {
        // insert the device user
//        String id = String.valueOf(new Random().nextInt());

        users.put(deviceId, getMockUserData());
    }

    protected DocumentReference makeEventDocumentSnapshot(String id) {
        Map<String, Object> data = events.get(id);
        if (data == null) {
            // generate a new event
            events.put(id, new HashMap<>());
        }

        DocumentReference mockEventDocument = mock(DocumentReference.class);
        Task<DocumentSnapshot> mockEventTask = mock(Task.class);
        DocumentSnapshot mockEventDocumentSnapshot = mock(DocumentSnapshot.class);

        when(mockEventDocument.getId()).thenReturn(id);
        when(mockEventDocument.get()).thenReturn(mockEventTask);
        when(mockEventDocument.delete()).thenAnswer(invocation -> {
            events.remove(id);
            if (eventListener != null) {
                eventListener.onEvent(mockEventQuerySnapshot, null);
            }
            return mockVoidTask;
        });
        when(mockEventDocument.set(anyMap())).thenAnswer((invocation) -> {
            // Update the events map
            Map<String, Object> eventData = invocation.getArgument(0);
            // cast ints to longs in the data
            for (Map.Entry<String, Object> entry : eventData.entrySet()) {
                if (entry.getValue() instanceof Integer) {
                    eventData.put(entry.getKey(), Long.valueOf((Integer) entry.getValue()));
                }
            }

            events.put(id, eventData);
            // Only call the eventListener if it has been created
            if (eventListener != null) {
                eventListener.onEvent(mockEventQuerySnapshot, null);
            }
            return mockVoidTask;
        });
        when(mockEventTask.addOnCompleteListener(any(OnCompleteListener.class))).thenAnswer((invocation) -> {
            OnCompleteListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onComplete(mockEventTask);  // call the listener immediately
            return mockEventTask;
        });
        when(mockEventTask.isSuccessful()).thenReturn(true);
        when(mockEventTask.getResult()).thenReturn(mockEventDocumentSnapshot);

        when(mockEventDocumentSnapshot.getData()).thenReturn(events.get(id));  // should return the data map (problem might be not updating)
        when(mockEventDocumentSnapshot.exists()).thenReturn(true);

        return mockEventDocument;
    }

    protected Iterator<QueryDocumentSnapshot> makeEventsIter() {
        ArrayList<QueryDocumentSnapshot> list = new ArrayList<>();
        for (Map.Entry<String, Map<String, Object>> entry: events.entrySet()) {
            QueryDocumentSnapshot mockEventQueryDocumentSnapshot = mock(QueryDocumentSnapshot.class);
            when(mockEventQueryDocumentSnapshot.getData()).thenReturn(entry.getValue());
            when(mockEventQueryDocumentSnapshot.getId()).thenReturn(entry.getKey());

            list.add(mockEventQueryDocumentSnapshot);
        }

        return list.iterator();
    }

    protected DocumentReference makeUserDocumentSnapshot(String id) {
        Map<String, Object> data = users.get(id);
        if (data == null) {
            // generate a new User
            users.put(id, new HashMap<>());
        }

        DocumentReference mockUserDocument = mock(DocumentReference.class);
        Task<DocumentSnapshot> mockUserTask = mock(Task.class);
        DocumentSnapshot mockUserDocumentSnapshot = mock(DocumentSnapshot.class);

        when(mockUserDocument.getId()).thenReturn(id);
        when(mockUserDocument.get()).thenReturn(mockUserTask);
        when(mockUserDocument.set(anyMap())).thenAnswer((invocation) -> {
            // Update the users map
            Map<String, Object> eventData = invocation.getArgument(0);
            // cast ints to longs in the data
            for (Map.Entry<String, Object> entry : eventData.entrySet()) {
                if (entry.getValue() instanceof Integer) {
                    eventData.put(entry.getKey(), Long.valueOf((Integer) entry.getValue()));
                }
            }

            users.put(id, eventData);
            // only call the userListener if it has been created
            if (userListener != null) {
                userListener.onEvent(mockUserQuerySnapshot, null);
            }
            return mockVoidTask;
        });
        when(mockUserTask.addOnCompleteListener(any(OnCompleteListener.class))).thenAnswer((invocation) -> {
            OnCompleteListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onComplete(mockUserTask);  // call the listener immediately
            return mockUserTask;
        });
        when(mockUserTask.isSuccessful()).thenReturn(true);
        when(mockUserTask.getResult()).thenReturn(mockUserDocumentSnapshot);

        when(mockUserDocumentSnapshot.getData()).thenReturn(users.get(id));  // should return the data map (problem might be not updating)
        when(mockUserDocumentSnapshot.exists()).thenReturn(true);

        when(mockUserTask.addOnFailureListener(any(OnFailureListener.class))).thenReturn(mockUserTask);
        when(mockUserTask.addOnSuccessListener(any(OnSuccessListener.class))).thenAnswer(invocation -> {
            OnSuccessListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onSuccess(mockUserDocumentSnapshot);
            return mockUserTask;
        });

        return mockUserDocument;
    }

    protected Iterator<QueryDocumentSnapshot> makeUsersIter() {
        ArrayList<QueryDocumentSnapshot> list = new ArrayList<>();
        for (Map.Entry<String, Map<String, Object>> entry: users.entrySet()) {
            QueryDocumentSnapshot mockEventQueryDocumentSnapshot = mock(QueryDocumentSnapshot.class);
            when(mockEventQueryDocumentSnapshot.getData()).thenReturn(entry.getValue());
            when(mockEventQueryDocumentSnapshot.getId()).thenReturn(entry.getKey());

            list.add(mockEventQueryDocumentSnapshot);
        }

        return list.iterator();
    }

    @Before
    public void setup() {
        Intents.init();
        openMocks(this);

        // Call hook for adding events to db
        loadMockEventData(events);
        loadMockUserData(users);

        // Set up user mocking
        when(mockFirestore.collection("users")).thenReturn(mockUsersCollection);
        when(mockUsersCollection.document(anyString())).thenAnswer(invocation -> {
            String id = invocation.getArgument(0);  // assuming id exists in users map
            return makeUserDocumentSnapshot(id);
        });
        // Get a collection of users stuff
//        when(mockUsersCollection.whereEqualTo(anyString(), any())).thenReturn(mockUserQuery);  // note: not sure where is called
//        when(mockUserQuery.get()).thenReturn(mockUserQueryTask);
        when(mockUsersCollection.get()).thenReturn(mockUserQueryTask);

        when(mockUserQueryTask.addOnCompleteListener(any(OnCompleteListener.class)))
                .thenAnswer(invocation -> {
                    OnCompleteListener<QuerySnapshot> listener = invocation.getArgument(0);
                    listener.onComplete(mockUserQueryTask);
                    return mockUserQueryTask;
                });
        when(mockUserQueryTask.isSuccessful()).thenReturn(true);
        when(mockUserQueryTask.getResult()).thenReturn(mockUserQuerySnapshot);
        when(mockUserQuerySnapshot.iterator()).thenAnswer(invocation -> makeUsersIter());

        when(mockUsersCollection.addSnapshotListener(any(EventListener.class)))
                .thenAnswer((invocation) -> {
                    userListener = invocation.getArgument(0);  // capture snapshot listener
                    return null;
                });

        // Set up event mocking
        // We don't want to save anything to the database, so we mock the methods that save an event to the db to do nothing
        // We also mock getId() to return "mockEventID" instead of going to the database for an id
        when(mockFirestore.collection("events")).thenReturn(mockEventsCollection);
        // Get a specific event stuff
        when(mockEventsCollection.document()).thenAnswer(invocation -> {
            Random random = new Random();
            return makeEventDocumentSnapshot(String.valueOf(random.nextInt()));
        });
        when(mockEventsCollection.document(anyString())).thenAnswer(invocation -> {
            String id = invocation.getArgument(0);  // Assuming id is already in events
            return makeEventDocumentSnapshot(id);
        });

        // Get a collection of events stuff
        when(mockEventsCollection.whereEqualTo(anyString(), any())).thenReturn(mockEventQuery);  // note: not sure where is called
        when(mockEventsCollection.get()).thenReturn(mockEventQueryTask);
        when(mockEventQuery.get()).thenReturn(mockEventQueryTask);

        when(mockEventQueryTask.addOnCompleteListener(any(OnCompleteListener.class)))
                .thenAnswer(invocation -> {
                    OnCompleteListener<QuerySnapshot> listener = invocation.getArgument(0);
                    listener.onComplete(mockEventQueryTask);
                    return mockEventQueryTask;
                });
        when(mockEventQueryTask.isSuccessful()).thenReturn(true);
        when(mockEventQueryTask.getResult()).thenReturn(mockEventQuerySnapshot);
        when(mockEventQuerySnapshot.iterator()).thenAnswer(invocation -> makeEventsIter());

        when(mockEventsCollection.addSnapshotListener(any(EventListener.class)))
                .thenAnswer((invocation) -> {
                    // TODO: can only hold 1 eventListener at a time.
                    eventListener = invocation.getArgument(0);  // capture snapshot listener
                    return null;
                });

        // mock notifications db stuff
        when(mockFirestore.collection("messages")).thenReturn(mockMessagesCollection);
        when(mockMessagesCollection.document(any())).thenReturn(mockMessagesDocument);
        when(mockMessagesDocument.addSnapshotListener(any())).thenAnswer((invocation) -> {
            return null;
        });
        when(mockMessagesDocument.set(anyMap())).thenReturn(mockVoidTask);

        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
//        globalApp.setDb(mockFirestore);
        globalApp.setDeviceId(deviceId);  // always use a fake device id so we know the id for making events and such
    }

    @After
    public void tearDown() {
        // Reset global app state
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(null);
        globalApp.setUser(null);
        globalApp.resetState();

        Intents.release();
    }
}
