package com.arean.ClimateRelief.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.arean.ClimateRelief.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.A;

public class UserProfileActivity extends AppCompatActivity {


    private TextView textViewWelcome, textViewFullName, textViewEmail, textViewDoB, textViewGender, textViewMobile;

    private String fullName, email, doB, gender, mobile;
    private ImageView imageView;
    private FirebaseAuth authProfile;
    private Button btnLogOut;

    String settings[] = {"Settings", "Languages", "Enable Dark Mode", "Privacy Policy"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    String support[] = {"Help", "Contact Us", "About Us"};
    AutoCompleteTextView autoCompleteTextViewforHelpandSupport;
    ArrayAdapter<String> adapterItemsforHelpandSupport;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        textViewWelcome = findViewById(R.id.textView_show_welcome);
        textViewFullName = findViewById(R.id.textView_show_full_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewDoB = findViewById(R.id.textView_show_dob);
        textViewGender = findViewById(R.id.textView_show_gender);
        textViewMobile = findViewById(R.id.textView_show_mobile);
        btnLogOut = findViewById(R.id.buttonLogOut);


        imageView = findViewById(R.id.imageView_profile_dp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, UploadProfilePicActivity.class);
                startActivity(intent);
            }
        });





        autoCompleteTextView = findViewById(R.id.autocompletetextView);
        adapterItems = new ArrayAdapter<String>(UserProfileActivity.this, R.layout.settings_dropdown_item, settings);
        autoCompleteTextView.setAdapter(adapterItems);


        autoCompleteTextViewforHelpandSupport = findViewById(R.id.autocompletetextViewforHelpandSupport);
        adapterItemsforHelpandSupport = new ArrayAdapter<String>(UserProfileActivity.this, R.layout.helpsupport_dropdown_item, support);
        autoCompleteTextViewforHelpandSupport.setAdapter(adapterItemsforHelpandSupport);


        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

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
//
        else if(id == R.id.menu_update_email)
        {
            Intent intent = new Intent(UserProfileActivity.this, UpdateEmailActivity.class);
            startActivity(intent);

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
}