package com.arean.ClimateRelief.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arean.ClimateRelief.R;
import com.arean.ClimateRelief.model.recyclerViewAdapter;
import com.arean.ClimateRelief.model.recyclerViewModel;
import com.arean.ClimateRelief.ui.activity.UserProfileActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FormFillUpActivity extends AppCompatActivity {


    private static final String TAG = "FormFillUpActivity";
    private Spinner spinnerDivision, spinnerDistrict, spinnerUpazilla, spinnerUnion;
    DatabaseReference databaseReference;
    List<String> divisionNames=new ArrayList<>();
    List<String> spinnerDivisionArrayList, spinnerDistrictArrayList, spinnerUpazillaArrayList, spinnerUnionArrayList;
    ArrayAdapter<String>arrayAdapter;
    ArrayAdapter<String> spinnerDivisionArrayAdapter,spinnerDistrictArrayAdapter,spinnerUpazillaArrayAdapter, spinnerUnionArrayAdapter;
    String divisionID;

    String userDivision, userDistrict, userUpazilla, userUnion, userFemaleCount, userChildrenCount, userSeniorCitizenCount, userDomesticAnimalPresence, userLocationLatitude, userLocationLongitude, userNIDNo, userBkashContactNo;
    EditText editTextSubmitUserFemaleCount, editTextSubmitUserChildrenCount, editTextSubmitUserSeniorCitizenCount, editTextSubmitUserBkashContactNo, editTextSubmitUserNIDNo;
    RadioGroup userAnimalPresenceRadioGroup;
    RadioButton userAnimalPresenceRadioButton;
    Button button_getLocation;
    TextView textView_LatitudeCoordinate, textView_LongitudeCoordinate;
    private FirebaseAuth authProfile;


    private FusedLocationProviderClient fusedLocationProviderClient;
    String userID,userName;


    RecyclerView recyclerView;
    ArrayList<recyclerViewModel> recyclerViewModels;
    recyclerViewAdapter recyclerViewAdapter;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_fill_up);
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        authProfile= FirebaseAuth.getInstance();

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.navigation_claim);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), MainActivityweather.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_claim:
                        startActivity(new Intent(getApplicationContext(),FormFillUpActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_donate:
                        startActivity(new Intent(getApplicationContext(), ClaimerListActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_account:
                        if (authProfile.getCurrentUser()!=null)
                        {
                            startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                        }
                        else
                        {
                            startActivity(new Intent(getApplicationContext(), AccountActivity.class));

                        }
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


        recyclerView = findViewById(R.id.recycler_view_claim_form);
        Integer[] stepImages = {R.drawable.steps, R.drawable.steps, R.drawable.steps,R.drawable.steps,R.drawable.steps,R.drawable.steps} ;
        String [] stepNames = {"Step 1", "Step 2","Step 3","Step 4","Step 5","Step 6"};

        recyclerViewModels = new ArrayList<>();
        for(int i=0; i<stepImages.length; i++)
        {
            recyclerViewModel tempModel = new recyclerViewModel(stepImages[i], stepNames[i]);
            recyclerViewModels.add(tempModel);

        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(FormFillUpActivity.this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerViewAdapter = new recyclerViewAdapter(FormFillUpActivity.this, recyclerViewModels);
        recyclerView.setAdapter(recyclerViewAdapter);


        spinnerDivision = findViewById(R.id.spinnerDivision);
        spinnerDivisionArrayList = new ArrayList<>();
        spinnerDivisionArrayAdapter = new ArrayAdapter<String>(FormFillUpActivity.this, android.R.layout.simple_spinner_item, spinnerDivisionArrayList);
        spinnerDivision.setAdapter(spinnerDivisionArrayAdapter);

        getDataIntoSpinnerDivisionFromFirebase();

        spinnerDistrict = findViewById(R.id.spinnerDistrict);
        spinnerDistrictArrayList = new ArrayList<>();
        spinnerDistrictArrayAdapter = new ArrayAdapter<String>(FormFillUpActivity.this, android.R.layout.simple_spinner_item, spinnerDistrictArrayList);
        spinnerDistrict.setAdapter(spinnerDistrictArrayAdapter);


        spinnerUpazilla = findViewById(R.id.spinnerUpazilla);
        spinnerUpazillaArrayList = new ArrayList<>();
        spinnerUpazillaArrayAdapter = new ArrayAdapter<String>(FormFillUpActivity.this, android.R.layout.simple_spinner_item, spinnerUpazillaArrayList);
        spinnerUpazilla.setAdapter(spinnerUpazillaArrayAdapter);



        spinnerUnion = findViewById(R.id.spinnerUnion);
        spinnerUnionArrayList = new ArrayList<>();
        spinnerUnionArrayAdapter = new ArrayAdapter<String>(FormFillUpActivity.this, android.R.layout.simple_spinner_item, spinnerUnionArrayList);
        spinnerUnion.setAdapter(spinnerUnionArrayAdapter);



        spinnerDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ((TextView) spinnerDivision.getSelectedView()).setTextColor(Color.BLACK);
                getDataIntoSpinnerDistrict();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) spinnerDistrict.getSelectedView()).setTextColor(Color.BLACK);
                getDataIntoSpinnerUpazilla();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerUpazilla.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) spinnerUpazilla.getSelectedView()).setTextColor(Color.BLACK);
                getDataIntoSpinnerUnion();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerUnion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) spinnerUnion.getSelectedView()).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button_getLocation = findViewById(R.id.button_getLocation);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(FormFillUpActivity.this);

        button_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(FormFillUpActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FormFillUpActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                {
                    getCurrentLocation();

                }
                else
                {
                    ActivityCompat.requestPermissions(FormFillUpActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

                }
            }
        });


        Button buttonSubmit = findViewById(R.id.button_submit_relief_form);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userDivision = spinnerDivision.getSelectedItem().toString().replaceAll("[0-9]", "");
                userDistrict = spinnerDistrict.getSelectedItem().toString().replaceAll("[0-9]", "");
                userUpazilla = spinnerUpazilla.getSelectedItem().toString().replaceAll("[0-9]", "");
                userUnion = spinnerUnion.getSelectedItem().toString().replaceAll("[0-9]", "");
                editTextSubmitUserFemaleCount = findViewById(R.id.editText_register_female_count);
                editTextSubmitUserChildrenCount = findViewById(R.id.editText_register_children_count);
                editTextSubmitUserSeniorCitizenCount = findViewById(R.id.editText_register_senior_citizen_count);
                editTextSubmitUserBkashContactNo = findViewById(R.id.editText_register_bkashContactNo);
                editTextSubmitUserNIDNo = findViewById(R.id.editText_register_nid);
                textView_LatitudeCoordinate = findViewById(R.id.textView_LatitudeCoordinate);
                textView_LongitudeCoordinate= findViewById(R.id.textView_LongitudeCoordinate);

                userFemaleCount = editTextSubmitUserFemaleCount.getText().toString();
                userChildrenCount = editTextSubmitUserChildrenCount.getText().toString();
                userSeniorCitizenCount = editTextSubmitUserSeniorCitizenCount.getText().toString();
                userAnimalPresenceRadioGroup = (RadioGroup) findViewById(R.id.radio_group_register_domestic_animal_presence);
                int selectedID = userAnimalPresenceRadioGroup.getCheckedRadioButtonId();
                userAnimalPresenceRadioButton = (RadioButton) findViewById(selectedID);
                if(selectedID<=0)
                {
                    Toast.makeText(FormFillUpActivity.this, "Please let us know if you have domestic animal or not", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    userDomesticAnimalPresence = userAnimalPresenceRadioButton.getText().toString();

                }

                userLocationLatitude = textView_LatitudeCoordinate.getText().toString().replaceAll("[^0-9.]+", "");
                System.out.println(userLocationLatitude);
                userLocationLongitude = textView_LongitudeCoordinate.getText().toString().replaceAll("[^0-9.]+", "");
                userBkashContactNo = editTextSubmitUserBkashContactNo.getText().toString();
                userNIDNo = editTextSubmitUserNIDNo.getText().toString();

                if(TextUtils.isEmpty(userBkashContactNo) || userBkashContactNo.length()!=11)
                {
                    Toast.makeText(FormFillUpActivity.this, "Bkash Contact No must be specified and must be 11 digits long", Toast.LENGTH_SHORT).show();
                    editTextSubmitUserBkashContactNo.requestFocus();
                }
                else if(TextUtils.isEmpty(userNIDNo) || userNIDNo.length()!=10)
                {
                    Toast.makeText(FormFillUpActivity.this, "NID No cannot be empty and must be 10 digit long", Toast.LENGTH_SHORT).show();
                    editTextSubmitUserNIDNo.setError("NID required");
                    editTextSubmitUserNIDNo.requestFocus();

                }

                else if(Objects.equals(userLocationLatitude, "0.0") && Objects.equals(userLocationLongitude, "0.0"))
                {
                    Toast.makeText(FormFillUpActivity.this, "Specify your location", Toast.LENGTH_SHORT).show();


                }

                else if(TextUtils.isEmpty(userChildrenCount))
                {
                    Toast.makeText(FormFillUpActivity.this, "Please specify number of children", Toast.LENGTH_SHORT).show();
                    editTextSubmitUserChildrenCount.setError("Children Count Required");
                    editTextSubmitUserChildrenCount.requestFocus();

                }

                else if(TextUtils.isEmpty(userFemaleCount))
                {
                    Toast.makeText(FormFillUpActivity.this, "Please specify number of females", Toast.LENGTH_SHORT).show();
                    editTextSubmitUserFemaleCount.setError("Female Count Required");
                    editTextSubmitUserFemaleCount.requestFocus();

                }

                else if(TextUtils.isEmpty(userSeniorCitizenCount))
                {
                    Toast.makeText(FormFillUpActivity.this, "Please specify number of senior citizens", Toast.LENGTH_SHORT).show();
                    editTextSubmitUserSeniorCitizenCount.setError("Senior citizen Count Required");
                    editTextSubmitUserSeniorCitizenCount.requestFocus();

                }


                else if(authProfile.getCurrentUser()==null)
                {
                    Toast.makeText(FormFillUpActivity.this, "You must login before submit", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    storeDataIntoFirebase(userDivision, userDistrict, userUpazilla, userUnion, userFemaleCount, userChildrenCount, userSeniorCitizenCount, userDomesticAnimalPresence, userLocationLatitude, userLocationLongitude, userNIDNo, userBkashContactNo);

                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            getCurrentLocation();
        } else {
            Toast.makeText(FormFillUpActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    Location location = task.getResult();

                    if(location != null)
                    {
                        textView_LatitudeCoordinate = findViewById(R.id.textView_LatitudeCoordinate);
                        textView_LatitudeCoordinate.setText("Latitude: " + String.valueOf(location.getLatitude()));

                        textView_LongitudeCoordinate = findViewById(R.id.textView_LongitudeCoordinate);
                        textView_LongitudeCoordinate.setText("Longitude: " + String.valueOf(location.getLongitude()));

                    }

                    else
                    {
                        LocationRequest locationRequest = new LocationRequest().setPriority(Priority.PRIORITY_HIGH_ACCURACY).setInterval(10000).setFastestInterval(1000).setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {

                                Location location1 = locationResult.getLastLocation();
                                textView_LatitudeCoordinate.setText("Latitude: " + String.valueOf(location1.getLatitude()));
                                textView_LongitudeCoordinate.setText("Longitude: " + String.valueOf(location1.getLongitude()));

                            }
                        };

                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());


                    }



                }
            });
        }

        else
        {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void getDataIntoSpinnerUpazilla() {

        String selectedValueFromSpinnerDistrict = spinnerDistrict.getSelectedItem().toString().replaceAll("[^0-9]", "");


        ValueEventListener listener;

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference().child("upazilla");

        listener = rootRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                spinnerUpazillaArrayList.clear();
                for(DataSnapshot upazillaKey: snapshot.getChildren())
                {
                    if(Objects.equals(upazillaKey.child("district_id").getValue(String.class), selectedValueFromSpinnerDistrict))
                    {
                        String upazillaName = upazillaKey.child("name").getValue(String.class);
                        String upazillaID = upazillaKey.child("id").getValue(String.class);
                        spinnerUpazillaArrayList.add(upazillaID+" "+upazillaName);

                    }
                }
                spinnerUpazillaArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getDataIntoSpinnerUnion() {

        String selectedValueFromSpinnerUpazilla = spinnerUpazilla.getSelectedItem().toString().replaceAll("[^0-9]", "");


        ValueEventListener listener;

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference().child("union");

        listener = rootRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                spinnerUnionArrayList.clear();
                for(DataSnapshot unionKey: snapshot.getChildren())
                {
                    if(Objects.equals(unionKey.child("upazilla_id").getValue(String.class), selectedValueFromSpinnerUpazilla))
                    {
                        String unionName = unionKey.child("name").getValue(String.class);
                        String unionID = unionKey.child("id").getValue(String.class);
                        spinnerUnionArrayList.add(unionID+" "+unionName);

                    }
                }
                spinnerUnionArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getDataIntoSpinnerDivisionFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("division");

        ValueEventListener listener = databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                spinnerDivisionArrayList.clear();

                for (DataSnapshot divisionDetails : snapshot.getChildren()) {

                    String divisionNameKey = divisionDetails.child("name").getValue(String.class);
                    divisionID = divisionDetails.child("id").getValue(String.class);
                    spinnerDivisionArrayList.add(divisionID+" "+divisionNameKey);

                }

                spinnerDivisionArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });


    }

    public void getDataIntoSpinnerDistrict()
    {

        String selectedValueFromSpinnerDivision = spinnerDivision.getSelectedItem().toString().replaceAll("[^0-9]", "");


        ValueEventListener listener;

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference().child("districts");

        listener = rootRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                spinnerDistrictArrayList.clear();
                for(DataSnapshot districtKey: snapshot.getChildren())
                {
                    if(Objects.equals(districtKey.child("division_id").getValue(String.class), selectedValueFromSpinnerDivision))
                    {
                        String districtName = districtKey.child("name").getValue(String.class);
                        String districtID = districtKey.child("id").getValue(String.class);
                        spinnerDistrictArrayList.add(districtID+" " +districtName);

                    }
                }
                spinnerDistrictArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void storeDataIntoFirebase(String userDivision, String userDistrict, String userUpazilla, String userUnion, String userFemaleCount, String userChildrenCount, String userSeniorCitizenCount, String userDomesticAnimalPresence, String userLocationLatitude, String userLocationLongitude, String userNIDNo, String userBkashContactNo) {

        FirebaseAuth authProfile = FirebaseAuth.getInstance();
        FirebaseFirestore fstore = FirebaseFirestore.getInstance();

        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        userID  = firebaseUser.getUid();
        userName = firebaseUser.getDisplayName();
        DocumentReference documentReference = fstore.collection("Claimed Users").document(userID);
        Map<String, Object> user = new HashMap<>();
        user.put("userName", userName);
        user.put("userDivision", userDivision);
        user.put("userDistrict", userDistrict);
        user.put("userUpazilla", userUpazilla);
        user.put("userUnion", userUnion);
        user.put("userFemaleCount", userFemaleCount);
        user.put("userChildrenCount", userChildrenCount);
        user.put("userSeniorCitizenCount", userSeniorCitizenCount);
        user.put("userDomesticAnimalPresence", userDomesticAnimalPresence);
        user.put("userLocationLatitude", userLocationLatitude);
        user.put("userLocationLongitude", userLocationLongitude);
        user.put("userNIDNo", userNIDNo);
        user.put("userBkashContactNo", userBkashContactNo);

        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(FormFillUpActivity.this, "User information submitted for userID: " + userID , Toast.LENGTH_SHORT).show();

            }
        });







    }









}