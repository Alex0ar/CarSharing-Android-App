package com.example.carsharingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilActivity extends AppCompatActivity {

    private TextView tvEmail, tvSold;
    private Button btnIncarca;

    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_SOLD = "sold";

    private float sold = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("info", "activitatea de profil deschisa");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);


        tvEmail = findViewById(R.id.tvEmail);
        tvSold = findViewById(R.id.tvSold);
        btnIncarca = findViewById(R.id.btnIncarca);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tvEmail.setText("Email: " + user.getEmail());
        }

        sold = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getFloat(KEY_SOLD, 0.0f);
        actualizeazaAfisareSold();

        btnIncarca.setOnClickListener(v -> arataDialogIncarcare());
    }

    private void arataDialogIncarcare() {
        EditText input = new EditText(this);
        input.setHint("Suma (lei)");

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Încarcă soldul");
        dialog.setMessage("Introduceți suma în lei:");
        dialog.setView(input);

        dialog.setPositiveButton("Confirmă", (d, which) -> {
            try {
                float suma = Float.parseFloat(input.getText().toString());
                if (suma > 0) {
                    sold += suma;
                    salvareSold();
                    actualizeazaAfisareSold();
                    Toast.makeText(this, "Sold actualizat cu " + suma + " lei", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Suma trebuie să fie pozitivă", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Valoare invalidă", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setNegativeButton("Anulează", null);
        dialog.show();
    }

    private void salvareSold() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putFloat(KEY_SOLD, sold);
        editor.apply();
    }

    private void actualizeazaAfisareSold() {
        tvSold.setText(String.format("Sold curent: %.2f lei", sold));
    }
}
