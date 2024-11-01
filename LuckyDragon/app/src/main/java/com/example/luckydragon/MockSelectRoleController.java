package com.example.luckydragon;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MockSelectRoleController extends SelectRoleController {
    Map<String, Object> mockUserData;

    public MockSelectRoleController(User observable, Map<String, Object> mockUserData) {
        super(observable);
        this.mockUserData = mockUserData;
    }
}
//    @Override
//    public void fetchUserData() {
//        getObservable().setData(mockUserData);
//    }
//
//public class FakeDb extends FirebaseFirestore {
//    @Override
//}
