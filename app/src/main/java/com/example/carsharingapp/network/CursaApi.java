package com.example.carsharingapp.network;

import com.example.carsharingapp.model.Cursa;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CursaApi {
    @POST("/curse")
    Call<Cursa> adaugaCursa(@Body Cursa cursa);
}
