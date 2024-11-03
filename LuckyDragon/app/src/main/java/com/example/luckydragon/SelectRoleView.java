package com.example.luckydragon;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

public class SelectRoleView extends Observer {
    private final SelectRoleActivity selectRoleActivity;

    public SelectRoleView(User user, SelectRoleActivity selectRoleActivity) {
        this.selectRoleActivity = selectRoleActivity;
        startObserving(user);
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }

    @Override
    public void update(Observable whoUpdatedMe) {
        if (getObservable().isLoaded()) {
            selectRoleActivity.initializeView();
        }

        if (getObservable().isAdmin()) {
            selectRoleActivity.showAdminButton();
        }
    }
}
