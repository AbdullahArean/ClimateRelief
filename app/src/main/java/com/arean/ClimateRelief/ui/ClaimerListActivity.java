package com.arean.ClimateRelief.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.arean.ClimateRelief.R;
import com.arean.ClimateRelief.databinding.ActivityClaimerListBinding;
import com.arean.ClimateRelief.ui.activity.ClaimerInfo;
import com.arean.ClimateRelief.ui.activity.ClaimerListAdapterNew;
import com.arean.ClimateRelief.ui.activity.UserProfileActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ClaimerListActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "ClaimerListActivity";
    private GoogleMap mMap;//
    ArrayList<LatLng> arrayList = new ArrayList<LatLng>();//
    TextView userName, userDistrict, userFemaleCount, userSeniorCitizenCount, userNIDNo, userBkashContactNo;

    RecyclerView recyclerView_claimer_info;
    ArrayList<ClaimerInfo> claimerInfoArrayList;
    ClaimerListAdapterNew claimerListAdapter;
    FirebaseFirestore db;
    FirebaseAuth authProfile;


    Dialog dialog;

    private ActivityClaimerListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityClaimerListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = FirebaseFirestore.getInstance();

        db.collection("Claimed Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                double y = (double) document.get("userLocationLatitude");
                                double z = (double) document.get("userLocationLongitude");

                                LatLng x = new LatLng(y, z);
                                arrayList.add(x);
                            }
                        } else {
                            Toast.makeText(ClaimerListActivity.this, "Error getting document data from firebase", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        authProfile = FirebaseAuth.getInstance();
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.navigation_donate);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), MainActivityweather.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_claim:
                        startActivity(new Intent(getApplicationContext(), FormFillUpActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_donate:
                        startActivity(new Intent(getApplicationContext(), ClaimerListActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_account:
                        if (authProfile.getCurrentUser() != null) {
                            startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                        } else {
                            startActivity(new Intent(getApplicationContext(), AccountActivity.class));

                        }
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });


        recyclerView_claimer_info = findViewById(R.id.recyclerView_claimer_list);
        recyclerView_claimer_info.setHasFixedSize(true);
        recyclerView_claimer_info.setLayoutManager(new LinearLayoutManager(this));


        claimerInfoArrayList = new ArrayList<ClaimerInfo>();
        claimerListAdapter = new ClaimerListAdapterNew(ClaimerListActivity.this, claimerInfoArrayList);

        recyclerView_claimer_info.setAdapter(claimerListAdapter);
        EventChangeListener();

    }

    private void EventChangeListener() {

        db.collection("Claimed Users").orderBy("userName", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                claimerInfoArrayList.add(dc.getDocument().toObject(ClaimerInfo.class));
                            }


                            claimerListAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        db.collection("Claimed Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                double latitude = (double) document.get("userLocationLatitude");
                                double longitude = (double) document.get("userLocationLongitude");

                                LatLng x = new LatLng(latitude, longitude);
                                mMap.addMarker(new MarkerOptions().position(x).title("Latitude: " + latitude + "Longitude: " + longitude));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(x));


                            }
                        } else {
                            Toast.makeText(ClaimerListActivity.this, "Error getting document data from firebase", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                //Call the pop-up activity
                Double latitude = marker.getPosition().latitude;
                Toast.makeText(ClaimerListActivity.this, "Location: " + marker.getTitle(), Toast.LENGTH_SHORT).show();
                dialog = new Dialog(ClaimerListActivity.this);
                dialog.setContentView(R.layout.popup);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
                userName = dialog.findViewById(R.id.Name);
                userDistrict = dialog.findViewById(R.id.District);
                userNIDNo = dialog.findViewById(R.id.NID);
                userFemaleCount = dialog.findViewById(R.id.FemaleCount);
                userBkashContactNo = dialog.findViewById(R.id.BkashContactNo);
                userSeniorCitizenCount = dialog.findViewById(R.id.SeniorCitizenCount);
                db.collection("Claimed Users")
                        .whereEqualTo("userLocationLatitude", latitude)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        userName.setText(document.get("userName").toString());
                                        userDistrict.setText("District: "+ document.get("userDistrict"));
                                        userNIDNo.setText("NID No: "+ document.get("userNIDNo"));
                                        userFemaleCount.setText("Female Members: " + document.get("userFemaleCount"));
                                        userSeniorCitizenCount.setText("Senior Citizen Members: "+ document.get("userSeniorCitizenCount"));
                                        userBkashContactNo.setText("Bkash No: " + document.get("userBkashContactNo"));
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.city_search)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem menuItem = menu.findItem(R.id.city_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                claimerListAdapter.getFilter().filter(newText);//Changed a bit
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
