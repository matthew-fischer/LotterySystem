/**
 * Defines the Event model class and its inner class Time.
 */

package com.example.luckydragon;

import static java.util.Objects.nonNull;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents an Event object.
 * <p>
 * Issues:
 *   - could add another constructor for when waitlist limit is not specified (since it is optional)
 */
public class Event extends Observable {
    /**
     * Represents a time as hours and minutes in 24 hour time.
     * e.g. 8:30 pm would have hours = 20 and minutes = 30
     */
    private class Time {
        public Integer hours;
        public Integer minutes;

        public Time(Integer hours, Integer minutes) {
            this.hours = hours;
            this.minutes = minutes;
        }

        @NonNull
        @Override
        public String toString() {
            return String.format("%02d%02d", hours, minutes);
        }

        public String toString12h() {
            String AMorPM = hours <= 12 ? "AM" : "PM";
            if(hours > 12) hours -= 12;
            return String.format("%02d:%02d %s", hours, minutes, AMorPM);
        }
    }
    private transient FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String id;
    private String name = "";
    private String organizerName = "";
    private String organizerDeviceId = "";
    private String facility = "";
    private Integer waitListLimit = -1;
    private Integer attendeeLimit = -1;
    private String date = LocalDate.now().toString();
    private Time time = new Time(0, 0);
    private BitMatrix qrHash;
    private Bitmap qrCode;

    private List<String> waitList = new ArrayList<>();
    private List<String> inviteeList = new ArrayList<>();
    private List<String> attendeeList = new ArrayList<>();
    private List<String> cancelledList = new ArrayList<>();

    public Event() {
        id = db.collection("events").document().getId();
        qrHash = generateQRCode();
    }

    public Event(String id) {
        super();
        this.id = id;
        qrHash = generateQRCode();  // TODO: load from db
    }

    /**
     * Creates an Event object with organizer name and no id
     * @param name the name of the event
     * @param organizerName the name of the event organizer
     * @param organizerDeviceId the device ID of the event organizer
     * @param facility: the name of the event facility
     * @param waitListLimit: the waitlist limit of the event
     * @param attendeeLimit: the attendee limit of the event
     * @param date: the date of the event, as a string YY-MM-DD
     * @param timeHours: the hour time e.g. "8" for 8:30
     * @param timeMinutes: the minute time e.g. "30" for 8:30
     * @param waitList the waitlist for this event
     */
    public Event(String name, String organizerDeviceId, String organizerName, String facility, @Nullable Integer waitListLimit, Integer attendeeLimit, String date, Integer timeHours, Integer timeMinutes, List<String> waitList)  {
        this.name = name;
        this.organizerName = organizerName;
        this.organizerDeviceId = organizerDeviceId;
        this.facility = facility;
        this.waitListLimit = waitListLimit;
        this.attendeeLimit = attendeeLimit;
        this.date = date;
        this.time = new Time(timeHours, timeMinutes);
        this.qrHash = generateQRCode();
        this.waitList = waitList;
    }

    // TODO: Do we need 2 similar constructors?
    /**
     * Creates an Event object.
     * @param id the event id
     * @param name the name of the event
     * @param organizerDeviceId the device ID of the event organizer
     * @param facility: the name of the event facility
     * @param waitListLimit: the waitlist limit of the event
     * @param attendeeLimit: the attendee limit of the event
     * @param date: the date of the event, as a string YY-MM-DD
     * @param timeHours: the hour time e.g. "8" for 8:30
     * @param timeMinutes: the minute time e.g. "30" for 8:30
     */
    public Event(String id, String name, String organizerDeviceId, String facility, Integer waitListLimit, Integer attendeeLimit, String date, Integer timeHours, Integer timeMinutes)  {
        this.id = id;
        this.name = name;
        this.organizerDeviceId = organizerDeviceId;
        this.facility = facility;
        this.waitListLimit = waitListLimit;
        this.attendeeLimit = attendeeLimit;
        this.date = date;
        this.time = new Time(timeHours, timeMinutes);
        this.qrHash = generateQRCode();
        this.qrCode = createBitMap(this.qrHash);
    }


    @Override
    public void notifyObservers() {
        super.notifyObservers();
        save();
    }

