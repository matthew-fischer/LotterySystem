package com.example.luckydragon.Views;

import com.example.luckydragon.Activities.ViewEventActivity;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.Observable;
import com.example.luckydragon.Models.User;
import com.example.luckydragon.R;
import com.example.luckydragon.Views.Observer;

public class ViewEventView extends Observer {
    private final ViewEventActivity viewEventActivity;

    public ViewEventView(Event event, ViewEventActivity viewEventActivity) {
        this.viewEventActivity = viewEventActivity;
        startObserving(event);
    }

    @Override
    public void update(Observable whoUpdatedMe) {
        viewEventActivity.updateEventName();
        viewEventActivity.updateEventFacility();
        viewEventActivity.updateEventDateAndTime();
    }

    @Override
    public Event getObservable() {
        return (Event) super.getObservable();
    }
}
