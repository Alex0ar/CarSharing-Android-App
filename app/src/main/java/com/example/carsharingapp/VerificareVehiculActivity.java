package com.example.carsharingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class VerificareVehiculActivity extends AppCompatActivity {

    private TextView tvTimer;
    private CountDownTimer countDownTimer;
    private static final long START_TIME_MILLIS = 4 * 60 * 1000; // 4 minute

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificare_vehicul);

        tvTimer = findViewById(R.id.tvTimer);
        Button btnIncepe = findViewById(R.id.btnIncepeCursa);

        startCountdown();

        btnIncepe.setOnClickListener(v -> {
            Intent intent = new Intent(VerificareVehiculActivity.this, CursaActivity.class);
            intent.putExtra("tarif", getIntent().getDoubleExtra("tarif", 0));
            intent.putExtra("email", getIntent().getStringExtra("email"));
            intent.putExtra("vehicul", getIntent().getStringExtra("vehicul"));
            intent.putExtra("durata_rezervata_min", getIntent().getIntExtra("durata_rezervata_min", 60));
            startActivity(intent);
            finish();
        });

    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(START_TIME_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                tvTimer.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00:00");
                Toast.makeText(VerificareVehiculActivity.this, "Verificarea s-a încheiat!", Toast.LENGTH_LONG).show();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
