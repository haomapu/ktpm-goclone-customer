package com.example.ktpm_goclone_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    HomeFragment homeFragment;
    HistoryFragment historyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.bottom_navigation);
        if (homeFragment == null){
            homeFragment = new HomeFragment();
        }
        WebsocketConnector websocketConnector = WebsocketConnector.getInstance(getApplicationContext());

        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, homeFragment).commit();
        navigationView.setSelectedItemId(R.id.homeFragment);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                if (item.getItemId() == R.id.homeFragment) {
                    if (homeFragment == null){
                        homeFragment = new HomeFragment();
                    }
                    fragment = homeFragment;
                } else if (item.getItemId() == R.id.history) {
                    if (historyFragment == null){
                        historyFragment = new HistoryFragment();
                    }
                    fragment = historyFragment;
                } else if (item.getItemId() == R.id.profile) {
                    fragment = new ProfileFragment();
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
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState); // Call super to propagate the instance state to child fragments (including HomeFragment)
        // Save the instance state data for the MainActivity...

    }
}