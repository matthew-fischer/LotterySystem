package com.example.luckydragon;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Consumer;

public class SelectRoleController extends Controller {
    Map<String, Object> userData;

    public SelectRoleController(User observable) {
        super(observable);
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }

    public Map<String,Object> getUserData() {
        return userData;
    }
}
