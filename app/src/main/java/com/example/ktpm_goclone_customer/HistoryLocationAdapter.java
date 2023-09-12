package com.example.ktpm_goclone_customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ktpm_goclone_customer.listeners.ItemListener;

import java.util.ArrayList;
import java.util.Objects;

public class HistoryLocationAdapter extends RecyclerView.Adapter<HistoryLocationAdapter.ViewHolder>{
    private final Context context;
    ArrayList<ListHistoryLocation> listLocations;
    private final ItemListener itemListener;

    public HistoryLocationAdapter(Context context, ArrayList<ListHistoryLocation> listLocations, ItemListener itemListener) {
        this.context = context;
        this.listLocations = listLocations;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_history_location, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.locationTitle.setText(String.valueOf(listLocations.get(position).getTitle()));
        holder.locationDescript.setText(String.valueOf(listLocations.get(position).getDescript()));
        holder.dateTime.setText(String.valueOf(listLocations.get(position).getDateTime()));
        if (Objects.equals(listLocations.get(position).getVehicleType(), "4")) {
            holder.vehicleType.setImageResource(R.drawable.ic_4_seated_car_logo);
        } else {
            holder.vehicleType.setImageResource(R.drawable.ic_7_seated_car_logo);
        }

    }


    @Override
    public int getItemCount() {
        return listLocations.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView locationTitle, locationDescript, dateTime;
        ImageView vehicleType;

        public ViewHolder(View itemView) {
            super(itemView);
            locationTitle = itemView.findViewById(R.id.locationTitle);
            locationDescript = itemView.findViewById(R.id.locationDescript);
            dateTime = itemView.findViewById(R.id.dateTime);
            vehicleType = itemView.findViewById(R.id.vehicleType);

            itemView.setOnClickListener(view -> itemListener.OnItemPosition(getAdapterPosition()));
        }
    }
}
