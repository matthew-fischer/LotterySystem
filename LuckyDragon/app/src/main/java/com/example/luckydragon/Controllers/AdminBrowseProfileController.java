package com.example.luckydragon.Controllers;

import com.example.luckydragon.Models.User;
/** Controller class for managing admin-related profile browsing functionalities.
 * Allows administrators to interact with user data, such as deleting users by their device ID.
 * Extends the generic {@link Controller} class.
 */
public class AdminBrowseProfileController extends Controller{
    /**
     * Constructor to initialize the controller with an observable {@link User} instance.
     *
     * @param observable The {@link User} instance to be observed and managed by this controller.
     */

    public AdminBrowseProfileController(User observable) {
        super(observable);
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }
    /**
     * Deletes a user associated with the specified device ID.
     * This method delegates the deletion logic to the observable {@link User} instance.
     *
     */
    public void deleteUser() {
        getObservable().deleteUser();
    }

    /**
     * Removes the profile picture of the user. This method delegates
     * the deletion logic to the observable {@link User} instance.
     */
    public void removeProfilePicture() {
        getObservable().removeProfilePicture();
    }

}
