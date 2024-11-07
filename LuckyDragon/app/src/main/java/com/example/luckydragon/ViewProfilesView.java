package com.example.luckydragon;

public class ViewProfilesView extends Observer{

    private final ViewProfilesActivity viewProfilesActivity;

    public ViewProfilesView(UserList users, ViewProfilesActivity viewProfilesActivity) {
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
