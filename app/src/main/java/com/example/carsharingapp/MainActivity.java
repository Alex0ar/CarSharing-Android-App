package com.example.carsharingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.carsharingapp.model.Vehicul;
import com.example.carsharingapp.network.ApiClient;
import com.example.carsharingapp.network.VehiculApi;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap gMap;
    private List<Vehicul> listaVehicule = new ArrayList<>();

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("intrare in onCreate");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if(mapFragment != null){
            mapFragment.getMapAsync((OnMapReadyCallback) this);
        }

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle.setDrawerIndicatorEnabled(true);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_btn_lista_masini) {
                Intent intent = new Intent(this, ListaVehiculeActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawers(); // închide meniul după click
                return true;
            }
            return false;
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("DEBUG", "intrare in onMapReady");
        gMap = googleMap;
        BitmapDescriptor iconMasina = BitmapDescriptorFactory.fromResource(R.drawable.sedan);
        gMap.getUiSettings().setZoomGesturesEnabled(true);

        // Setare locație inițială – București
        LatLng bucuresti = new LatLng(44.4268, 26.1025);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bucuresti, 14));

        findViewById(R.id.btnZoomIn).setOnClickListener(v -> {
            gMap.animateCamera(CameraUpdateFactory.zoomIn());
        });
        findViewById(R.id.btnZoomOut).setOnClickListener(v -> {
            gMap.animateCamera(CameraUpdateFactory.zoomOut());
        });

        // Marker de test
        gMap.addMarker(new MarkerOptions().position(bucuresti).title("București"));
        Log.d("DEBUG", "setare marker de test");

        VehiculApi api = ApiClient.getClient().create(VehiculApi.class);
        Log.d("DEBUG", "dupa VehiculApi api = ApiClient.getClient().create(VehiculApi.class);");

        api.getVehicule().enqueue(new Callback<List<Vehicul>>() {
            @Override
            public void onResponse(Call<List<Vehicul>> call, Response<List<Vehicul>> response) {
                Log.d("DEBUG", "intrare in onResponse din onMapReady");

                if (response.isSuccessful() && response.body() != null) {
                    for (Vehicul vehicul : response.body()) {
                        LatLng locatie = new LatLng(vehicul.getLatitudine(), vehicul.getLongitudine());
                        String titlu = vehicul.getMarca() + " " + vehicul.getModel();
                        String detalii = "Locuri: " + vehicul.getNumarLocuri() + "\n" +
                                "Motorizare: " + (vehicul.getMotorizare() == 0 ? "Electrică" : vehicul.getMotorizare() + "cc") + "\n" +
                                "Caroserie: " + vehicul.getTipCaroserie() + "\n" +
                                "Combustibil: " + vehicul.getTipCombustibil();
                        Log.d("DEBUG", "Detalii vehicul: " + detalii);

                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(locatie)
                                .title(titlu)
                                .snippet(detalii)
                                .icon(iconMasina);

                        gMap.addMarker(markerOptions);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Vehicul>> call, Throwable t) {
                Log.e("MAP_API", "Eroare: " + t.getMessage());
            }
        });


        gMap.setOnMarkerClickListener(marker -> {
            showVehicleBottonSheet(marker.getTitle(), marker.getSnippet());
            return true;
        });
    }

    private void showVehicleBottonSheet(String titlu, String detalii){
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_vehicle, null);

        TextView textTitlu = view.findViewById(R.id.tvTitluVehicul);
        TextView textDetalii = view.findViewById(R.id.tvDetaliiVehicul);

        textTitlu.setText(titlu);
        textDetalii.setText(detalii);

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(toggle != null && toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}