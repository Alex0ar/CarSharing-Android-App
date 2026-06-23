package com.example.carsharingapp;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carsharingapp.model.Cursa;
import com.example.carsharingapp.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CursaActivity extends AppCompatActivity {

    private long startTime;
    private double tarifAles;
    private Handler handler;
    private TextView tvTimp, tvPretTotal;
    private double pretTotal;
    private String emailUtilizator, vehicul;
    private long durataRezervataSecunde;
    private double tarifPeSecunda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursa);

        startTime = System.currentTimeMillis();
        tarifAles = getIntent().getDoubleExtra("tarif", 0);
        emailUtilizator = getIntent().getStringExtra("email");
        vehicul = getIntent().getStringExtra("vehicul");
        long durataRezervataMinute = getIntent().getIntExtra("durata_rezervata_min", 60);
        durataRezervataSecunde = durataRezervataMinute * 60;

        tarifPeSecunda = tarifAles / durataRezervataSecunde;

        tvTimp = findViewById(R.id.tvCronometru);
        tvPretTotal = findViewById(R.id.tvPretTotal);

        handler = new Handler();
        handler.postDelayed(updateTimer, 1000);

        findViewById(R.id.btnStopCursa).setOnClickListener(v -> incheieCursa());
    }

    private Runnable updateTimer = new Runnable() {
        public void run() {
            long elapsedSec = (System.currentTimeMillis() - startTime) / 1000;

            tvTimp.setText(elapsedSec / 60 / 24 %24 + ":" + elapsedSec / 60 % 60 + ":" + elapsedSec % 60);

            // Preț total crește cu tarif pe secundă, indiferent de depășire
            pretTotal = tarifPeSecunda * elapsedSec;

            tvPretTotal.setText(String.format("%.2f lei", pretTotal));

            handler.postDelayed(this, 1000);
        }
    };

    private void incheieCursa() {
        handler.removeCallbacks(updateTimer);
        long durata = (System.currentTimeMillis() - startTime) / 1000;

        Cursa cursa = new Cursa(emailUtilizator, vehicul, tarifAles, pretTotal, durata);

        ApiClient.getCursaApi().adaugaCursa(cursa).enqueue(new Callback<Cursa>() {
            @Override
            public void onResponse(Call<Cursa> call, Response<Cursa> response) {
                Toast.makeText(CursaActivity.this, "Cursa încheiată și salvată!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Cursa> call, Throwable t) {
                Toast.makeText(CursaActivity.this, "Eroare la salvare!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
