package com.example.luckydragon;

import android.content.Intent;
import android.util.Log;

public class SignupView extends Observer {
    private final SignupActivity signupActivity;
    private final SignupController signupController;

    public SignupView(User user, SignupActivity signupActivity, SignupController signupController) {
        this.signupActivity = signupActivity;
        this.signupController = signupController;
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
        signupActivity.setSubmitButton(getObservable().isValid());
        signupActivity.updateProfilePictureIcon(getObservable().getUploadedProfilePicture());
    }
}
