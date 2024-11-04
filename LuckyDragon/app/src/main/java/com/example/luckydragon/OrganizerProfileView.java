package com.example.luckydragon;

public class OrganizerProfileView extends Observer {
    private final OrganizerProfileFragment organizerProfileFragment;

    public OrganizerProfileView(User user, OrganizerProfileFragment organizerProfileFragment) {
        this.organizerProfileFragment = organizerProfileFragment;
        startObserving(user);
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }

    @Override
    public void update(Observable whoUpdatedMe) {
        if(getObservable().isLoaded()) {
            organizerProfileFragment.initializeView();
        }
    }
}
