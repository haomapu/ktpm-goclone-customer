package com.example.ktpm_goclone_customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MarkAdapter  extends RecyclerView.Adapter<MarkAdapter.ViewHolder>{
    ArrayList<ListLocation> listLocations;

    public MarkAdapter(ArrayList<ListLocation> listLocations) {
        this.listLocations = listLocations;
    }
    @NonNull
    @Override
    public MarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_mark, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkAdapter.ViewHolder holder, int position) {
        holder.locationTitle.setText(String.valueOf(listLocations.get(position).getTitle()));
    }

    @Override
    public int getItemCount() {
        return listLocations.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView locationTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            locationTitle = itemView.findViewById(R.id.locationTitle);
        }
    }

}
