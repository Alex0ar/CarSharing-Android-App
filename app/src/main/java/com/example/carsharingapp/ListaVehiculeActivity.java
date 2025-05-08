package com.example.carsharingapp;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carsharingapp.adapter.VehiculAdapter;
import com.example.carsharingapp.model.Vehicul;
import com.example.carsharingapp.network.ApiClient;
import com.example.carsharingapp.network.VehiculApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaVehiculeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private VehiculAdapter adapter;
    private List<Vehicul> listaVehicule = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_vehicule);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewVehicule);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VehiculAdapter(listaVehicule);
        recyclerView.setAdapter(adapter);

        VehiculApi api = ApiClient.getClient().create(VehiculApi.class);
        api.getVehicule().enqueue(new Callback<List<Vehicul>>() {
            @Override
            public void onResponse(Call<List<Vehicul>> call, Response<List<Vehicul>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaVehicule.clear();
                    listaVehicule.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    Log.e("titlu", "afisarea tuturor vehiculelor:");
                    for(var el:listaVehicule){
                        Log.e("vehicul", el.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Vehicul>> call, Throwable t) {
                Log.e("LISTA_API", "Eroare la preluarea vehiculelor: " + t.getMessage());
            }
        });
    }
}
