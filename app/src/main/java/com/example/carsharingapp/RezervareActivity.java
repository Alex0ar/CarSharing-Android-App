package com.example.carsharingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RezervareActivity extends AppCompatActivity {

    private TextView tvDetaliiRezervare;
    private RadioGroup rgTarife;
    private Button btnConfirma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezervare);

        tvDetaliiRezervare = findViewById(R.id.tvDetaliiRezervare);
        rgTarife = findViewById(R.id.rgTarife);
        btnConfirma = findViewById(R.id.btnConfirmaRezervare);

        String titlu = getIntent().getStringExtra("titlu");
        String detalii = getIntent().getStringExtra("detalii");

        tvDetaliiRezervare.setText(titlu + "\n" + detalii);

        btnConfirma.setOnClickListener(v -> {
            int selectedId = rgTarife.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Selectează un tarif!", Toast.LENGTH_SHORT).show();
                return;
            }

            String tarif = "";
            double tarifAles = 0;
            int durataMinute = 0;
            if (selectedId == R.id.rb1min) {
                tarif = "0.50 lei / min";
                tarifAles = 0.5;
                durataMinute = 1;
            } else if (selectedId == R.id.rb1h) {
                tarif = "10.00 lei / oră";
                tarifAles = 10;
                durataMinute = 60;
            } else if (selectedId == R.id.rb12h) {
                tarif = "49.00 lei / 12 ore";
                tarifAles = 49;
                durataMinute = 720;
            } else if (selectedId == R.id.rb24h) {
                tarif = "89.00 lei / 24 ore";
                tarifAles = 89;
                durataMinute = 1440;
            }

            Intent intent = new Intent(RezervareActivity.this, VerificareVehiculActivity.class);
            intent.putExtra("tarif", tarifAles);
            intent.putExtra("email", getIntent().getStringExtra("email"));
            intent.putExtra("vehicul", getIntent().getStringExtra("titlu"));
            intent.putExtra("durata_rezervata_min", durataMinute);
            startActivity(intent);

            Toast.makeText(this, "Rezervare confirmată.\nTarif: " + tarif, Toast.LENGTH_LONG).show();

            finish();
        });
    }
}
