package com.example.ktpm_goclone_customer;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryLocationDetailActivity extends AppCompatActivity {

    TextView destName, destDes;
    ListHistoryLocation curLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_location_detail);

        destName = findViewById(R.id.destName);
        destDes = findViewById(R.id.destDes);
        if (getIntent().hasExtra("location")) {
            curLocation = (ListHistoryLocation) getIntent().getSerializableExtra("location");
            destName.setText(curLocation.getTitle());
            destDes.setText(curLocation.getDescript());
        }

    }
}