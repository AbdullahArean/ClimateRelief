package com.arean.ClimateRelief.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.arean.ClimateRelief.R;
import com.arean.ClimateRelief.ui.activity.LogInSignUpPrompt;
import com.arean.ClimateRelief.ui.activity.SettingsPrompt;
import com.arean.ClimateRelief.ui.activity.UserProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authProfile = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        findViewById(R.id.button_login).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), LogInSignUpPrompt.class)));
        findViewById(R.id.imageView4).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), LogInSignUpPrompt.class)));
        findViewById(R.id.button_login2).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), SettingsPrompt.class)));
        findViewById(R.id.profile).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), LogInSignUpPrompt.class)));

    }
}