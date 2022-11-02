package com.arean.ClimateRelief.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.arean.ClimateRelief.R;
import com.arean.ClimateRelief.ui.activity.ClaimerInfo;
import com.arean.ClimateRelief.ui.activity.ClaimerListAdapterNew;
import com.arean.ClimateRelief.ui.activity.UserProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ClaimerListActivity extends AppCompatActivity {

    RecyclerView recyclerView_claimer_info;
    ArrayList<ClaimerInfo> claimerInfoArrayList;
    ClaimerListAdapterNew claimerListAdapter;
    FirebaseFirestore db;
    FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claimer_list);
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.navigation_donate);

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
                        startActivity(new Intent(getApplicationContext(), FormFillUpActivity.class));
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



        recyclerView_claimer_info = findViewById(R.id.recyclerView_claimer_list);
        recyclerView_claimer_info.setHasFixedSize(true);
        recyclerView_claimer_info.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
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

                        if(error != null)
                        {
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for(DocumentChange dc: value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED)
                            {
                                claimerInfoArrayList.add(dc.getDocument().toObject(ClaimerInfo.class));
                            }


                            claimerListAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}