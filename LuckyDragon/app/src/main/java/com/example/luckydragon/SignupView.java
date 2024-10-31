package com.example.luckydragon;

import android.content.Intent;

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
        if (getObservable().isValid()) {
            // TODO: activity creates error messages as necessary

            // if all is good, tell controller to save data to db
            signupController.saveUserData();

            // tell activity to launch profile
            Intent intent = new Intent(signupActivity, ProfileActivity.class);
            intent.putExtra("user", getObservable());
            intent.putExtra("role", "ENTRANT");

            // Start profile activity
            signupActivity.startActivity(intent);
            stopObserving();
            signupActivity.finish();
        }
    }
}
