package com.example.ktpm_goclone_customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button btnBack;
    Button login;
    EditText inputnumberhp;

    private String KEY_NUMBER = "NOMORHP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Login Using Number
        login = (Button) findViewById(R.id.btnLanjut);
        inputnumberhp = (EditText) findViewById(R.id.etNomorHP);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            try {
                String nomor = inputnumberhp.getText().toString();
                if (nomor.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Nomor HP Tidak Boleh Kosong!", Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(LoginActivity.this, otpActivity.class);
                    i.putExtra(KEY_NUMBER, nomor);
                    startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplication(), "ERROR, TRY AGAIN !",Toast.LENGTH_SHORT).show();
            }
            }
        });

        //Open Textview
        TextView textView = findViewById(R.id.tvNomorHp);

        String text = "Nomor Hp*";

        SpannableString ss = new SpannableString(text);

        ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.RED);

        ss.setSpan(fcsRed, 8, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(ss); //close TextView

        //Open Button Login Activity

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WelcomeActivity();
            }
        });
        //Close Button Login Activity
    }

    public void WelcomeActivity() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }
}