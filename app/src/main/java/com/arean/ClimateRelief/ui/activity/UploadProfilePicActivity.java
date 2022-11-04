package com.arean.ClimateRelief.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.arean.ClimateRelief.R;
import com.arean.ClimateRelief.ui.MainActivityweather;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadProfilePicActivity extends AppCompatActivity {

    private ImageView imageViewUploadPic;
    private FirebaseAuth authProfile;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private static final int PICK_IMAGE_REQUEST =1;
    private Uri uriImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_pic);

        Button buttonUploadPicChoose = findViewById(R.id.upload_pic_choose_button);
        Button buttonUploadPic = findViewById(R.id.upload_pic_button);
        imageViewUploadPic = (ImageView) findViewById(R.id.imageView_profile_dp);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference("DisplayPics");

        Uri uri = firebaseUser.getPhotoUrl();

        Picasso.with(UploadProfilePicActivity.this).load(uri).into(imageViewUploadPic);

        buttonUploadPicChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        buttonUploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadPic();
            }
        });



    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImage = data.getData();
            imageViewUploadPic.setImageURI(uriImage);
        }
    }

    private void UploadPic(){
        if(uriImage !=null)
        {
            StorageReference fileReference = storageReference.child(authProfile.getCurrentUser().getUid()+"."+getFileExtension(uriImage));


            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;
                            firebaseUser = authProfile.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(downloadUri).build();
                            firebaseUser.updateProfile(profileUpdates);

                        }
                    });

                    Intent intent = new Intent(UploadProfilePicActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        else
        {
            Toast.makeText(UploadProfilePicActivity.this, "No Profile Pic Selected", Toast.LENGTH_SHORT).show();

        }
    }


    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
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
            Intent intent = new Intent(UploadProfilePicActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
            finish();

        }
        else if(id == R.id.menu_update_email)
        {
            Intent intent = new Intent(UploadProfilePicActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
            finish();

        }

        else if(id == R.id.menu_change_password)
        {
            Intent intent = new Intent(UploadProfilePicActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            finish();
        }

            else if(id == R.id.menu_logout)
            {
                authProfile.signOut();
                Toast.makeText(UploadProfilePicActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UploadProfilePicActivity.this, MainActivityweather.class );

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();



            }
            else
            {
                Toast.makeText(UploadProfilePicActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
            return super.onOptionsItemSelected(item);
        }

    }
