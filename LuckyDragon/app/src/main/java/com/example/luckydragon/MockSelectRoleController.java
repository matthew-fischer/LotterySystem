package com.example.luckydragon;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MockSelectRoleController extends SelectRoleController {
    Map<String, Object> mockUserData;

    public MockSelectRoleController(User observable, Map<String, Object> mockUserData) {
        super(observable);
        this.mockUserData = mockUserData;
    }

    @Override
    public void fetchUserData() {
        getObservable().setData(mockUserData);
    }
}
