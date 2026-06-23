package com.example.carsharingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carsharingapp.MainActivity;
import com.example.carsharingapp.R;
import com.example.carsharingapp.model.Vehicul;

import java.util.List;

public class VehiculAdapter extends RecyclerView.Adapter<VehiculAdapter.VehiculViewHolder> {
    private List<Vehicul> lista;
    private Context context;
    private double latRef;
    private double lngRef;

    public VehiculAdapter(List<Vehicul> lista, Context context, double latRef, double lngRef){
        this.lista = lista;
        this.context = context;
        this.latRef = latRef;
        this.lngRef = lngRef;
    }

    @NonNull
    @Override
    public VehiculViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicul, parent, false);
        return new VehiculViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehiculViewHolder holder, int position) {
        Vehicul v = lista.get(position);

        holder.tvMarcaModel.setText(v.getMarca() + " " + v.getModel());
        holder.tvDetalii.setText(
                "Locuri: " + v.getNumarLocuri() + " | " +
                        "Caroserie: " + v.getCaroserie() + " | " +
                        "Combustibil: " + v.getCombustibil() + " | " +
                        "Autonomie: " + v.getAutonomie() + " km"
        );

        float[] results = new float[1];
        Location.distanceBetween(latRef, lngRef, v.getLatitudine(), v.getLongitudine(), results);
        float distanta = results[0];

        holder.tvDistanta.setText(String.format("Distanță: %.0f m", distanta));

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("latitudine", v.getLatitudine());
            intent.putExtra("longitudine", v.getLongitudine());
            intent.putExtra("titlu", v.getMarca() + " " + v.getModel());
            intent.putExtra("detalii",
                    "Locuri: " + v.getNumarLocuri() + "\n" +
                            "Motorizare: " + (v.getMotorizare() == 0 ? "Electrică" : v.getMotorizare() + "cc") + "\n" +
                            "Caroserie: " + v.getCaroserie() + "\n" +
                            "Combustibil: " + v.getCombustibil() + "\n" +
                            "Autonomie: " + v.getAutonomie() + " km");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class VehiculViewHolder extends RecyclerView.ViewHolder {
        TextView tvMarcaModel, tvDetalii, tvDistanta;

        VehiculViewHolder(View itemView){
            super(itemView);
            tvMarcaModel = itemView.findViewById(R.id.tvMarcaModel);
            tvDetalii = itemView.findViewById(R.id.tvDetalii);
            tvDistanta = itemView.findViewById(R.id.tvDistanta); // asigură-te că există în item_vehicul.xml
        }
    }
}
