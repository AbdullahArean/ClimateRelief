package com.arean.ClimateRelief;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLoginEmail, editTextLoginPwd;

    private FirebaseAuth authProfile;
    private ImageView imageViewShowHidePwd;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextLoginEmail = findViewById(R.id.editText_login_email);
        editTextLoginPwd = findViewById(R.id.editText_login_pwd);

        authProfile = FirebaseAuth.getInstance();

        Button buttonForgotPassword = findViewById(R.id.button_forgot_password);
        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "You can reset your password now", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));

            }
        });


//        imageViewShowHidePwd = (ImageView)findViewById(R.id.imageView_show_hide_pwd);
////        imageViewShowHidePwd.setImageDrawable(getResources().getDrawable(R.drawable.ic_show_pwd));
//        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(editTextLoginPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
//                {
//                    editTextLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
//                }
//                else
//                {
//                    editTextLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    imageViewShowHidePwd.setImageResource(R.drawable.ic_show_pwd);
//
//                }
//            }
//        });



        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = editTextLoginEmail.getText().toString();
                String textPwd = editTextLoginPwd.getText().toString();

                if(TextUtils.isEmpty(textEmail))
                {
                    Toast.makeText(LoginActivity.this, "Please enter your mail", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Email is required");
                    editTextLoginEmail.requestFocus();
                }

                else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches())
                {
                    Toast.makeText(LoginActivity.this, "Please enter valid mail", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Valid Email is required");
                    editTextLoginEmail.requestFocus();

                }

                else if(TextUtils.isEmpty(textPwd))
                {
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    editTextLoginPwd.setError("Password is required");
                    editTextLoginPwd.requestFocus();

                }

                else

                {
                    loginUser(textEmail, textPwd);
                }

            }
        });


//        Button buttonRegister = findViewById(R.id.buttonRegister);
//        buttonRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(intent);
//            }
//        });

//        Button buttonToreliefForm = findViewById(R.id.buttonToreliefForm);
//        buttonToreliefForm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, FormFillupActivity.class);
//                startActivity(intent);
//            }
//        });

    }

    private void loginUser(String email, String pwd) {

        authProfile.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser firebaseUser = authProfile.getCurrentUser();
                    if(firebaseUser.isEmailVerified())
                    {
                        Toast.makeText(LoginActivity.this, "Log in Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
                        finish();
                    }
                    else
                    {
                        firebaseUser.sendEmailVerification();
                        authProfile.signOut();
                        showAlertDialogBox();
                    }

                }
                else
                {
                    try{
                        throw task.getException();
                    }
                    catch(FirebaseAuthInvalidUserException e)
                    {
                        editTextLoginEmail.setError("User does not exists or is no longer valid. Register again");
                        editTextLoginEmail.requestFocus();
                    }
                    catch(FirebaseAuthInvalidCredentialsException e)
                    {
                        editTextLoginEmail.setError("Invalid credentials. Check and re-enter");
                        editTextLoginEmail.requestFocus();
                    }

                    catch(Exception e)
                    {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showAlertDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email first. You cannot log in without email verification");

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(authProfile.getCurrentUser()!=null)
        {
            Toast.makeText(LoginActivity.this, "You are already logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
            finish();
        }
        else
        {
            Toast.makeText(LoginActivity.this, "You can login now", Toast.LENGTH_SHORT).show();
        }
    }
}