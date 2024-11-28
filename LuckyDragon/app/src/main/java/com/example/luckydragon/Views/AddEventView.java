package com.example.luckydragon.Views;

import android.widget.TextView;

import com.example.luckydragon.Fragments.AddEventDialogFragment;
import com.example.luckydragon.Models.Event;
import com.example.luckydragon.Models.Observable;
import com.example.luckydragon.R;

import java.util.Objects;

/**
 * Creates the view for an Event which extends the Observer abstract class.
 */
public class AddEventView extends Observer {
    private AddEventDialogFragment fragment;
    private TextView timeTextView;
    private TextView dateTextView;
    private TextView lotteryTimeTextView;
    private TextView lotteryDateTextView;

    private String deviceId;

    /**
     * Constructor for the AddEventView class. Calls the Observer method to start observing the event.
     * @param event
     * @param fragment
     */
    public AddEventView(Event event, AddEventDialogFragment fragment) {
        this.fragment = fragment;
        Objects.requireNonNull(fragment.getDialog());
        timeTextView = fragment.getDialog().findViewById(R.id.eventTimeTextView);
        dateTextView = fragment.getDialog().findViewById(R.id.eventDateTextView);
        lotteryTimeTextView = fragment.getDialog().findViewById(R.id.lotteryTimeTextView);
        lotteryDateTextView = fragment.getDialog().findViewById(R.id.lotteryDateTextView);
        startObserving(event);
    }

    /**
     * Returns the observed Event.
     * @return observed Event instance.
     */
    @Override
    public Event getObservable() {
        return (Event) super.getObservable();
    }

    /**
     * Override the Event Observer update function. Sets the time and date text views.
     * @param whoUpdatedMe
     */
    @Override
    public void update(Observable whoUpdatedMe) {
        String eventTime = getObservable().getTime12h();
        timeTextView.setText(eventTime);

        String eventDate = getObservable().getDate();
        dateTextView.setText(eventDate);

        String lotteryTime = getObservable().getLotteryTime12h();
        lotteryTimeTextView.setText(lotteryTime);

        String lotteryDate = getObservable().getLotteryDate().toString();
        lotteryDateTextView.setText(lotteryDate);
    }
}
