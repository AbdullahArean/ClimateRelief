package com.arean.ClimateRelief.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.arean.ClimateRelief.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ChangePasswordActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private EditText editTextPwdCurr, editTextPwdNew, getEditTextPwdConfirmNew;
    private TextView textViewAuthenticated;
    private Button buttonChangePwd, buttonReAuthenticate;
    private ProgressBar progressBar;
    private String userPwdCurr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editTextPwdCurr = findViewById(R.id.editText_change_pwd_current);
        editTextPwdNew = findViewById(R.id.editText_change_pwd_new);
        getEditTextPwdConfirmNew = findViewById(R.id.editText_confirm_change_pwd_new);
        textViewAuthenticated = findViewById(R.id.textView_change_pwd_authenticated);
        buttonChangePwd = findViewById(R.id.button_change_pwd);
        buttonReAuthenticate = findViewById(R.id.button_change_pwd_authenticate);


        editTextPwdNew.setEnabled(false);
        editTextPwdCurr.setEnabled(true);
        buttonChangePwd.setEnabled(false);


        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser.equals(""))
        {
            Toast.makeText(ChangePasswordActivity.this, "Something is wrong. User Details are not available", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChangePasswordActivity.this, UserProfileActivity.class);
            startActivity(intent);
            finish();
        }

        else
        {
            reAuthenticateUser(firebaseUser);
        }

    }

    private void reAuthenticateUser(FirebaseUser firebaseUser) {
        buttonReAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPwdCurr = editTextPwdCurr.getText().toString();

                if(TextUtils.isEmpty(userPwdCurr)){
                    Toast.makeText(ChangePasswordActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
                    editTextPwdCurr.setError("Please enter your password to authenticate");
                    editTextPwdCurr.requestFocus();
                }
                else
                {
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), userPwdCurr);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                editTextPwdCurr.setEnabled(false);
                                editTextPwdNew.setEnabled(true);
                                getEditTextPwdConfirmNew.setEnabled(true);

                                buttonReAuthenticate.setEnabled(false);
                                buttonChangePwd.setEnabled(true);

                                textViewAuthenticated.setText("You are authenticated. You can change your password now");
                                Toast.makeText(ChangePasswordActivity.this, "Password has been verified"+"Change password now", Toast.LENGTH_SHORT).show();

                                buttonChangePwd.setBackgroundTintList(ContextCompat.getColorStateList(ChangePasswordActivity.this, R.color.dark_green));

                                buttonChangePwd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        changePwd(firebaseUser);
                                    }
                                });


                            }

                            else
                            {
                                try {
                                    throw task.getException();
                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });

    }

    public void changePwd(FirebaseUser firebaseUser)
    {
        String userPwdNew = editTextPwdNew.getText().toString();
        String userPwdConfirmNew = getEditTextPwdConfirmNew.getText().toString();

        if(TextUtils.isEmpty(userPwdNew))
        {
            Toast.makeText(ChangePasswordActivity.this, "New Password is required", Toast.LENGTH_SHORT).show();
            editTextPwdNew.setError("Please enter your new password");
            editTextPwdNew.requestFocus();
        }

        else if(TextUtils.isEmpty(userPwdConfirmNew))
        {
            Toast.makeText(ChangePasswordActivity.this, "Please confirm your new password", Toast.LENGTH_SHORT).show();
            getEditTextPwdConfirmNew.setError("Please re-enter your new password");
            getEditTextPwdConfirmNew.requestFocus();

        }

        else if(!userPwdNew.matches(userPwdConfirmNew))
        {
            Toast.makeText(ChangePasswordActivity.this, "Password did not match", Toast.LENGTH_SHORT).show();
            getEditTextPwdConfirmNew.setError("Please re-enter same password");
            getEditTextPwdConfirmNew.requestFocus();

        }
        else if(userPwdCurr.matches(userPwdNew))
        {
            Toast.makeText(ChangePasswordActivity.this, "New password cannot be same as old password", Toast.LENGTH_SHORT).show();
            editTextPwdNew.setError("Please enter a new password");
            editTextPwdNew.requestFocus();
        }

        else
        {
            firebaseUser.updatePassword(userPwdNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(ChangePasswordActivity.this, "Password has been changed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePasswordActivity.this, UserProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        try {
                            throw task.getException();
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.menu_refresh)
        {
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }

        else if(id == R.id.menu_update_profile)
        {
            Intent intent = new Intent(ChangePasswordActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
            finish();

        }
//
        else if(id == R.id.menu_update_email)
        {
            Intent intent = new Intent(ChangePasswordActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
            finish();

        }

        else if(id == R.id.menu_change_password)
        {
            Intent intent = new Intent(ChangePasswordActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        }


        else if(id == R.id.menu_logout)
        {
            authProfile.signOut();
            Toast.makeText(ChangePasswordActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChangePasswordActivity.this,MainActivity.class );

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(ChangePasswordActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
