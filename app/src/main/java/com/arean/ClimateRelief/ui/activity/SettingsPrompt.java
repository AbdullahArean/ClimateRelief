package com.arean.ClimateRelief.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.arean.ClimateRelief.R;
import com.arean.ClimateRelief.ui.AccountActivity;
import com.arean.ClimateRelief.ui.ClaimerListActivity;
import com.arean.ClimateRelief.ui.FormFillUpActivity;
import com.arean.ClimateRelief.ui.MainActivityweather;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsPrompt extends AppCompatActivity {
    FirebaseAuth authProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_prompt);
        authProfile = FirebaseAuth.getInstance();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_account);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(getApplicationContext(), MainActivityweather.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.navigation_claim:
                    startActivity(new Intent(getApplicationContext(), FormFillUpActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.navigation_donate:
                    startActivity(new Intent(getApplicationContext(), ClaimerListActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.navigation_account:
                    if (authProfile.getCurrentUser() != null) {
                        startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                    } else {
                        startActivity(new Intent(getApplicationContext(), AccountActivity.class));

                    }
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });
        findViewById(R.id.button_myaccount).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), LogInSignUpPrompt.class)));
        if (authProfile.getCurrentUser() != null) findViewById(R.id.button_myaccount).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), UserProfileActivity.class)));
        findViewById(R.id.button_cu).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ContactUsActivity.class)));
        findViewById(R.id.button_cu).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ContactUsActivity.class)));
        findViewById(R.id.button_cu).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ContactUsActivity.class)));
        findViewById(R.id.button_cu).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ContactUsActivity.class)));

    }
}