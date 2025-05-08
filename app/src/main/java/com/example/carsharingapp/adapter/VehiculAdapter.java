package com.example.carsharingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carsharingapp.R;
import com.example.carsharingapp.model.Vehicul;

import java.util.List;

public class VehiculAdapter extends RecyclerView.Adapter<VehiculAdapter.VehiculViewHolder> {
    private List<Vehicul> lista;
    public VehiculAdapter(List<Vehicul> lista){
        this.lista = lista;
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
                        "Caroserie: " + v.getTipCaroserie() + " | " +
                        "Combustibil: " + v.getTipCombustibil()
        );
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class VehiculViewHolder extends RecyclerView.ViewHolder{
        TextView tvMarcaModel, tvDetalii;
        VehiculViewHolder(View itemView){
                super(itemView);
                tvMarcaModel = itemView.findViewById(R.id.tvMarcaModel);
                tvDetalii = itemView.findViewById(R.id.tvDetalii);
        }


    }
}
