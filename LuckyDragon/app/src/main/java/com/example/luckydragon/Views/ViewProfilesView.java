package com.example.luckydragon.Views;

import com.example.luckydragon.Activities.ViewProfilesActivity;
import com.example.luckydragon.Models.Observable;
import com.example.luckydragon.Models.UserList;

/**
 * Observer class that listens for updates to an UserList and
 * refreshes the ViewProfilesActivity UI when changes occur.
 * <p>
 *     The ViewProfilesView class observes an UserList and triggers an update
 *     in ViewProfilesActivity when the list is modified, ensuring that the
 *     event data displayed to user is up-to-date.
 * </p>
 */
public class ViewProfilesView extends Observer {

    private final ViewProfilesActivity viewProfilesActivity;

    /**
     * Constructs a new ViewProfilesView observer and begins observing the UserList.
     * @param users the UserList to observe for changes
     * @param viewProfilesActivity the Activity to notify of updates
     */
    public ViewProfilesView(UserList users, ViewProfilesActivity viewProfilesActivity) {
        this.viewProfilesActivity = viewProfilesActivity;
        startObserving(users);
    }

    /**
     * Returns the observed UserList.
     * @return observed UserList instance
     */
    @Override
    public UserList getObservable() {
        return (UserList) super.getObservable();
    }

    /**
     * Called when the observed UserList is updated.
     * <p>
     *     This method notifies the ViewProfilesActivity to refresh the UI, so
     *     any changes in the UserList are displayed.
     * </p>
     * @param whoUpdatedMe the Observable that triggered this update
     */
    @Override
    public void update(Observable whoUpdatedMe) {

        viewProfilesActivity.notifyAdapter();

    }

}
