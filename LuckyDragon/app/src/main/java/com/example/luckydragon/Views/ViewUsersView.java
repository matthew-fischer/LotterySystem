package com.example.luckydragon.Views;

import com.example.luckydragon.Activities.ViewProfilesActivity;
import com.example.luckydragon.Models.Observable;
import com.example.luckydragon.Models.UserList;

public class ViewUsersView extends Observer{

    private final ViewProfilesActivity viewProfilesActivity;

    public ViewUsersView(UserList users, ViewProfilesActivity viewProfilesActivity) {
        this.viewProfilesActivity = viewProfilesActivity;
        startObserving(users);
    }

    @Override
    public UserList getObservable() {
        return (UserList) super.getObservable();
    }

    @Override
    public void update(Observable whoUpdatedMe) {

        viewProfilesActivity.notifyAdapter();

    }
}
