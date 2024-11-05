package com.example.luckydragon;

public class ProfileView extends Observer {
    private final ProfileActivity profileActivity;

    public ProfileView(User user, ProfileActivity profileActivity) {
        this.profileActivity = profileActivity;
        startObserving(user);
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }

    @Override
    public void update(Observable whoUpdatedMe) {
        if(getObservable().isLoaded()) {
            profileActivity.initializeView();
        }
    }
}
