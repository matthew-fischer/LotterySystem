package com.example.luckydragon.Controllers;

import com.example.luckydragon.Models.User;

public class UserController extends Controller{
    public UserController(User observable) {
        super(observable);
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }

    public void deleteUser(String deviceId) {
        getObservable().deleteUser(deviceId);
    }

    public void deleteOrganizerEvents(String deviceId) {
        getObservable().deleteOrganizerEvents(deviceId);
    }

}
