package com.arean.ClimateRelief.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arean.ClimateRelief.R;

public class ContactUsActivity extends AppCompatActivity {

    EditText editTextContactEmail, editTextContactEmailSubject, editTextContactEmailMessage;
    Button buttonContactUsSendMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
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