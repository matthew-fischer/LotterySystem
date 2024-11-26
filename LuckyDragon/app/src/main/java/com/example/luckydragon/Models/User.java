/**
 * Defines the User model class.
 * Stores all information about the application's user.
 * ISSUES:
 *   NONE
 */

package com.example.luckydragon.Models;

import static java.util.Objects.nonNull;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * Represents a User object.
 * User may act as an Entrant or Organizer (or both).
 * User has Entrant and Organizer attributes, which contain entrant-specific and organizer-specific information and functionality.
 * User is an Observable. Changes to User will cause views observing it to update.
 */
public class User extends Observable {
    private FirebaseFirestore db;
    private String deviceId;
    private String name = "";
    private String email = "";
    private String phoneNumber = "";
    private Boolean notifications = false;
    private Bitmap uploadedProfilePicture;
    private Bitmap defaultProfilePicture;
    private Boolean isLoaded = Boolean.FALSE;
    private Organizer organizer;
    private Entrant entrant;
    private Boolean isAdmin = Boolean.FALSE;

    /**
     * Creates a User.
     * @param deviceId the device ID of the user
     * @param db the database to use
     */
    public User(String deviceId, FirebaseFirestore db) {
        super();
        this.db = db;
        this.deviceId = deviceId;
    }

