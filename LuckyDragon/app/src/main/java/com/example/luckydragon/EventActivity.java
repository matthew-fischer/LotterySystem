package com.example.luckydragon;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class EventActivity extends AppBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        getSupportActionBar().setTitle("Event");
    }
}
