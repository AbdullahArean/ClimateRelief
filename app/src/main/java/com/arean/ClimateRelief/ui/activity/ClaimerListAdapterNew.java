package com.arean.ClimateRelief.ui.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arean.ClimateRelief.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClaimerListAdapterNew extends RecyclerView.Adapter<ClaimerListAdapterNew.ClaimerListViewHolder> implements Filterable {


    Context context;
    List<ClaimerInfo>ClaimerArrayList;
    public List<ClaimerInfo> getClaimerListFilter = new ArrayList<>();

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.values = getClaimerListFilter;
                    filterResults.count = getClaimerListFilter.size();
                }
                else{
                    String searchStr = constraint.toString().toLowerCase();
                    List<ClaimerInfo> claimerListModels = new ArrayList<>();
                    for(ClaimerInfo claimerListModel:getClaimerListFilter)
                    {
                        if(claimerListModel.getUserDistrict().toLowerCase().contains(searchStr)){
                            claimerListModels.add(claimerListModel);
                        }
                    }
                    filterResults.values = claimerListModels;
                    filterResults.count = claimerListModels.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ClaimerArrayList = (List<ClaimerInfo>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

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
