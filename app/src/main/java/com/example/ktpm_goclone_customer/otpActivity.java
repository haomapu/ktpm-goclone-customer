package com.example.ktpm_goclone_customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class otpActivity extends AppCompatActivity {

    TextView tvGetNumber;
    private String nomor;
    private String KEY_NUMBER ="NOMORHP";
    private Button btnBack2;
    Button login;
    EditText otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        // OTP Valid Open

        login = (Button) findViewById(R.id.btnNextHome);
        otp = (EditText) findViewById(R.id.etNomorHP);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otp.getText().toString().toUpperCase().equals("1234"))
                {
                    otp_berhasil();
                } else
                {
                 Toast.makeText(getApplicationContext(), "OTP Anda Salah", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // OTP Valid Close

        //Open Get Number
        tvGetNumber = (TextView) findViewById(R.id.tvGetNumber);

        Bundle extras = getIntent().getExtras();
        nomor = extras.getString(KEY_NUMBER);
        tvGetNumber.setText(" " + nomor + " ");
        //Close Get Number


        //Open Button Login Activity

        btnBack2 = (Button) findViewById(R.id.btnBack2);
        btnBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity();
            }
        });
        //Close Button Login Activity

    }
    public void LoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void otp_berhasil() {
        Intent i = new Intent(otpActivity.this,MainActivity.class);
        startActivity(i);
    }
}