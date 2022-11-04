package com.arean.ClimateRelief.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.arean.ClimateRelief.R;
import com.arean.ClimateRelief.ui.AccountActivity;
import com.arean.ClimateRelief.ui.FormFillUpActivity;
import com.arean.ClimateRelief.ui.MainActivityweather;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {


    private TextView textViewWelcome, textViewFullName, textViewEmail, textViewDoB, textViewGender, textViewMobile;

    private String fullName, email, doB, gender, mobile;
    private ImageView imageView;
    private FirebaseAuth authProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_account);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch(item.getItemId())
            {
                case R.id.navigation_home:
                    startActivity(new Intent(getApplicationContext(), MainActivityweather.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigation_claim:
                    startActivity(new Intent(getApplicationContext(), FormFillUpActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigation_donate:
                    return true;
                case R.id.navigation_account:
                    return true;
            }
            return false;
        });


        textViewWelcome = findViewById(R.id.textView_show_welcome);
        textViewFullName = findViewById(R.id.textView_show_full_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewDoB = findViewById(R.id.textView_show_dob);
        textViewGender = findViewById(R.id.textView_show_gender);
        textViewMobile = findViewById(R.id.textView_show_mobile);
        Button btnLogOut = findViewById(R.id.buttonLogOut);


        imageView = findViewById(R.id.imageView_profile_dp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, UploadProfilePicActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.settings_from_profile).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), LogInSignUpPrompt.class)));
        if(firebaseUser == null)
        {
            Toast.makeText(UserProfileActivity.this, "Something went wrong. User's details are not available", Toast.LENGTH_SHORT).show();


        }

        else
        {
            showUserProfile(firebaseUser);
        }


        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                authProfile.signOut();
                Toast.makeText(UserProfileActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserProfileActivity.this,MainActivity.class );

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });






    }

    private void showUserProfile(FirebaseUser firebaseUser) {

        String userID = firebaseUser.getUid();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);

                if(readUserDetails != null )
                {
                    fullName = firebaseUser.getDisplayName();
                    email = firebaseUser.getEmail();
                    doB = readUserDetails.doB;
                    gender = readUserDetails.gender;
                    mobile = readUserDetails.mobile;


                    textViewWelcome.setText("Welcome "+ fullName+ "!");
                    textViewEmail.setText(email);
                    textViewFullName.setText(fullName);
                    textViewGender.setText(gender);
                    textViewDoB.setText(doB);
                    textViewMobile.setText(mobile);

                    Uri uri = firebaseUser.getPhotoUrl();

                    Picasso.with(UserProfileActivity.this).load(uri).into(imageView);


                }

                else
                {
                    Toast.makeText(UserProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
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
            Intent intent = new Intent(UserProfileActivity.this, UpdateProfileActivity.class);
            startActivity(intent);

        }
        else if(id == R.id.menu_update_email)
        {
            Intent intent = new Intent(UserProfileActivity.this, UpdateEmailActivity.class);
            startActivity(intent);

        }

        else if(id == R.id.menu_change_password)
        {
            Intent intent = new Intent(UserProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            finish();
        }

        else if(id == R.id.menu_logout)
        {
            authProfile.signOut();
            Toast.makeText(UserProfileActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserProfileActivity.this,MainActivity.class );

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();



        }
        else
        {
            Toast.makeText(UserProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserProfileActivity.this,MainActivity.class );
        startActivity(intent);
        finish();
    }
}