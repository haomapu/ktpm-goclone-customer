package com.example.ktpm_goclone_customer;

import  androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {


    private Button btnLogin;
    private Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Open Textview
        TextView textView = findViewById(R.id.tvDescWelcome2);

        String text = "Dengan masuk atau mendaftar, kamu menyetujui Ketentuan Layanan dan Kebijakan Privasi";

        SpannableString ss = new SpannableString(text);

        ForegroundColorSpan fcsGreen = new ForegroundColorSpan(Color.GREEN);
        ForegroundColorSpan fcsGreen2 = new ForegroundColorSpan(Color.GREEN);

        ss.setSpan(fcsGreen, 45, 62, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(fcsGreen2, 67, 84, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(ss); //close TextView

        //Open Button Login Activity

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity();
            }
        });
        //Close Button Login Activity

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

    }

    public void LoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}