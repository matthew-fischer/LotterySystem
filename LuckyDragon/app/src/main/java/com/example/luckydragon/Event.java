package com.example.luckydragon;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Event {
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
    }
    private String name;
    private String organizer;
    private String facility;
    private Integer waitlistLimit;
    private Integer attendeeLimit;
    private String date;
    private Time time;
    private BitMatrix qrHash;

    public Event(String name, String organizer, String facility, Integer waitlistLimit, Integer attendeeLimit, String date, Integer timeHours, Integer timeMinutes)  {
        this.name = name;
        this.organizer = organizer;
        this.facility = facility;
        this.waitlistLimit = waitlistLimit;
        this.attendeeLimit = attendeeLimit;
        this.date = date;
        this.time = new Time(timeHours, timeMinutes);
        this.qrHash = generateQRCode();
    }

    public Map<String, Object> toHashMap() {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("Name", name);
        eventData.put("Facility", facility);
        eventData.put("WaitlistLimit", waitlistLimit);
        eventData.put("AttendeeLimit", attendeeLimit);
        eventData.put("Date", date);
        eventData.put("Hours", time.hours);
        eventData.put("Minutes", time.minutes);
        eventData.put("HashedQR", qrHash.toString("1", "0"));
        return eventData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(name, event.name) && Objects.equals(facility, event.facility) && Objects.equals(waitlistLimit, event.waitlistLimit) && Objects.equals(attendeeLimit, event.attendeeLimit) && Objects.equals(date, event.date) && Objects.equals(time, event.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, facility, waitlistLimit, attendeeLimit, date, time);
    }

    public String getQrHash() {
        return qrHash.toString("1", "0");
    }

    public BitMatrix generateQRCode() {
        // Generate a string that concatenates organizer, facility, name, date, and time and replaces whitespace with "-"
        String hashedStr = String.format("%s/%s/%s/%s/%s", organizer.trim(), facility.trim(), name.trim(), date, time.toString()).replaceAll("\\s+", "-");;
        // Encode string as QR code
        try {
            return new MultiFormatWriter().encode(hashedStr, BarcodeFormat.QR_CODE, 200, 200);
        } catch (WriterException e) {
            Log.e("QR Generation", "QR encoding failed!");
            return null;
        }
    }
}