    /**
     * Save to firestore
     */
    public void save() {
        if(!nonNull(organizerDeviceId)) {
            Log.e("Event", "Tried to save an event without organizerDeviceId");
            return;
        }
        if(!nonNull(facility)) {
            Log.e("Event", "Tried to save an event without a facility");
            return;
        }

        Map<String, Object> eventData = new HashMap<>();
        if(nonNull(name)) eventData.put("name", name);
        if(nonNull(organizerDeviceId) && !organizerDeviceId.isEmpty()) eventData.put("organizerDeviceId", organizerDeviceId);
        if(nonNull(facility) && !facility.isEmpty()) eventData.put("facility", facility);
        if(nonNull(waitListLimit)) eventData.put("waitListLimit", waitListLimit);
        if(nonNull(attendeeLimit)) eventData.put("attendeeLimit", attendeeLimit);
        if(nonNull(date) && !date.isEmpty()) eventData.put("date", date);
        if(nonNull(time.hours)) eventData.put("hours", time.hours);
        if(nonNull(time.minutes)) eventData.put("minutes", time.minutes);
        if(nonNull(qrHash)) eventData.put("hashedQR", qrHash.toString("1", "0"));
        eventData.put("waitList", waitList);
        eventData.put("inviteeList", inviteeList);
        eventData.put("attendeeList", attendeeList);
        eventData.put("cancelledList", cancelledList);

        if (id == null || id.isEmpty()) {
            throw new RuntimeException("Event id should not be empty!");
        }
        db.collection("events").document(id)
                .set(eventData).addOnFailureListener(e -> {
                    Log.e("SAVE DB", "event save fail");
                });
    }

    public void fetchData() {
        // TODO: Ensure null attr are ok to read
        DocumentReference docRef = db.collection("events").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isSuccessful()) {
                    throw new RuntimeException("Database read failed.");
                }
                DocumentSnapshot eventDocument = task.getResult();
                if (!eventDocument.exists()) {
                    throw new RuntimeException("Event Document does not exist.");
                }
                Map<String, Object> eventData = eventDocument.getData();
                if (eventData == null) {
                    throw new RuntimeException("Event has no data.");
                }
                name = (String) eventData.get("name");
                organizerDeviceId = (String) eventData.get("organizerDeviceId");
                facility = (String) eventData.get("facility");
                waitListLimit = (int) (long) eventData.get("waitListLimit");
                attendeeLimit = (int) (long) eventData.get("attendeeLimit");
                date = (String) eventData.get("date");
                time = new Time((int) (long) eventData.get("hours"), (int) (long) eventData.get("minutes"));
//                TODO: Decode qrHash
//                qrHash
                List<String> incWaitList = (List<String>) eventData.get("waitList");
                List<String> incInviteeList = (List<String>) eventData.get("inviteeList");
                List<String> incAttendeeList = (List<String>) eventData.get("attendeeList");
                List<String> incCancelledList = (List<String>) eventData.get("cancelledList");
                if (incWaitList != null) {
                    waitList = incWaitList;
                }
                if (incInviteeList != null) {
                    inviteeList = incInviteeList;
                }
                if (incAttendeeList != null) {
                    attendeeList = incAttendeeList;
                }
                if (incCancelledList != null) {
                    cancelledList = incCancelledList;
                }

