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
public class Organizer {
    private String facility;

    public Organizer() {
        super();
    }

    /**
     * Creates an Organizer from a facility name.
     * @param facility: the organizer's facility name
     */
    public Organizer(String facility) {
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
