package com.example.luckydragon;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import android.content.Context;

import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

public abstract class MockedDb {
    @Mock
    protected FirebaseFirestore mockFirestore;
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
    @Mock
    private CollectionReference mockMessagesCollection;
    @Mock
    private DocumentReference mockMessagesDocument;
    @Mock
    private QuerySnapshot mockEventQuerySnapshot;

    protected EventListener<QuerySnapshot> eventListener;

    // need a map of id to data map
    protected HashMap<String, Map<String, Object>> events = new HashMap<>();

    protected String eventId = "fakeEventId";
    protected abstract Map<String, Object> getMockUserData();
    protected abstract Map<String, Object> getMockEventData();

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
        when(mockEventDocument.get()).thenReturn(mockEventTask);  // currently do not return the actual data
        when(mockEventDocument.set(anyMap())).thenAnswer((invocation) -> {
            Map<String, Object> eventData = invocation.getArgument(0);
            // cast ints to longs in the data
            for (Map.Entry<String, Object> entry : eventData.entrySet()) {
                if (entry.getValue() instanceof Integer) {
                    eventData.put(entry.getKey(), Long.valueOf((Integer) entry.getValue()));
                }
            }

            events.put(id, eventData);
            eventListener.onEvent(mockEventQuerySnapshot, null);
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

    @Before
    public void setup() {
        Intents.init();
        openMocks(this);
        // Set up user mocking
        when(mockFirestore.collection("users")).thenReturn(mockUsersCollection);
        when(mockUsersCollection.document(anyString())).thenReturn(mockUserDocument);
        when(mockUserDocument.get()).thenReturn(mockUserTask);
        when(mockUserDocument.set(any(Map.class))).thenReturn(mockVoidTask);
        when(mockUserTask.addOnFailureListener(any(OnFailureListener.class))).thenReturn(mockUserTask);
        doAnswer(invocation -> {
            OnSuccessListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onSuccess(mockUserDocumentSnapshot);
            return mockUserTask;
        }).when(mockUserTask).addOnSuccessListener(any(OnSuccessListener.class));
        when(mockUserDocumentSnapshot.getData()).thenReturn(getMockUserData());

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
