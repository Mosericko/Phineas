package com.mdevs.phineas.financemanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.mdevs.phineas.R;
import com.mdevs.phineas.loginregistration.UserProfile;

public class FinanceActivity extends AppCompatActivity {
    RelativeLayout profile;
    CardView pending, approved, feedback, rejected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);

        profile = findViewById(R.id.profile);

        profile.setOnClickListener(v -> {
            startActivity(new Intent(this, UserProfile.class));
        });

    }
}