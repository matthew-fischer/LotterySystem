/*
 * Defines SelectRoleView which manages SelectRoleActivity.
 * Will initialize SelectRoleActivity only once the user is loaded.
 * ISSUES:
 *   NONE
 */

package com.example.luckydragon;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

/**
 * This is the view that updates SelectRoleActivity.
 * Once user is loaded, this will initialize SelectRoleActivity's buttons and on click listeners.
 * If user is an admin, it will show the admin button.
 */
public class SelectRoleView extends Observer {
    private final SelectRoleActivity selectRoleActivity;

    /**
     * Creates a SelectRoleView.
     * @param user the application user
     * @param selectRoleActivity the activity to update
     */
    public SelectRoleView(User user, SelectRoleActivity selectRoleActivity) {
        this.selectRoleActivity = selectRoleActivity;
        startObserving(user);
    }

    /**
     * Updates SelectRoleActivity when triggered by a change in User.
     * Calls selectRoleActivity.initializeView() once the user loads.
     * Calls selectRoleActivity.showAdminButton() if user is an admin.
     * @param whoUpdatedMe the observable that triggered the update
     */
    @Override
    public void update(Observable whoUpdatedMe) {
        if (getObservable().isLoaded()) {
            selectRoleActivity.initializeView();
        }

        if (getObservable().isAdmin()) {
            selectRoleActivity.showAdminButton();
        }
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }
}
