package com.example.ktpm_goclone_customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {
    Button buttonSignup, btnBack;
    EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        buttonSignup = findViewById(R.id.buttonSignup);
        email = (EditText) findViewById(R.id.editTextEmail);
        btnBack = (Button) findViewById(R.id.btnBack);


        buttonSignup.setOnClickListener(view -> {
            try {
                String email = this.email.getText().toString();
                if (email.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please fill out the blanks!", Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(SignupActivity.this, SignupInputCustomerdataActivity.class);
                    i.putExtra("email", email);
                    startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplication(), "ERROR, TRY AGAIN !",Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}