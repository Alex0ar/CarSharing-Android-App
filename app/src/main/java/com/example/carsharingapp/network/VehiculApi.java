package com.example.carsharingapp.network;

import com.example.carsharingapp.model.Vehicul;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface VehiculApi {
    @GET("/api/vehicule")
    Call<List<Vehicul>> getVehicule();
}
