package com.example.luckydragon;

import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import org.w3c.dom.Text;

import java.time.Instant;
import java.util.Objects;

/**
 * Creates the view for an Event which extends the Observer abstract class.
 */
public class AddEventView extends Observer {
    private AddEventDialogFragment fragment;
    private TextView timeTextView;
    private TextView dateTextView;

    private String deviceId;

    /**
     * Constructor for the AddEventView class. Calls the Observer method to start observing the event.
     * @param event
     * @param fragment
     */
    public AddEventView(Event event, AddEventDialogFragment fragment) {
        this.fragment = fragment;
        Objects.requireNonNull(fragment.getDialog());
        timeTextView = fragment.getDialog().findViewById(R.id.timeTextView);
        dateTextView = fragment.getDialog().findViewById(R.id.dateTextView);
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
        String time = getObservable().getTime12h();
        timeTextView.setText(time);

        String date = getObservable().getDate();
        dateTextView.setText(date);
    }
}