                notifyObservers();
            }
        });
    }

    /**
     * Returns a random entrant's deviceID from the waitlist.
     * In the case that there is no one in the waitlist, returns null.
     * @return the deviceID of the randomly chosen entrant, or null if list is empty
     * TODO: Should be private method?
     */
    public String drawEntrantFromWaitList() {
        if (waitList.isEmpty()) {
            return null;
        }
        int randomIndex = (int) (Math.random() * waitList.size());

        return waitList.get(randomIndex);
    }

    /**
     * Samples entrants from the waitlist and moves them to the invitee list.
     * TODO: This should also notify the invited entrants.
     * TODO: Should move other entrants to cancelled list? - Tony
     */
    public void sampleEntrantsFromWaitList() {
        while (attendeeList.size() + inviteeList.size() < attendeeLimit && !waitList.isEmpty()) {
            String sampledEntrant = drawEntrantFromWaitList();
            inviteeList.add(sampledEntrant);
            waitList.remove(sampledEntrant);
        }
        notifyObservers();
    }

    /**
     * Checks if an Event is equal to another event.
     * @param o the other event to compare to
     * @return True if they are equal, False if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(name, event.name) && Objects.equals(facility, event.facility) && Objects.equals(waitListLimit, event.waitListLimit) && Objects.equals(attendeeLimit, event.attendeeLimit) && Objects.equals(date, event.date) && Objects.equals(time, event.time);
    }

    /**
     * Generates a hash code for the event.
     * @return the generated hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, facility, waitListLimit, attendeeLimit, date, time);
    }

    /**
     * Gets the QR hash as a string.
     * Set bits are "1", unset bits are "9"
     * @return the string representation of the QR hash
     */
    public String getQrHash() {
        return qrHash.toString("1", "0");
    }

    /**
     * Generates a QR code for the event.
     * QR code is based off of the organizer, facility, event name, date, and time.
     * Uses the zxing library.
     * @return the QR code as a BitMatrix
     */
    public BitMatrix generateQRCode() {
        // Use event id for hashed string
        String hashedStr = id;
        // Encode string as QR code
        try {
            return new MultiFormatWriter().encode(hashedStr, BarcodeFormat.QR_CODE, 200, 200);
        } catch (WriterException e) {
            Log.e("QR Generation", "QR encoding failed!");
            return null;
        }
    }

    /**
     * Takes BitMatrix and generates a Bitmap.
     * Reference: https://stackoverflow.com/questions/19337448/generate-qr-code-directly-into-imageview
     * @param bitMatrix used to convert to bitMap
     * @return bitMap based on bitMatrix
     */
    public Bitmap createBitMap(BitMatrix bitMatrix) {
        int height = bitMatrix.getHeight();
        int width = bitMatrix.getWidth();
        Bitmap bitMap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x_value = 0; x_value < width; x_value++) {
            for (int y_value = 0; y_value < height; y_value++) {
                bitMap.setPixel(x_value, y_value, bitMatrix.get(x_value, y_value) ? Color.BLACK : Color.WHITE);
            }
        }
        // DO IMAGEVIEW HERE.
        return bitMap;
    }

    public void joinWaitList(String deviceId) {
        if (!waitList.contains(deviceId)) {
            waitList.add(deviceId);
            notifyObservers();
        }
    }
    /** Remove deviceId from joinWaitList when cancel button is clicked
     * @param deviceId users unique deviceID
     */
    public void leaveWaitList(String deviceId) {
        if (waitList.contains(deviceId)) {
            waitList.remove(deviceId);
            notifyObservers();
        }
    }

    public void leaveInviteeList(String deviceId) {
        if (inviteeList.contains(deviceId)) {
            inviteeList.remove(deviceId);
            notifyObservers();
        }
    }

    public void joinAttendeeList(String deviceId) {
        if (!attendeeList.contains(deviceId)) {
            attendeeList.add(deviceId);
            notifyObservers();
        }
    }

    public void leaveAttendeeList(String deviceId) {
        if (attendeeList.contains(deviceId)) {
            attendeeList.remove(deviceId);
            notifyObservers();
        }
    }

    public void joinCancelledList(String deviceId) {
        if (!cancelledList.contains(deviceId)) {
            cancelledList.add(deviceId);
            notifyObservers();
        }
    }

    public String getTime12h() {
        return time.toString12h();
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getDateAndTime() {
        return String.format("%s -- %s", time.toString12h(), date);
    }

    public String getFacility() {
        return facility;
    }

    public int getWaitListSpots() {
        return waitListLimit;
    }

    public int getAttendeeSpots() {
        return attendeeLimit;
    }

    public int getWaitListSize() {
        return waitList.size();
    }

    public int getAttendeeListSize() {
        return attendeeList.size();
    }

    public boolean onWaitList(String deviceId) {
        return waitList.contains(deviceId);
    }

    public boolean onInviteeList(String deviceId) {
        return inviteeList.contains(deviceId);
    }

    public boolean onAttendeeList(String deviceId) {
        return attendeeList.contains(deviceId);
    }

    public boolean onCancelledList(String deviceId) {
        return cancelledList.contains(deviceId);
    }

    public String getId() {
        return id;
    }

    public List<String> getWaitList() {
        return waitList;
    }

    public void setName(String name) {
        // TODO: Catch empty?
        this.name = name;
        notifyObservers();
    }

    public void setAttendeeLimit(Integer limit) {
        attendeeLimit = limit;
        notifyObservers();
    }

    public void setWaitListLimit(Integer limit) {
        waitListLimit = limit;
        notifyObservers();
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
        notifyObservers();
    }

    public void setOrganizerDeviceId(String organizerDeviceId) {
        this.organizerDeviceId = organizerDeviceId;
        notifyObservers();
    }

    public void setFacility(String facility) {
        this.facility = facility;
        notifyObservers();
    }

    public void setTime(int timeHours, int timeMinutes) {
        this.time = new Time(timeHours, timeMinutes);
        notifyObservers();
    }

    public void setDate(String date) {
        this.date = date;
        notifyObservers();
    }
}