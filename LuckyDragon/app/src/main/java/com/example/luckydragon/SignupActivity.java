package com.example.luckydragon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SignupActivity extends AppBarActivity {
    private User user;
    private SelectRoleController selectRoleController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("Sign-Up");

        // Unpack intent
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        selectRoleController = (SelectRoleController) intent.getSerializableExtra("selectRoleController");

        // Get input fields
        EditText editName = findViewById(R.id.signupName);
        EditText editEmail = findViewById(R.id.signupEmail);
        EditText editPhone = findViewById(R.id.signupPhone);
        // TODO: Profile photo
        SwitchMaterial switchNotifications = findViewById(R.id.signupNotifications);
        Button submitButton = findViewById(R.id.signupSubmit);

//        // on submit listener
//        submitButton.setOnClickListener(v -> {
//            selectRoleController.saveUserData();
//            finish();
//        });
    }


}
