package com.example.carsharingapp;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


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
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        //System.out.println("intrare in onCreate");
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
            else if (id == R.id.nav_btn_logout) {
                FirebaseAuth.getInstance().signOut(); // Delogare Firebase
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Curăță back stack
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_btn_profil) {
                Intent intent = new Intent(this, ProfilActivity.class);
                Log.e("info", "urmeaza sa intre in activitatea de profil");
                startActivity(intent);
                drawerLayout.closeDrawers();
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
                        String detalii =
                                "Caroserie: " + vehicul.getCaroserie() + "\n" +
                                "Locuri: " + vehicul.getNumarLocuri() + "\n" +
                                "Motorizare: " + (vehicul.getMotorizare() == 0 ? "Electrică" : vehicul.getMotorizare() + "cc") + "\n" +
                                "Combustibil: " + vehicul.getCombustibil() + "\n" + "Autonomie: " + vehicul.getAutonomie() + " km";

                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(locatie)
                                .title(titlu)
                                .snippet(detalii)
                                .icon(iconMasina);

                        Marker marker = gMap.addMarker(markerOptions);
                        if (marker != null) {
                            marker.setTag(vehicul);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Vehicul>> call, Throwable t) {
                Log.e("MAP_API", "Eroare: " + t.getMessage());
            }
        });


        gMap.setOnMarkerClickListener(marker -> {
            Vehicul vehicul = (Vehicul) marker.getTag();
            if (vehicul != null) {
                String titlu = vehicul.getMarca() + " " + vehicul.getModel();
                String detalii = "Caroserie: " + vehicul.getCaroserie() + "\n" +
                        "Locuri: " + vehicul.getNumarLocuri() + "\n" +
                        "Motorizare: " + (vehicul.getMotorizare() == 0 ? "Electrică" : vehicul.getMotorizare() + "cc") + "\n" +
                        "Combustibil: " + vehicul.getCombustibil() + "\n" +
                        "Autonomie: " + vehicul.getAutonomie() + " km";

                showVehicleBottonSheet(titlu, detalii, vehicul.getLatitudine(), vehicul.getLongitudine());
            }
            return true;
        });


        Intent intent = getIntent();
        if (intent.hasExtra("latitudine") && intent.hasExtra("longitudine")) {
            double lat = intent.getDoubleExtra("latitudine", 0);
            double lng = intent.getDoubleExtra("longitudine", 0);
            String titlu = intent.getStringExtra("titlu");
            String detalii = intent.getStringExtra("detalii");

            LatLng locatieVehicul = new LatLng(lat, lng);
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locatieVehicul, 15));
            gMap.addMarker(new MarkerOptions().position(locatieVehicul).title(titlu).snippet(detalii));

            gMap.setOnMapLoadedCallback(() -> showVehicleBottonSheet(titlu, detalii, lat, lng));
        }
    }

    private void showVehicleBottonSheet(String titlu, String detalii, double masinaLat, double masinaLng){
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_vehicle, null);

        TextView textTitlu = view.findViewById(R.id.tvTitluVehicul);
        TextView textDetalii = view.findViewById(R.id.tvDetaliiVehicul);
        TextView textTarife = view.findViewById(R.id.tvTarife);
        Button btnRezerva = view.findViewById(R.id.btnRezerva);

        textTitlu.setText(titlu);
        textDetalii.setText(detalii);

        String tarife = "Tarife:\n• 1 minut: 0.5lei\n• 1 oră: 10lei\n• 12 ore: 49lei\n• 24 ore: 89lei";
        textTarife.setText(tarife);

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);

        View bottomSheet = dialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        dialog.show();

        btnRezerva.setOnClickListener(v -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String email = "";
            if(currentUser != null){
                email = currentUser.getEmail();
            }
            double userLat = 44.4268;
            double userLng = 26.1025;

            float[] rezultat = new float[1];
            Location.distanceBetween(userLat, userLng, masinaLat, masinaLng, rezultat);

            if (rezultat[0] > 2.0f) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Apropiați-vă de vehicul")
                        .setMessage("Vă aflați la o distanță de " + (int)rezultat[0] + " metri de vehicul.\nPentru a-l debloca, distanța maximă este de 2 metri.")
                        .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                        .setCancelable(false)
                        .show();
            } else {
                Intent intent = new Intent(MainActivity.this, RezervareActivity.class);
                intent.putExtra("titlu", titlu);
                intent.putExtra("email", email);
                intent.putExtra("detalii", detalii);
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(toggle != null && toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

//de adaugat functionalitatea
//
// -> sa adaug functionalitatea ca soferul sa poata introduce in aplicatie CI si permisul de conducere (sa faca poze)
//      -> daca reusesc sa adaug verificarea actelor, adica verifica daca CI chiar arata ca un CI si la fel cu permisul de conducere
//
//
// -> rezervarea unei masini
//      -> dupa ce aleg tot se deschide camera si imi cere minim 4 poze din toate 4 parti (voi face asta cand voi testa pe telefon
//      -> dupa poze cursa sta pe pauza pentru maxim 2 minute pentru ca soferul sa poata verifica toate functionalitatile vehiculului (faruri, oglinzi etc) (acesta va fi un bottomSheet care va aparea deja cand se va deschide harta dupa ce apesi ok dupa ce s-a ales tariful final, va aparea un temporizator de 2 min si vor fi scrise info ce trebuie verificat inainte de pornire)
//
//
// -> sa adaug locatia live si sa fac asa ca atunci cand se incheie cursa sa se actualizeze locatia masinii cu care m-am deplasat
//
// -> de resetat timerul daca cursa trece de 24 ore si de scris asa: 1 zi 00:00:00
//
// -> sa adaug locatie live
// -> sa simulez cat mai aproape de realitate incarcarea contului
//
// -> de ce info despre cursele finalizate nu ajung in BD
//
//
//
//