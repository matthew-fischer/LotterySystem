package com.example.luckydragon;

/**
 * Represents an Entrant object.
 * <p>
 * Issues:
 *   - Currently Entrant does not add any functionality to User.
 *      I expect we will have Entrant-specific methods later on so I created this class.
 *   - Email and phone number may be optional. Additional constructors should be defined for these cases.
 */
public class Entrant extends User {
    /**
     * Creates an Entrant object with empty string values and a deviceID.
     * @param deviceID: the user's device ID
     */
    public Entrant(String deviceID) {
        super(deviceID);
    }

    /**
     * Creates an Entrant object from an existing User.
     * @param user the existing user
     */
    public Entrant(User user) {
        super(user);
    }
}
