package com.arean.ClimateRelief;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {


    private TextView textViewWelcome, textViewFullName, textViewEmail, textViewDoB, textViewGender, textViewMobile;

    private String fullName, email, doB, gender, mobile;
    private ImageView imageView;
    private FirebaseAuth authProfile;

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



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}