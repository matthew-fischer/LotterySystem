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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.After;
import org.junit.Before;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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

    protected String eventId = "fakeEventId";
    protected abstract Map<String, Object> getMockData();
    protected abstract Map<String, Object> getMockEventData();

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
        when(mockUserDocumentSnapshot.getData()).thenReturn(getMockData());

        // Set up event mocking
        // We don't want to save anything to the database, so we mock the methods that save an event to the db to do nothing
        // We also mock getId() to return "mockEventID" instead of going to the database for an id
        when(mockFirestore.collection("events")).thenReturn(mockEventsCollection);
        // Get a specific event stuff
        when(mockEventsCollection.document()).thenReturn(mockEventDocument);
        when(mockEventsCollection.document(anyString())).thenReturn(mockEventDocument);
        when(mockEventDocument.getId()).thenReturn(eventId);
        when(mockEventDocument.get()).thenReturn(mockEventTask);
        // when set is called, we want to do nothing TODO: might have to extend set if we check mock db in tests
        when(mockEventDocument.set(anyMap())).thenReturn(mockVoidTask);
        DocumentSnapshot mockEventDocumentSnapshot = mock(DocumentSnapshot.class);
        when(mockEventTask.addOnCompleteListener(any(OnCompleteListener.class))).thenAnswer((invocation) -> {
            OnCompleteListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onComplete(mockEventTask);
            return mockEventTask;
        });
        when(mockEventTask.isSuccessful()).thenReturn(true);
        when(mockEventTask.getResult()).thenReturn(mockEventDocumentSnapshot);
        when(mockEventDocumentSnapshot.getData()).thenReturn(getMockEventData());
        when(mockEventDocumentSnapshot.exists()).thenReturn(true);

        // Get a collection of events stuff
        when(mockEventsCollection.get()).thenReturn(mockEventQueryTask);
        when(mockEventsCollection.whereEqualTo(anyString(), any())).thenReturn(mockEventQuery);  // note: not sure where is called
        when(mockEventQueryTask.addOnCompleteListener(any(OnCompleteListener.class)))
                .thenAnswer(invocation -> {
                    OnCompleteListener<QuerySnapshot> listener = invocation.getArgument(0);
                    listener.onComplete(mockEventQueryTask);
                    return mockEventQueryTask;
                });

        QuerySnapshot mockEventQuerySnapshot = mock(QuerySnapshot.class);  // list of QueryDocumentSnapshots
        when(mockEventQueryTask.getResult()).thenReturn(mockEventQuerySnapshot);
        Iterator<QueryDocumentSnapshot> mockEventQueryDocumentSnapshotIter = mock(Iterator.class);
        when(mockEventQuerySnapshot.iterator()).thenReturn(mockEventQueryDocumentSnapshotIter);
        when(mockEventQueryDocumentSnapshotIter.hasNext()).thenReturn(true, false);  // true for the first call, false next time
        QueryDocumentSnapshot mockEventQueryDocumentSnapshot = mock(QueryDocumentSnapshot.class);
        when(mockEventQueryDocumentSnapshotIter.next()).thenReturn(mockEventQueryDocumentSnapshot);
        when(mockEventQueryDocumentSnapshot.getData()).thenReturn(getMockEventData());
        when(mockEventQueryDocumentSnapshot.getId()).thenReturn("fakeEventId");
        // in Organizer.fetchData(), we don't want to pull events from db
        when(mockEventQueryTask.isSuccessful()).thenReturn(true);
        when(mockEventQuery.get()).thenReturn(mockEventQueryTask);

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
