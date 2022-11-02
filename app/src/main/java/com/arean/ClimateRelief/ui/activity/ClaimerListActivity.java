package com.arean.ClimateRelief.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.arean.ClimateRelief.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claimer_list);



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