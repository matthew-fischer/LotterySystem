package com.example.luckydragon;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class SignupActivity extends AppBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("Sign-Up");
    }
}
