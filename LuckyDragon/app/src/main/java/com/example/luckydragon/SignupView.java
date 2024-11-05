package com.example.luckydragon;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import java.util.Objects;

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

        // hide signup top bar if user is signing (not editing)
        if (Objects.equals(signupActivity.getRole(), "ENTRANT") && !getObservable().isEntrant()) {
            signupActivity.setNavProfileVisible(false);
        } else if (Objects.equals(signupActivity.getRole(), "ORGANIZER") && !getObservable().isOrganizer()) {
            signupActivity.setNavProfileVisible(false);
        } else {
            signupActivity.setNavProfileVisible(true);
        }
    }
}
