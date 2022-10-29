package com.arean.ClimateRelief.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.arean.ClimateRelief.R;
import com.arean.ClimateRelief.ui.fragment.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountActivity extends AppCompatActivity {

    EditText editTextContactEmail, editTextContactEmailSubject, editTextContactEmailMessage;
    Button buttonContactUsSendMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
//
        // Set Home selected
  bottomNavigationView.setSelectedItemId(R.id.navigation_account);
//
        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch(item.getItemId())
            {
                case R.id.navigation_home:
                    startActivity(new Intent(getApplicationContext(),MainActivityweather.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigation_claim:
                    startActivity(new Intent(getApplicationContext(),FormFillUpActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigation_donate:
                    return true;
                case R.id.navigation_account:
                    startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });




        Button buttonRegisterFromAccount = (Button) findViewById(R.id.buttonRegisterFromAccount);

        buttonRegisterFromAccount.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });
        Button buttonLoginFromAccount = (Button)findViewById(R.id.buttonLoginFromAccount);

        buttonLoginFromAccount.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

        });
        Button buttoncontactus = (Button)findViewById(R.id.button_contactus);

        buttoncontactus.setOnClickListener(view -> {
            //Contact Us Not Working
//            Intent intent = new Intent(getApplicationContext(), ContactUsActivity.class);
//            startActivity(intent);
            startActivity(new Intent(getApplicationContext(), ContactUsActivity.class));
        });
        Button buttonsettings = findViewById(R.id.buttonsettings);
        buttonsettings.setOnClickListener(view -> getSupportFragmentManager().beginTransaction().add(R.id.settings_container,new SettingsFragment()).commit());
        
    }

}