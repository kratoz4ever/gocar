package com.example.gocar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

public class VehiclesAdapter extends RecyclerView.Adapter<VehiclesAdapter.ProductViewHolder>  {
    private Context mCtx;
    private ArrayList<Vehicles> VehiclesList;
    private OnVehicleListener MonVehicleListener;

    public VehiclesAdapter(Context mCtx, ArrayList<Vehicles> VihiclestList ,OnVehicleListener monVehicleListener) {
        this.mCtx = mCtx;
        this.VehiclesList = VihiclestList;
        this.MonVehicleListener=monVehicleListener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout, null);
        return new ProductViewHolder( view , MonVehicleListener);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        Vehicles vehicles = VehiclesList.get(position);
        String path = "http://192.168.1.6/" + vehicles.getImage_path();
        Picasso.get().load(path).into(holder.Car_image);
        holder.Car_Name.setText("Vehicle Name : " + vehicles.getName());
        holder.Car_Model.setText("Vehicle Model : " + vehicles.getYear());
        holder.Car_Fuel.setText("Fuel Capacity : " + String.valueOf(vehicles.getFuel_level()));
    }

    @Override
    public int getItemCount() {
        return VehiclesList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView Car_Name, Car_Model, Car_Fuel;
        ImageView Car_image;
        OnVehicleListener onVehicleListener;
        public ProductViewHolder(View itemView ,OnVehicleListener listener) {
            super(itemView);
            this.onVehicleListener = listener;
            Car_Name = itemView.findViewById(R.id.txtcarname);
            Car_Model = itemView.findViewById(R.id.txtmodel);
            Car_Fuel = itemView.findViewById(R.id.txtfuel);
            Car_image = itemView.findViewById(R.id.imagecar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onVehicleListener.OnVehicleClick(getAdapterPosition());

        }
    }
    public interface OnVehicleListener {
        void OnVehicleClick(int position );

    }
}

