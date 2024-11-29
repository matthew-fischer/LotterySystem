/**
 * Defines the Event model class and its inner class Time.
 */

package com.example.luckydragon.Models;

import static java.util.Objects.nonNull;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * Represents an Event object.
 * <p>
 * Issues:
 *   - could add another constructor for when waitlist limit is not specified (since it is optional)
 */
public class Event extends Observable implements Serializable {
    /**
     * Represents a time as hours and minutes in 24 hour time.
     * e.g. 8:30 pm would have hours = 20 and minutes = 30
     */
    private class Time {
        public Integer hours;
        public Integer minutes;

        /**
         * Creates a time instance.
         * @param hours the hours (24 hour time)
         * @param minutes the minutes
         */
        public Time(Integer hours, Integer minutes) {
            this.hours = hours;
            this.minutes = minutes;
        }

        /**
         * Generates a 24-hour string representation of time.
         * @return 24-hr time as string
         */
        @NonNull
        @Override
        public String toString() {
            return String.format("%02d%02d", hours, minutes);
        }

        /**
         * Generates a 12-hour string representation of time.
         * @return 12-hr time as string
         * @return
         */
        public String toString12h() {
            String AMorPM = hours < 12 ? "AM" : "PM";
            return String.format("%d:%02d %s", hours > 12 ? hours - 12 : hours, minutes, AMorPM);
        }
    }

    // TODO: REMOVE DEFAULT
    private transient FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String id;
    private String name = "";
    private String organizerName = "";
    private String organizerDeviceId = "";
    private String facility = "";
    private Integer waitListLimit = -1;
    private Integer attendeeLimit = -1;
    private Boolean hasGeolocation = false;
    private String date = LocalDate.now().plusDays(7).toString();
    private Time time = new Time(19, 0);
    private String lotteryDate = LocalDate.now().plusDays(3).toString();
    private Time lotteryTime = new Time(8, 0);
    private BitMatrix qrHash;
    private Bitmap qrCode;

    private Long createdTimeMillis = null;

    private List<String> waitList = new ArrayList<>();
    private List<String> inviteeList = new ArrayList<>();
    private List<String> attendeeList = new ArrayList<>();
    private List<String> cancelledList = new ArrayList<>();

    private List<Location> waitlistLocations = new ArrayList<>();

    private boolean isLoaded = false;
    private Bitmap eventPoster;

    private boolean inviteesHaveBeenSelected = false;

    /**
     * Creates an event instance without a given id.
     * @param db the database to use
     */
    public Event(FirebaseFirestore db) {
        this.db = db;
        id = db.collection("events").document().getId();
        qrHash = generateQRCode();
    }

    /**
     * Creates an event instance with a given id.
     * @param id the event id
     * @param db the database to use
     */
    public Event(String id, FirebaseFirestore db) {
        super();
        this.db = db;
        this.id = id;
    }

    /**
     * TODO: REMOVE
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
    public Event(String id, String name, String organizerDeviceId, String facility, Integer waitListLimit, Integer attendeeLimit, String date, Integer timeHours, Integer timeMinutes, String bitMatrixString, FirebaseFirestore db)  {
        this.id = id;
        this.name = name;
        this.organizerDeviceId = organizerDeviceId;
        this.facility = facility;
        this.waitListLimit = waitListLimit;
        this.attendeeLimit = attendeeLimit;
        this.date = date;
        this.time = new Time(timeHours, timeMinutes);
        this.qrHash = stringToBitMatrix(bitMatrixString);
        this.qrCode = createBitMap(this.qrHash);
        this.db = db;
    }

    /**
     * Notify observers and save event state to database.
     */
    @Override
    public void notifyObservers() {
        super.notifyObservers();
        save();
    }

