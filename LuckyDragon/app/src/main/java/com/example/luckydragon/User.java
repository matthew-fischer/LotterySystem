/**
 * Defines the User model class.
 */

package com.example.luckydragon;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

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
public class User extends Observable implements Serializable {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String deviceId;
    private String name;
    private String email;
    private String phoneNumber;
    private Bitmap profilePicture;

    private Boolean isLoaded = Boolean.FALSE;

    private Organizer organizer;
    private Entrant entrant;

    private Boolean isAdmin = Boolean.FALSE;
    public User(String deviceId) {
        super();
        this.deviceId = deviceId;
    }

    @Override
    public void notifyObservers() {
        super.notifyObservers();
//        save();
    }

    /**
     * Set user data from firestore
     */
    public void fetchData() {
        db.collection("users").document(getDeviceId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> userData = documentSnapshot.getData();
                    if (userData == null) {
                        // create new user with empty info
                        save();
                    } else {
                        email = String.format("%s", userData.get("email"));
                        name = String.format("%s", userData.get("name"));
                        phoneNumber = String.format("%s", userData.get("phoneNumber"));

                        boolean isEntrant = userData.get("isEntrant") != null
                                && userData.get("isEntrant").toString().equals("true");
                        if (isEntrant) {
                            entrant = new Entrant();
                        }
                        boolean isOrganizer = userData.get("isOrganizer") != null
                                && userData.get("isOrganizer").toString().equals("true");
                        if (isOrganizer) {
                            String facility = String.format("%s", userData.get("facility"));

                            if (facility != null) {
                                organizer = new Organizer(facility);
                            } else {
                                organizer = new Organizer();
                            }
                        }
                        isAdmin = userData.get("isAdmin") != null
                                && userData.get("isAdmin").toString().equals("true");

                    }


                    isLoaded = true;
                    notifyObservers();
                });
    }

    /**
     * Save to firestore
     */
    public void save() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("isEntrant", isEntrant());
        map.put("isOrganizer", isOrganizer());
        map.put("isAdmin", isAdmin());

        if (isOrganizer()) {
            map.put("facility", organizer.getFacility());
        }

        map.put("name", name);
        map.put("email", email);
        map.put("phoneNumber", phoneNumber);
        map.put("profilePicture", profilePicture);
        db.collection("users").document(deviceId)
                .set(map);
    }

    // TODO: Implement, send error messages
    public Boolean isValid() {
        return !name.isEmpty();
    }

    /**
     * Get the user's device ID.
     * @return user's device ID as String
     */
    public String getDeviceId() {
        return deviceId;
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
     * Gets the user's profile picture.
     * @return the user's profile picture
     */
    public Bitmap getProfilePicture() { return profilePicture; }

    /**
     * Set the user's name.
     * @param name: the user's new name
     */
    public void setName(String name) {
        this.name = name;
        notifyObservers();
    }

    /**
     * Set the user's email.
     * @param email: the user's new email
     */
    public void setEmail(String email) {
        this.email = email;
        notifyObservers();
    }

    /**
     * Set the user's phone number.
     * @param phoneNumber: the user's new phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        notifyObservers();
    }

    /**
     * Set the user's email.
     * @param profilePicture: the user's new email
     */
    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
        notifyObservers();
    }

    public Boolean isEntrant() {
        return entrant != null;
    }

    public void setEntrant(Boolean entrant) {
        if (entrant) {
            this.entrant = new Entrant();
        } else {
            this.entrant = null;
        }
        notifyObservers();
    }

    public Boolean isOrganizer() {
        return organizer != null;
    }

    @Nullable
    public Organizer getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Boolean organizer) {
        if (organizer) {
            this.organizer = new Organizer();
        } else {
            this.organizer = null;
        }
    }

    public Boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Boolean isLoaded() {
        return isLoaded;
    }
}
