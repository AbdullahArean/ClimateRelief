package com.arean.ClimateRelief;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FormFillUpActivity extends AppCompatActivity {


    private Spinner spinnerDivision, spinnerDistrict, spinnerUpazilla, spinnerUnion;
    DatabaseReference databaseReference;
    List<String> divisionNames=new ArrayList<>();
    List<String> spinnerDivisionArrayList, spinnerDistrictArrayList, spinnerUpazillaArrayList, spinnerUnionArrayList;
    ArrayAdapter<String>arrayAdapter;
    ArrayAdapter<String> spinnerDivisionArrayAdapter,spinnerDistrictArrayAdapter,spinnerUpazillaArrayAdapter, spinnerUnionArrayAdapter;
    String divisionID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_fill_up);

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




}