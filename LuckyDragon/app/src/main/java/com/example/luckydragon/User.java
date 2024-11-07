/**
 * Defines the User model class.
 */

package com.example.luckydragon;

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
 * <p>
 * Issues:
 *   - This is only a basic implementation. Additional functionality should be added as needed.
 *   - Email and phone number may be optional. Additional constructors should be defined for these cases.
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
    public User(String deviceId, FirebaseFirestore db) {
        super();
        this.db = db;
        this.deviceId = deviceId;
    }


    /**
     * User constructor for loading data in UserList
     * @param name
     * @param email
     * @param phoneNumber
     * @param defaultProfilePicture
     * @param profilePicture
     */
    public User(String name, String email, String phoneNumber, Bitmap defaultProfilePicture, Bitmap profilePicture) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.defaultProfilePicture = defaultProfilePicture;
        this.uploadedProfilePicture = profilePicture;
    }

    @Override
    public void notifyObservers() {
        super.notifyObservers();
        save();
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
                        name = userData.get("name") != null ? Objects.requireNonNull(userData.get("name")).toString() : null;
                        email = userData.get("email") != null ? Objects.requireNonNull(userData.get("email")).toString() : null;
                        phoneNumber = userData.get("phoneNumber") != null ? Objects.requireNonNull(userData.get("phoneNumber")).toString() : null;
                        assert(name != null);

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
                                organizer = new Organizer(deviceId, facility, this::notifyObservers);
                            } else {
                                organizer = new Organizer(deviceId, this::notifyObservers);
                            }
                            organizer.fetchEvents();
                        }
                        isAdmin = userData.get("isAdmin") != null
                                && userData.get("isAdmin").toString().equals("true");
                        uploadedProfilePicture = stringToBitmap((String)userData.get("profilePicture"));
                        defaultProfilePicture = stringToBitmap((String)userData.get("defaultProfilePicture"));
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
     * @return the user's profile picture if it exists, otherwise the default profile picture.
     */
    public Bitmap getProfilePicture() {
        if (uploadedProfilePicture != null) return uploadedProfilePicture;
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
     * Set the user's name.
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

    public void setNotifications(boolean enabled) {
        this.notifications = enabled;
        notifyObservers();
    }

    public boolean isNotified() {
        return notifications;
    }

    public void setUserProfilePicture(Bitmap profilePicture) {
        this.uploadedProfilePicture = profilePicture;
        notifyObservers();
    }

    public void setUploadedProfilePicture(Bitmap profilePicture) {
        this.uploadedProfilePicture = profilePicture;
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

    public Bitmap generateProfilePicture(String s) {
        assert !s.isEmpty();

        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Fill background with solid color
        Random rand = new Random(s.hashCode());
        float hue = rand.nextFloat() * 360;
        float saturation = (rand.nextInt(5000) + 1000) / 10000f;
        float luminance = 0.95f;

//        Log.d("COLORS", Float.toString(hue) + " " + Float.toString(saturation) + " " + Float.toString(luminance));
        Paint background = new Paint(Paint.ANTI_ALIAS_FLAG);
        background.setColor(Color.HSVToColor(new float[]{hue, saturation, luminance}));
        canvas.drawRect(0, 0, 100, 100, background);

        // Draw first letter of s on bitmap
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

    public Boolean isOrganizer() {
        return organizer != null;
    }

    @Nullable
    public Organizer getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Boolean organizer) {
        if (organizer) {
            this.organizer = new Organizer(deviceId, this::notifyObservers);
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

    public void setIsLoaded(Boolean newIsLoaded) {
        isLoaded = newIsLoaded;
        notifyObservers();
    }

    /**
     * Converts bitmap to string
     * @param image: the bitmap to convert to base 64 string
     * @return image encoded as string
     */
    private String bitmapToString(Bitmap image) {
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
        if (base64Str == null || base64Str.isEmpty()) return null;
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