    /**
     * Saves event data to database.
     */
    public void save() {
        Map<String, Object> eventData = new HashMap<>();
        if(nonNull(name)) eventData.put("name", name);
        if(nonNull(organizerDeviceId) && !organizerDeviceId.isEmpty()) eventData.put("organizerDeviceId", organizerDeviceId);
        if(nonNull(facility) && !facility.isEmpty()) eventData.put("facility", facility);
        if(nonNull(waitListLimit)) eventData.put("waitListLimit", waitListLimit);
        if(nonNull(attendeeLimit)) eventData.put("attendeeLimit", attendeeLimit);
        if(nonNull(hasGeolocation)) eventData.put("hasGeolocation", hasGeolocation);
        if(nonNull(date) && !date.isEmpty()) eventData.put("date", date);
        if(nonNull(time.hours)) eventData.put("hours", time.hours);
        if(nonNull(time.minutes)) eventData.put("minutes", time.minutes);
        if(nonNull(lotteryDate) && !lotteryDate.isEmpty()) eventData.put("lotteryDate", lotteryDate);
        System.out.println(lotteryTime == null ? "null" : "not null");
        if(nonNull(lotteryTime.hours)) eventData.put("lotteryHours", lotteryTime.hours);
        if(nonNull(lotteryTime.hours)) eventData.put("lotteryMinutes", lotteryTime.minutes);
        if(nonNull(qrHash)) eventData.put("hashedQR", qrHash.toString("1", "0"));
        eventData.put("poster", BitmapUtil.bitmapToString(eventPoster));
        eventData.put("waitList", waitList);
        eventData.put("inviteeList", inviteeList);
        eventData.put("attendeeList", attendeeList);
        eventData.put("cancelledList", cancelledList);
        eventData.put("waitListLocations", waitlistLocations);

        if(nonNull(createdTimeMillis)) eventData.put("createdTimeMillis", createdTimeMillis);
        if(nonNull(inviteesHaveBeenSelected)) eventData.put("inviteesHaveBeenSelected", inviteesHaveBeenSelected);
        // if event is loaded and createdTimeMillis is still null, then add the time stamp
        if(createdTimeMillis == null && isLoaded) {
            createdTimeMillis = System.currentTimeMillis();
            eventData.put("createdTimeMillis", createdTimeMillis);
        }

        if (id == null || id.isEmpty()) {
            throw new RuntimeException("Event id should not be empty!");
        }
        db.collection("events").document(id)
                .set(eventData).addOnFailureListener(e -> {
                    Log.e("SAVE DB", "event save fail");
                });
    }

    /**
     * Fetches event data from database.
     */
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

