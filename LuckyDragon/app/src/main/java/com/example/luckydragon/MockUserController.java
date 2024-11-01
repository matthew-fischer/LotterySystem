package com.example.luckydragon;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Consumer;

/**
 * To run the onFailure callback for a test, another argument could be passed into the constructor to specify this behaviour.
 * - Ellis
 */

public class MockUserController extends UserController implements Serializable {
    Map<String, Object> mockUserData;

    MockUserController(Map<String, Object> mockUserData) {
        this.mockUserData = mockUserData;
    }

    /**
     * Runs the onSuccess callback on the mock user data.
     * @param deviceID user's device ID (not needed, but matches the function signature
     * @param onSuccess the onSuccess callback function
     */
    @Override
    public void getUser(String deviceID, Consumer<Map<String, Object>> onSuccess) {
        onSuccess.accept(mockUserData);
    }
}
