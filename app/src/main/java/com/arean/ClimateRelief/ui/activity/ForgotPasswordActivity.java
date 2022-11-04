package com.arean.ClimateRelief.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arean.ClimateRelief.R;
import com.arean.ClimateRelief.ui.MainActivityweather;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editTextPwdResetEmail;
    private final static String TAG = "ForgotPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextPwdResetEmail = findViewById(R.id.editText_password_reset_email);
        Button buttonPwdReset = findViewById(R.id.button_password_reset);

        buttonPwdReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextPwdResetEmail.getText().toString();
                
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter your registered mail", Toast.LENGTH_SHORT).show();
                    editTextPwdResetEmail.setError("Enter your email adress");
                    editTextPwdResetEmail.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter valid mail", Toast.LENGTH_SHORT).show();
                    editTextPwdResetEmail.setError("Valid Email is required");
                    editTextPwdResetEmail.requestFocus();
                }
                else
                {
                    resetPassword(email);
                }
            }
        });
    }

    private void resetPassword(String email)
    {
        FirebaseAuth authProfile = FirebaseAuth.getInstance();
        authProfile.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Please check your inbox for reset link", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ForgotPasswordActivity.this, MainActivityweather.class );

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }
                else
                {
                    try{
                        throw task.getException();
                    }
                    catch (FirebaseAuthInvalidUserException e)
                    {
                        editTextPwdResetEmail.setError("User does not exist or no longer valid. Register with valid email");
                    }
                    catch (Exception e)
                    {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(ForgotPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }


                }
            }
        });
    }
}