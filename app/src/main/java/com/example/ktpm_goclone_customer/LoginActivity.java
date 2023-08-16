package com.example.ktpm_goclone_customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private Button btnBack;
    Button login;
    EditText username, password;

    private String KEY_NUMBER = "NOMORHP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ApiCaller apiCaller = ApiCaller.getInstance();
        // Login Using Number
        login = (Button) findViewById(R.id.buttonLogin);
        username = (EditText) findViewById(R.id.editTextUsername);
        password = (EditText) findViewById(R.id.editTextPassword);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            try {
                String nomor = username.getText().toString();
                if (nomor.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please fill out the blanks!", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("username", username.getText().toString());
                    jsonObject.put("password", password.getText().toString());;
                    apiCaller.post("/api/auth/signin", jsonObject.toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                if (response.code() == 401) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, "Wrong password or username!", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    return;
                                }
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                User.currentUser = new User(jsonObject.getString("username"), jsonObject.getString("email"), jsonObject.getString("id"));
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("token", jsonObject.getString("token"));
                                editor.putString("id", jsonObject.getString("id"));
                                editor.putString("username", jsonObject.getString("username"));
                                editor.apply();
                                editor.commit();
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

//                            editor.putString("token", token)
                        }
                    }, "None");

                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplication(), "ERROR, TRY AGAIN !",Toast.LENGTH_SHORT).show();
            }
            }
        });

        //Open Textview
        TextView textView = findViewById(R.id.textViewUsername);

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