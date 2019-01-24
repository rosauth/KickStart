package com.example.rosa.kickstart.adapter;

import android.hardware.Sensor;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rosa.kickstart.R;

import java.util.List;

import static com.example.rosa.kickstart.activity.ListSensorActivity.TAG;

public class RVSensorAdapter extends RecyclerView.Adapter<RVSensorAdapter.ViewHolder> {
    private List<Sensor> sensors;

    public RVSensorAdapter(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView number, name, vendor, maxRange;

        ViewHolder(View itemView){
            super(itemView);

            cardView = itemView.findViewById(R.id.sensorCV);
            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.name);
            vendor = itemView.findViewById(R.id.vendor);
            maxRange = itemView.findViewById(R.id.maxRange);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sensor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.name.setText(sensors.get(position).getName());
        viewHolder.vendor.setText("Vendor: " + sensors.get(position).getVendor() + ", ");
        viewHolder.maxRange.setText("Maximum Range: "+sensors.get(position).getMaximumRange());

        Log.i(TAG, "onBindViewHolder: " + sensors);
    }

    @Override
    public int getItemCount() {
        return sensors.size();
    }
}
