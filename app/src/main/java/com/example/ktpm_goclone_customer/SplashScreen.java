package com.example.ktpm_goclone_customer;

import static com.example.ktpm_goclone_customer.User.currentUser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //menghilangkan ActionBar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Retrieve JWT token from SharedPreferences
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token", null);
                String id = sharedPreferences.getString("id", null);
                String username = sharedPreferences.getString("username", null);
                if (token == null){
                    startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                    finish();
                } else {
                    currentUser = new User(username, token, id);
                    Log.e("Hello", currentUser.getId());

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }


            }
        }, 2000L); //3000 L = 3 detik
    }
}