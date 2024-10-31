package com.example.luckydragon;

import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupController extends Controller {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public SignupController(User observable) {
        super(observable);
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }

    public void extractFields(EditText editName, EditText editEmail, EditText editPhone) {
        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        String phoneNumber = editPhone.getText().toString();

        // TODO: input validation
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("name", name);
        data.put("email", email);
        data.put("phoneNumber", phoneNumber);
        data.put("isEntrant", true);
        getObservable().setData(data);
    }

    public void saveUserData() {
        Log.d("DB", "saveUserData");
        db
                .collection("users")
                .document(getObservable().getDeviceID())
                .set(getObservable().getUserData());
    }
}
