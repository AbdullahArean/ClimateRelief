package com.arean.ClimateRelief.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arean.ClimateRelief.R;

import java.util.ArrayList;

public class recyclerViewAdapter extends RecyclerView.Adapter<recyclerViewAdapter.ViewHolder> {

    final ArrayList<recyclerViewModel> recyclerViewModels;
    final Context context;
    public recyclerViewAdapter(Context context, ArrayList<recyclerViewModel> recyclerViewModels)
    {
        this.context = context;
        this.recyclerViewModels = recyclerViewModels;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_rowitem, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.recycler_view_imageView.setImageResource(recyclerViewModels.get(position).getStepImages());
        holder.recycler_view_image_textView.setText(recyclerViewModels.get(position).getStepNames());

    }

    @Override
    public int getItemCount() {
        return recyclerViewModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView recycler_view_imageView;
        final TextView recycler_view_image_textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recycler_view_imageView = itemView.findViewById(R.id.recycler_view_imageView);
            recycler_view_image_textView = itemView.findViewById(R.id.recycler_view_image_textView);



        }
    }
}
