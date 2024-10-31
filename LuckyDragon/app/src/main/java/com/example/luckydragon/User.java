/**
 * Defines the User model class.
 */

package com.example.luckydragon;

import android.util.Log;
import android.widget.Button;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a User object.
 * <p>
 * Issues:
 *   - This is only a basic implementation. Additional functionality should be added as needed.
 *   - Email and phone number may be optional. Additional constructors should be defined for these cases.
 */
public class User implements Serializable {
    private String deviceID;
    private String name;
    private String email;
    private String phoneNumber;

    private Boolean isEntrant = Boolean.FALSE;
    private Boolean isOrganizer = Boolean.FALSE;
    private Boolean isAdmin = Boolean.FALSE;

    /**
     * Creates a User object with empty string values and a deviceID.
     * @param deviceID: the user's device ID
     */
    public User(String deviceID) {
        this.deviceID = deviceID;
        this.name = "";
        this.email = "";
        this.phoneNumber = "";
    }

    /**
     * Creates a User object based on an existing instance
     * @param user: the user
     */
    public User(User user) {
        this.deviceID = user.getDeviceID();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.isEntrant = user.isEntrant();
        this.isOrganizer = user.isOrganizer();
        this.isAdmin = user.isAdmin();
    }

    /**
     * Creates a User object.
     * @param deviceID: the user's device ID
     * @param name: the user's name
     * @param email: the user's email
     * @param phoneNumber: the user's phone number
     */
    public User(String deviceID, String name, String email, String phoneNumber) {
        this.deviceID = deviceID;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Set user data using a map from firestore
     * @param userData: the firestore map
     */
    public void setData(Map<String, Object> userData) {
        email = String.format("%s", userData.get("email"));
        name = String.format("%s", userData.get("name"));
        phoneNumber = String.format("%s", userData.get("phoneNumber"));

        isEntrant = userData.get("entrant") != null
                && userData.get("entrant").toString().equals("true");
        isOrganizer = userData.get("organizer") != null
                && userData.get("organizer").toString().equals("true");
        isAdmin = userData.get("admin") != null
                && userData.get("admin").toString().equals("true");
    }

    /**
     * Return user data for saving to firestore
     * @return hash map for saving to firestore
     */
    public HashMap<String, Object> getUserData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("entrant", isEntrant);
        map.put("organizer", isOrganizer);
        map.put("admin", isAdmin);
        map.put("name", name);
        map.put("email", email);
        map.put("phoneNumber", phoneNumber);
        return map;
    }

    /**
     * Get the user's device ID.
     * @return user's device ID as String
     */
    public String getDeviceID() {
        return deviceID;
    }

    /**
     * Get the user's name.
     * @return user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the user's email.
     * @return user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the user's phone number.
     * @return the user's phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set the user's name.
     * @param name: the user's new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the user's email.
     * @param email: the user's new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set the user's phone number.
     * @param phoneNumber: the user's new phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean isEntrant() {
        return isEntrant;
    }

    public void setEntrant(Boolean entrant) {
        isEntrant = entrant;
    }

    public Boolean isOrganizer() {
        return isOrganizer;
    }

    public void setOrganizer(Boolean organizer) {
        isOrganizer = organizer;
    }

    public Boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
