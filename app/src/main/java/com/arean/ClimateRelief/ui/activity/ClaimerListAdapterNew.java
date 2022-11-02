package com.arean.ClimateRelief.ui.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arean.ClimateRelief.R;

import java.util.ArrayList;

public class ClaimerListAdapterNew extends RecyclerView.Adapter<ClaimerListAdapterNew.ClaimerListViewHolder> {


    Context context;
    ArrayList<ClaimerInfo>ClaimerArrayList;

    public ClaimerListAdapterNew(Context context, ArrayList<ClaimerInfo> claimerArrayList) {
        this.context = context;
        ClaimerArrayList = claimerArrayList;
    }

    @NonNull
    @Override
    public ClaimerListAdapterNew.ClaimerListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.claimer_info, parent, false);

        return new ClaimerListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimerListAdapterNew.ClaimerListViewHolder holder, int position) {
         ClaimerInfo claimer = ClaimerArrayList.get(position);
         holder.userName.setText(claimer.userName);
         holder.userDistrict.setText(claimer.userDistrict);
         holder.userNIDNo.setText(claimer.userNIDNo);
         holder.userFemaleCount.setText(claimer.userFemaleCount);
         holder.userSeniorCitizenCount.setText(claimer.userSeniorCitizenCount);
         holder.userBkashContactNo.setText(claimer.userBkashContactNo);
    }

    @Override
    public int getItemCount() {
        return ClaimerArrayList.size();
    }

    public static class ClaimerListViewHolder extends RecyclerView.ViewHolder{

        TextView userName, userDistrict, userFemaleCount, userSeniorCitizenCount, userNIDNo, userBkashContactNo;

        public ClaimerListViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.textView_claimerName);
            userDistrict = itemView.findViewById(R.id.textView_claimerDistrict);
            userBkashContactNo = itemView.findViewById(R.id.textView_claimerBkashNo);
            userFemaleCount = itemView.findViewById(R.id.textView_claimerFemaleCount);
            userNIDNo = itemView.findViewById(R.id.textView_claimerNIDNo);
            userSeniorCitizenCount = itemView.findViewById(R.id.textView_claimerSeniorCitizenCount);
        }
    }
}
