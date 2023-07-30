package com.example.ktpm_goclone_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeFragment()).commit();
        navigationView.setSelectedItemId(R.id.homeFragment);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                if (item.getItemId() == R.id.homeFragment) {
                    fragment = new HomeFragment();
                } else if (item.getItemId() == R.id.promo_fragment) {
                    fragment = new PromoFragment();
                } else if (item.getItemId() == R.id.riwayat) {
                    fragment = new HistoryFragment();
                } else if (item.getItemId() == R.id.chat) {
                    fragment = new ChatFragment();
                }
//                switch (item.getItemId()){
//                    case R.id.homeFragment:
//                        fragment = new HomeFragment();
//                        break;
//
//                    case R.id.promo_fragment:
//                        fragment = new PromoFragment();
//                        break;
//
//                    case R.id.riwayat:
//                        fragment = new HistoryFragment();
//                        break;
//
//                    case R.id.chat:
//                        fragment = new ChatFragment();
//                        break;
//                }
                getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();

                return true;
            }
        });
    }
}