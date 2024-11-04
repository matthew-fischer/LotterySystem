package com.example.luckydragon;

public class EditProfileView extends Observer {
    private final EditProfileDialogFragment fragment;
    private final EditProfileController controller;

    public EditProfileView(User user, EditProfileDialogFragment editProfileDialogFragment, EditProfileController editProfileController) {
        this.fragment = editProfileDialogFragment;
        this.controller = editProfileController;
        startObserving(user);
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }

    @Override
    public void update(Observable whoUpdatedMe) {
        // check if user has valid fields
        // TODO: activity creates error messages as necessary
//        fragment.setSubmitButton(getObservable().isValid());
    }
}
