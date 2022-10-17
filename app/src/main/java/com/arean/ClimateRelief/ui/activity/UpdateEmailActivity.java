package com.arean.ClimateRelief.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arean.ClimateRelief.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateEmailActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private TextView textViewAuthenticated;
    private String userOldEmail, userNewEmail, userPwd;
    private Button buttonUpdateEmail;
    private EditText editTextNewEmail, editTextPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        editTextPwd = findViewById(R.id.editText_update_email_verify_password);
        editTextNewEmail = findViewById(R.id.editText_update_email_new);
        textViewAuthenticated = findViewById(R.id.textView_update_email_authenticated);
        buttonUpdateEmail = findViewById(R.id.button_update_email);
        buttonUpdateEmail.setEnabled(false);
        editTextNewEmail.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        userOldEmail = firebaseUser.getEmail();
        TextView textViewOldEmail = findViewById(R.id.textView_update_email_old);
        textViewOldEmail.setText(userOldEmail);

        if(firebaseUser.equals(""))
        {
            Toast.makeText(UpdateEmailActivity.this, "User details are not available", Toast.LENGTH_SHORT).show();

        }
        else
        {
            reAuthenticate(firebaseUser);
        }

    }

    private void reAuthenticate(FirebaseUser firebaseUser )
    {
        Button buttonVerifyUser = findViewById(R.id.button_authenticate_user);
        buttonVerifyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userPwd = editTextPwd.getText().toString();
                if(TextUtils.isEmpty(userPwd))
                {
                    Toast.makeText(UpdateEmailActivity.this, "Password is needed to continue", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    AuthCredential credential = EmailAuthProvider.getCredential(userOldEmail, userPwd);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(UpdateEmailActivity.this, "Password has been verified. You can update email now", Toast.LENGTH_SHORT).show();
                            textViewAuthenticated.setText("You are authenticated. You can update email now");

                            editTextNewEmail.setEnabled(true);
                            editTextPwd.setEnabled(false);
                            buttonVerifyUser.setEnabled(false);
                            buttonUpdateEmail.setEnabled(true);


                            buttonUpdateEmail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    userNewEmail = editTextNewEmail.getText().toString();
                                    if(TextUtils.isEmpty(userNewEmail))
                                    {
                                        Toast.makeText(UpdateEmailActivity.this, "New Email is required", Toast.LENGTH_SHORT).show();

                                    }
                                    else if(!Patterns.EMAIL_ADDRESS.matcher(userNewEmail).matches())
                                    {
                                        Toast.makeText(UpdateEmailActivity.this, "Valid Email is required", Toast.LENGTH_SHORT).show();

                                    }
                                    else if(userOldEmail.matches(userNewEmail))
                                    {
                                        Toast.makeText(UpdateEmailActivity.this, "New Email cannot be same as old email", Toast.LENGTH_SHORT).show();
                                    }

                                    else
                                    {
                                        updateEmail(firebaseUser);

                                    }
                                }
                            });

                        }
                    });
                }

            }
        });
    }


    private void updateEmail(FirebaseUser firebaseUser)
    {

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
            Intent intent = new Intent(UpdateEmailActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
            finish();

        }
//
        else if(id == R.id.menu_update_email)
        {
            Intent intent = new Intent(UpdateEmailActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
            finish();

        }
//        else if(id == R.id.menu_settings)
//        {
//            Toast.makeText(UserProfileActivity.this, "menu settings", Toast.LENGTH_SHORT).show();
//
//        }
//
//        else if(id == R.id.menu_change_password)
//        {
//            Intent intent = new Intent(UserProfileActivity.this, ChangePasswordActivity.class);
//            startActivity(intent);
//        }
//
//        else if(id == R.id.menu_delete_profile)
//        {
//            Intent intent = new Intent(UserProfileActivity.this, DeleteProfileActivity.class);
//            startActivity(intent);
//
//        }

        else if(id == R.id.menu_logout)
        {
            authProfile.signOut();
            Toast.makeText(UpdateEmailActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateEmailActivity.this,MainActivity.class );

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();



        }
        else
        {
            Toast.makeText(UpdateEmailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}