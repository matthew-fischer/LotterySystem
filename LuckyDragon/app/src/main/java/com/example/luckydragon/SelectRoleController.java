package com.example.luckydragon;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class SelectRoleController extends Controller {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public SelectRoleController(User observable) {
        super(observable);
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }

    public void fetchUserData() {
        db
            .collection("users")
            .document(getObservable().getDeviceID())
            .get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> userData = documentSnapshot.getData();
                        assert userData != null;
                        getObservable().setData(userData);
//                        user = new Organizer(user, String.format("%s", userData.get("Facility")));
                    } else {
                        // Create a new document for this user
                        db.collection("users")
                                .document(getObservable().getDeviceID())
                                .set(getObservable().getUserData());
                    }
                }
            });
    }
}
