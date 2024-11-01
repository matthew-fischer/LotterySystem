package com.example.luckydragon;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Consumer;

import kotlin.jvm.Transient;

public class UserController implements Serializable {
    // "transient" means that db will be omitted from serialization
    transient private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getUser(String deviceID, Consumer<Map<String, Object>> onSuccess) {
        // Get user data
        DocumentReference docRef = db.collection("users").document(deviceID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot userDocument = task.getResult();
                    if(userDocument.exists()) {
                        Map<String, Object> userData = userDocument.getData();
                        userData.put("DeviceID", deviceID);
                        onSuccess.accept(userData);
                    } else {
                        // TODO: start signup activity
                    }
                } else {
                    throw new RuntimeException("Database read failed.");
                }
            }
        });
    }
}
