package com.icoding.warranty;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.icoding.warranty.data.WarrantyData;

import java.util.List;

public class WarrantyAdapter extends RecyclerView.Adapter<WarrantyAdapter.WarrantyViewHolder> {

    List<WarrantyData> mWarrantyData;

    public WarrantyAdapter(List<WarrantyData> warrantyData) {
        mWarrantyData = warrantyData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WarrantyAdapter.WarrantyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.warranty_item, parent, false);
        WarrantyViewHolder wvh = new WarrantyViewHolder(v);
        return wvh;
    }

    @Override
    public void onBindViewHolder(@NonNull WarrantyAdapter.WarrantyViewHolder holder, int position) {

        WarrantyData currentWarranty = mWarrantyData.get(position);
        holder.nameView.setText(currentWarranty.getName());
        holder.durationView.setText(String.valueOf(currentWarranty.getDuration()));
        Log.e("duration response", String.valueOf(currentWarranty.getDuration()));
    }

    @Override
    public int getItemCount() {
        return mWarrantyData.size();
    }



    public static class WarrantyViewHolder extends RecyclerView.ViewHolder{
        TextView nameView;
        TextView durationView;

        public WarrantyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.text_name);
            durationView = itemView.findViewById(R.id.text_duration);
        }
    }
}
