package com.example.luckydragon.Controllers;

import com.example.luckydragon.Models.User;

public class AdminBrowseProfileController extends Controller{
    public AdminBrowseProfileController(User observable) {
        super(observable);
    }

    @Override
    public User getObservable() {
        return (User) super.getObservable();
    }

    public void deleteUser(String deviceId) {
        getObservable().deleteUser(deviceId);
    }

}
