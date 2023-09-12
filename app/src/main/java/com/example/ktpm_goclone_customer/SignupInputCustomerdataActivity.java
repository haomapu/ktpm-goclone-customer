package com.example.ktpm_goclone_customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class SignupInputCustomerdataActivity extends AppCompatActivity {
    Button buttonContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_input_customerdata);

        buttonContinue = findViewById(R.id.buttonContinue);

        Intent preIntent = getIntent(); // get data from previous Activity
        String email = preIntent.getStringExtra("email");

        buttonContinue.setOnClickListener(view -> {
            Intent i = new Intent(SignupInputCustomerdataActivity.this, WelcomeActivity.class);
            startActivity(i);
            finishAffinity();
        });
    }
}