    /**
     * User constructor for loading data in UserList
     * @param name the name of the user
     * @param email the email of the user
     * @param phoneNumber the phone number of the user
     * @param defaultProfilePicture the default profile picture (Bitmap)
     * @param profilePicture the user's profile picture (Bitmap)
     */
    public User(String name, String email, String phoneNumber, Bitmap defaultProfilePicture, Bitmap profilePicture) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.defaultProfilePicture = defaultProfilePicture;
        this.uploadedProfilePicture = profilePicture;
    }

    /**
     * Notifies the user's observers and saves the user to the database.
     */
    @Override
    public void notifyObservers() {
        super.notifyObservers();
        save();
    }

    /**
     * Sets user data from the user stored in the database
     * Users are identified in the database by their unique device ID
     */
    public void fetchData() {
        Log.d("UserFetch", "Initiating Fetch");
        db.collection("users").document(getDeviceId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Log.d("UserFetch", "Completed Fetch");
                    Map<String, Object> userData = documentSnapshot.getData();
                    if (userData == null) {
                        save();
                    } else {
                        buildUserFromMap(userData);
                    }
                    isLoaded = true;
                    notifyObservers();
                });
    }

    /**
     * Saves the user to the database.
     * A logging message is printed in case of database save failure.
     */
    public void save() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("isEntrant", isEntrant());
        map.put("isOrganizer", isOrganizer());
        map.put("isAdmin", isAdmin());

        if (isOrganizer()) {
            map.put("facility", nonNull(organizer.getFacility()) ? organizer.getFacility() : null);
        }

        map.put("name", name);
        map.put("email", nonNull(email) && !email.isEmpty() ? email : null);
        map.put("phoneNumber", nonNull(phoneNumber) && !phoneNumber.isEmpty() ? phoneNumber : null);
        map.put("notifications", notifications);
        map.put("profilePicture", bitmapToString(uploadedProfilePicture));
        map.put("defaultProfilePicture", bitmapToString(defaultProfilePicture));
        db.collection("users").document(deviceId)
                .set(map).addOnFailureListener(e -> {
                    Log.e("SAVE DB", "fail");
                });
    }

    /**
     * Returns whether a user is valid.
     * A user is valid if their name is not empty.
     * @return true if user is valid, false if not
     */
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
     * @return the user's uploaded profile picture if is not null,
     * otherwise the default profile picture.
     */
    public Bitmap getProfilePicture() {
        if (uploadedProfilePicture != null) return uploadedProfilePicture;
        return defaultProfilePicture;
    }

    /**
     * Gets the user's default profile picture.
     * @return the user's default profile picture.
     */
    public Bitmap getDefaultProfilePicture() {
        return defaultProfilePicture;
    }

    /**
     * Gets the user's uploaded profile picture.
     * @return the user's uploaded profile picture
     */
    public Bitmap getUploadedProfilePicture() {
        return uploadedProfilePicture;
    }

    /**
     * Sets the user's name.
     * @param name: the user's new name
     */
    public void setName(String name) {
        this.name = name;

        // change default profile picture
        if (!name.isEmpty()) {
            this.defaultProfilePicture = generateProfilePicture(this.name);
        }

        notifyObservers();
    }

    /**
     * Sets the user's email.
     * @param email: the user's new email
     */
    public void setEmail(String email) {
        this.email = email;
        notifyObservers();
    }

    /**
     * Sets the user's phone number.
     * @param phoneNumber: the user's new phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        notifyObservers();
    }

    /**
     * Sets the user's notification status.
     * @param enabled true if notifications should be enabled, false if not
     */
    public void setNotifications(boolean enabled) {
        this.notifications = enabled;
        notifyObservers();
    }

    /**
     * Gets the user's notification status.
     * @return true if notifications are enabled, false if not
     */
    public boolean isNotified() {
        return notifications;
    }

    /**
     * Sets the user's profile picture.
     * @param profilePicture the new profile picture (Bitmap)
     */
    public void setUploadedProfilePicture(Bitmap profilePicture) {
        this.uploadedProfilePicture = profilePicture;
        notifyObservers();
    }

    /**
     * Gets whether a user is an entrant.
     * A user is an entrant if entrant is not null (all user's should have entrant capabilities).
     * They are only not an entrant if they have yet to create their profile, in which case entrant will be null.
     * @return true if they are an entrant, false otherwise
     */
    public Boolean isEntrant() {
        return entrant != null;
    }

    /**
     * Sets the user's entrant status.
     * entrant may be true or null. if the passed in boolean is false, entrant will be set to null
     * @param entrant the new entrant status
     */
    public void setEntrant(Boolean entrant) {
        if (entrant) {
            this.entrant = new Entrant();
        } else {
            this.entrant = null;
        }
        notifyObservers();
    }

    /**
     * Deterministically generates a profile picture using a string
     * @param s the string to generate the profile picture using
     * @return the profile picture as a Bitmap
     */
    public static Bitmap generateProfilePicture(String s) {
        if (s == null || s.isEmpty()) return null;

        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Fill background with solid color
        Random rand = new Random(s.hashCode());
        float hue = rand.nextFloat() * 360;
        float saturation = (rand.nextInt(5000) + 1000) / 10000f;
        float luminance = 0.95f;

        Paint background = new Paint(Paint.ANTI_ALIAS_FLAG);
        background.setColor(Color.HSVToColor(new float[]{hue, saturation, luminance}));
        canvas.drawRect(0, 0, 100, 100, background);

        // Draw first letter of string on bitmap
        String text = s.substring(0, 1);
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(70);
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width())/2;
        int y = (bitmap.getHeight() - bounds.height())/2 + bounds.height();
        canvas.drawText(text, x, y, textPaint);

        return bitmap;
    }

    /**
     * Gets whether a user is an organizer.
     * A user is an organizer if organizer is not null (all user's should have organizer capabilities).
     * They are only not an organizer if they have yet to create their profile, in which case organizer will be null.
     * @return true if they are an organizer, false otherwise
     */
    public Boolean isOrganizer() {
        return organizer != null;
    }

    /**
     * Gets whether a user is an organizer.
     * @return the boolean value of organizer
     */
    @Nullable
    public Organizer getOrganizer() {
        return organizer;
    }

    /**
     * Sets a user's organizer status.
     * @param organizer the new organizer status.
     */
    public void setOrganizer(Boolean organizer) {
        if (organizer) {
            this.organizer = new Organizer(deviceId, this::notifyObservers, db);
        } else {
            this.organizer = null;
        }
    }

    /**
     * Gets whether a user is an administrator.
     * @return true if they are an admin, false if not
     */
    public Boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Sets a user's admin status.
     * @param admin the new admin status
     */
    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    /**
     * Gets whether a user is loaded.
     * @return true if user is loaded, false if not
     */
    public Boolean isLoaded() {
        return isLoaded;
    }

    /**
     * Sets whether a user is loaded.
     * Notifies observers that the user is now loaded.
     * @param newIsLoaded the new load status
     */
    public void setIsLoaded(Boolean newIsLoaded) {
        isLoaded = newIsLoaded;
        notifyObservers();
    }

    /**
     * Converts bitmap to string
     * @param image: the bitmap to convert to base 64 string
     * @return image encoded as string
     */
    private static String bitmapToString(Bitmap image) {
        // reference: https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
        if (image == null) return "";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Converts base64 string to bitmap
     * @param base64Str: The base 64 string to convert to bitmap
     * @return base64Str decoded to bitmap
     */
    public static Bitmap stringToBitmap(String base64Str) {
        // reference: https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
        if (base64Str == null || base64Str.isEmpty()) return null;
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    /**
     * Sets user attributes based on a map of user data.
     * Used for loading user from database.
     * @param userData the map of user data
     */
    public void buildUserFromMap(Map<String, Object> userData) {
        name = userData.get("name") != null ? Objects.requireNonNull(userData.get("name")).toString() : null;
        email = userData.get("email") != null ? Objects.requireNonNull(userData.get("email")).toString() : null;
        phoneNumber = userData.get("phoneNumber") != null ? Objects.requireNonNull(userData.get("phoneNumber")).toString() : null;
        assert(name != null);

        notifications = userData.get("notifications") != null
                && userData.get("notifications").toString().equals("true");

        boolean isEntrant = userData.get("isEntrant") != null
                && userData.get("isEntrant").toString().equals("true");
        if (isEntrant) {
            entrant = new Entrant();
        }
        boolean isOrganizer = userData.get("isOrganizer") != null
                && userData.get("isOrganizer").toString().equals("true");
        if (isOrganizer) {
            String facility = userData.get("facility") != null ? Objects.requireNonNull(userData.get("facility")).toString() : null;

            if (facility != null) {
                organizer = new Organizer(deviceId, facility, this::notifyObservers, db);
            } else {
                organizer = new Organizer(deviceId, this::notifyObservers, db);
            }
        }
        isAdmin = userData.get("isAdmin") != null
                && userData.get("isAdmin").toString().equals("true");
        uploadedProfilePicture = stringToBitmap((String)userData.get("profilePicture"));
        defaultProfilePicture = stringToBitmap((String)userData.get("defaultProfilePicture"));
    }
}
