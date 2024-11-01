/**
 * Defines the Organizer model class.
 */

package com.example.luckydragon;

/**
 * Represents an Organizer. Subclass of User.
 * Adds a facility field.
 * <p>
 * Issues:
 *   This is only a basic implementation. Additional functionality should be added as needed.
 *   Since email and phone number are optional, additional constructors should be added.
 */
public class Organizer extends User {
    private String facility;

    /**
     * Creates an Organizer object.
     * @param deviceID: the user's unique device id
     * @param name: the user's name
     * @param email: the user's email
     * @param phoneNumber: the user's phone number
     * @param facility: the organizer's facility name
     */
    public Organizer(String deviceID, String name, String email, String phoneNumber, String facility) {
        super(deviceID, name, email, phoneNumber);
        this.facility = facility;
    }

    /**
     * Creates an Organizer from a user and facility name.
     * @param user: the user to convert to an organizer
     * @param facility: the organizer's facility name
     */
    public Organizer(User user, String facility) {
        super(user);
        this.facility = facility;
    }


    /**
     * Gets the facility name for the organizer.
     * @return the organizer's facility name
     */
    public String getFacility() {
        return facility;
    }

    /**
     * Sets the facility name for the organizer.
     * @param facility: the new facility name
     */
    public void setFacility(String facility) {
        this.facility = facility;
    }
}
