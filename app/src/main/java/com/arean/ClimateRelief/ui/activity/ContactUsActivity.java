package com.arean.ClimateRelief.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arean.ClimateRelief.R;
import com.arean.ClimateRelief.ui.fragment.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ContactUsActivity extends AppCompatActivity {

    EditText editTextContactEmail, editTextContactEmailSubject, editTextContactEmailMessage;
    Button buttonContactUsSendMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
//
        // Set Home selected
  bottomNavigationView.setSelectedItemId(R.id.navigation_account);
//
        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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
                        startActivity(new Intent(getApplicationContext(),ContactUsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
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
            Intent intent = new Intent(getApplicationContext(), ContactUsActivity.class);
            startActivity(intent);
        });
        Button buttonsettings = findViewById(R.id.buttonsettings);
        buttonsettings.setOnClickListener(view -> getSupportFragmentManager().beginTransaction().add(R.id.settings_container,new SettingsFragment()).commit());

        editTextContactEmail = findViewById(R.id.contact_us_email_address);
        editTextContactEmailSubject=findViewById(R.id.contact_us_email_subject);
        editTextContactEmailMessage=findViewById(R.id.contact_us_email_message);

        buttonContactUsSendMail=findViewById(R.id.contact_us_send_email_button);

        buttonContactUsSendMail.setOnClickListener(view -> {
            String recipient = editTextContactEmail.getText().toString();
            String subject = editTextContactEmailSubject.getText().toString();
            String message = editTextContactEmailMessage.getText().toString();
            sendEmail(recipient, subject, message);
        });


    }
    private void sendEmail(String recipient , String subject , String message)
    {
        Intent emailIntent = new Intent (Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {recipient});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try{
            startActivity(Intent.createChooser(emailIntent, "Choose an Email Content"));

        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
}