                parseEventDocument(eventData);
                notifyObservers();
            }
        });
    }

    /**
     * Given the map data of an event returned from a Firestore call,
     * parses the raw data into the event object. Any fields set to null will also
     * be set to null in the event object.
     * @param eventData the raw event data from Firestore
     */
    public void parseEventDocument(Map<String, Object> eventData) {
        if(eventData == null) return;
        if (nonNull(eventData.get("name"))) {
            name = (String) eventData.get("name");
        }
        if (nonNull(eventData.get("organizerDeviceId"))) {
            organizerDeviceId = (String) eventData.get("organizerDeviceId");
        }
        if (nonNull(eventData.get("facility"))) {
            facility = (String) eventData.get("facility");
        }
        if (nonNull(eventData.get("waitListLimit"))) {
            waitListLimit = Math.toIntExact((Long) eventData.get("waitListLimit"));
        }
        if (nonNull(eventData.get("attendeeLimit"))) {
            attendeeLimit = Math.toIntExact((Long) eventData.get("attendeeLimit"));
        }
        if (nonNull(eventData.get("hasGeolocation"))) {
            hasGeolocation = (boolean) eventData.get("hasGeolocation");
        }
        if (nonNull(eventData.get("date"))) {
            date = (String) eventData.get("date");
        }
        if (nonNull(eventData.get("lotteryDate"))) {
            lotteryDate = (String) eventData.get("lotteryDate");
        }
        if(nonNull(eventData.get("createdTimeMillis"))) {
            createdTimeMillis = (Long) eventData.get("createdTimeMillis");
        }
        if(nonNull(eventData.get("inviteesHaveBeenSelected"))) {
            inviteesHaveBeenSelected = (boolean) eventData.get("inviteesHaveBeenSelected");
        }

        eventPoster = BitmapUtil.stringToBitmap((String)eventData.get("poster"));

        int hours = eventData.get("hours") != null ? Math.toIntExact((Long) eventData.get("hours")) : null;
        int minutes = eventData.get("minutes") != null ? Math.toIntExact((Long) eventData.get("minutes")) : null;
        time = new Time(hours, minutes);

        if(eventData.get("lotteryHours") != null && eventData.get("lotteryMinutes") != null) {
            lotteryTime = new Time(Math.toIntExact((Long) eventData.get("lotteryHours")), Math.toIntExact((Long) eventData.get("lotteryMinutes")));
        }

        if(eventData.get("waitListLocations") != null) {
            waitlistLocations = new ArrayList<>();
            for(HashMap<String, Object> oMap : (ArrayList<HashMap<String, Object>>) eventData.get("waitListLocations")) {
                waitlistLocations.add(new Location((double) oMap.get("latitude"), (double) oMap.get("longitude")));
            }
        }
        if (eventData.get("waitList") != null) {
            waitList = (List<String>) eventData.get("waitList");
        }
        if (eventData.get("attendeeList") != null) {
            attendeeList = (ArrayList<String>) eventData.get("attendeeList");
        }
        if (eventData.get("inviteeList") != null) {
            inviteeList = (ArrayList<String>) eventData.get("inviteeList");
        }
        if (eventData.get("cancelledList") != null) {
            cancelledList = (ArrayList<String>) eventData.get("cancelledList");
        }

        // stringToBitMatrix handles null values
        this.qrHash = stringToBitMatrix((String) eventData.get("hashedQR"));
        this.qrCode = createBitMap(this.qrHash);

        setIsLoaded(true);
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
        return qrHash != null ? qrHash.toString("1", "0") : null;
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
        if (bitMatrix == null) return null;
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

    /**
     * Adds device id to waitlist.
     * @param deviceId user's unique device id
     */
    public void joinWaitList(String deviceId) {
        if (!waitList.contains(deviceId)) {
            waitList.add(deviceId);
        }
        //notifyObservers();
    }

    /**
     * Adds location to waitlistLocations.
     * @param latitude latitude of locaton to add
     * @param longitude longitude of location to add
     */
    public void addWaitlistLocation(double latitude, double longitude) {
        Location location = new Location(latitude, longitude);
        waitlistLocations.add(location);
    }

    /** Removes deviceId from joinWaitList when cancel button is clicked
     * @param deviceId users unique deviceID
     */
    public void leaveWaitList(String deviceId) {
        if (waitList.contains(deviceId)) {
            waitList.remove(deviceId);
            //notifyObservers();
        }
    }

    /**
     * Removes deviceId from waitList and corresponding location from waitListLocations.
     * @param deviceId the device id to remove
     */
    public void leaveWaitlistWithLocation(String deviceId) {
        // find index of id in waitlist
        int index = 0;
        for(; index < waitList.size(); index++) {
            if(Objects.equals(waitList.get(index), deviceId)) {
                break;
            }
        }
        if(index == waitList.size()) return;

        waitList.remove(index);
        waitlistLocations.remove(index);

        //notifyObservers();
    }

    /**
     * Removes deviceId from invitee list.
     * @param deviceId users unique device ID
     */
    public void leaveInviteeList(String deviceId) {
        if (inviteeList.contains(deviceId)) {
            inviteeList.remove(deviceId);
            //notifyObservers();
        }
    }

    /**
     * Add deviceId to attendee list.
     * @param deviceId
     */
    public void joinAttendeeList(String deviceId) {
        if (!attendeeList.contains(deviceId)) {
            attendeeList.add(deviceId);
            //notifyObservers();
        }
    }

    /**
     * Removes deviceId from attendee list.
     * @param deviceId user's unique device id
     */
    public void leaveAttendeeList(String deviceId) {
        if (attendeeList.contains(deviceId)) {
            attendeeList.remove(deviceId);
            //notifyObservers();
        }
    }

    /**
     * Adds device id to cancelled list.
     * @param deviceId user's unique device id
     */
    public void joinCancelledList(String deviceId) {
        if (!cancelledList.contains(deviceId)) {
            cancelledList.add(deviceId);
            //notifyObservers();
        }
    }

    /**
     * Deletes this event from the database.
     * This does not take a device id.
     */
    public void deleteEventFromDb() {
        db.collection("events")
                .document(id)
                .delete();
    }

    /**
     * Removes the event's QR data from the database.
     */
    public void removeQR() {
        qrHash = null;
        notifyObservers();
    }

    /**
     * Sets the event's poster to null, effectively removing it.
     */
    public void removeEventPoster() {
        setEventPoster(null);
    }

    // Getters and setters:
    public String getTime12h() {
        return time.toString12h();
    }

    public String getLotteryTime12h() { return lotteryTime.toString12h(); }

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

    public boolean hasGeolocation() { return hasGeolocation; }

    /**
     * Checks if deviceId is present on the waitlist.
     * @param deviceId the device id
     * @return true if it is present, else false
     */
    public boolean onWaitList(String deviceId) {
        return waitList.contains(deviceId);
    }

    /**
     * Checks if deviceId is present on the invitee list.
     * @param deviceId the device id
     * @return true if it is present, else false
     */
    public boolean onInviteeList(String deviceId) {
        return inviteeList.contains(deviceId);
    }

    /**
     * Checks if deviceId is present on the attendee list.
     * @param deviceId the device id
     * @return true if it is present, else false
     */
    public boolean onAttendeeList(String deviceId) {
        return attendeeList.contains(deviceId);
    }

    /**
     * Checks if deviceId is present on the cancelled list.
     * @param deviceId the device id
     * @return true if it is present, else false
     */
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

    public void setHasGeolocation(Boolean hasGeolocation) {
        this.hasGeolocation = hasGeolocation;
        notifyObservers();
    }

    public String getOrganizerDeviceId() {
        return organizerDeviceId;
    }

    public List<String> getInviteeList() {
        return inviteeList;
    }

    public List<String> getAttendeeList() {
        return attendeeList;
    }

    public List<String> getCancelledList() {
        return cancelledList;
    }

    public void setIsLoaded(boolean newIsLoaded) {
        this.isLoaded = newIsLoaded;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public List<Location> getWaitlistLocations() {
        return waitlistLocations;
    }

    public boolean haveInviteesBeenSelected() {
        return inviteesHaveBeenSelected;
    }

    public void setInviteesHaveBeenSelected(boolean inviteesHaveBeenSelected) {
        this.inviteesHaveBeenSelected = inviteesHaveBeenSelected;
    }

    /**
     * Samples an invitee from the wait list to be added to the invitee list.
     * Returns the deviceId of the user who was selected.
     */
    public String sampleInvitee() {
        assert(waitList.size() > 0);

        Random random = new Random();
        int randomIndex = random.nextInt(waitList.size());
        // Move id from waitlist to invitee list
        String selectedId = waitList.get(randomIndex);
        inviteeList.add(selectedId);
        // Remove id from waitlist
        waitList.remove(randomIndex);
        if(hasGeolocation) {
            waitlistLocations.remove(randomIndex);
        }

        return selectedId;
    }

    /**
     * Selects invitees from the waiting list for the first time.
     * This is different from fillInvitees(), which is intended to fill spots freed by cancelled entrants after the initial selection.
     */
    public void selectInviteesFirstTime() {
        assert(inviteeList.isEmpty()); // invitee list should start empty
        assert(!inviteesHaveBeenSelected); // invitees should not have been selected yet

        while(!waitList.isEmpty() && inviteeList.size() < attendeeLimit) {
            sampleInvitee();
        }

        inviteesHaveBeenSelected = true; // invitees have now been selected
    }

    /**
     * Fills the invitee list with replacement entrants from the wait list if there are still
     * some spots left. Only can be called after the initial sampling of invitees
     * has happened.
     *
     * @return a list of the deviceIds of the entrants who have been selected as replacements
     */
    public ArrayList<String> fillInvitees() {
        assert(inviteesHaveBeenSelected);

        ArrayList<String> replacementInvitees = new ArrayList<>();
        while (!waitList.isEmpty() && inviteeList.size() + attendeeList.size() < attendeeLimit) {
            replacementInvitees.add(sampleInvitee());
        }

        return replacementInvitees;
    }

    public Long getCreatedTimeMillis() {
        return createdTimeMillis;
    }

    public String getLotteryDate() {
        return lotteryDate;
    }

    public void setLotteryDate(String lotteryDate) {
        this.lotteryDate = lotteryDate;
        notifyObservers();
    }

    public Time getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(int hours, int minutes) {
        Time lotteryTime = new Time(hours, minutes);
        this.lotteryTime = lotteryTime;
        notifyObservers();
    }

    public int getLotteryHours() {
        return lotteryTime.hours;
    }

    public int getLotteryMinutes() {
        return lotteryTime.minutes;
    }

    public void setEventPoster(Bitmap poster) {
        this.eventPoster = poster;
        notifyObservers();
    }

    public Bitmap getEventPoster() {
        return this.eventPoster;
    }

    public static BitMatrix stringToBitMatrix(String s) {
        if (s == null || s.isEmpty()) return null;

        String[] grid = s.split("\n");

        int n = grid.length;
        int m = grid[0].length();
        BitMatrix matrix = new BitMatrix(n, m);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i].charAt(j) == '1') {
                    matrix.set(i, j);
                }
            }
        }

        return matrix;
    }

    public BitMatrix getQRBitMatrix() {
        return this.qrHash;
    }
}