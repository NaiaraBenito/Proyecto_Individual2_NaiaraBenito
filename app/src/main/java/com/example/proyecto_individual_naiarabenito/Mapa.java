package com.example.proyecto_individual_naiarabenito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapa extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        LatLng barakaldo = new LatLng(43.297685425160246, -2.9878466053022246);
        mMap.addMarker(new MarkerOptions()
                .position(barakaldo)
                .title("Zaballa Kalea, 48901 Barakaldo, Bizkaia"));

        LatLng bilbao = new LatLng(43.26231215663768, -2.927504349778167);
        mMap.addMarker(new MarkerOptions()
                .position(bilbao)
                .title("Colón de Larreátegui K., 9, 48001 Bilbo, Bizkaia"));

        LatLng sestao = new LatLng(43.30673726943006, -3.008378845997969);
        mMap.addMarker(new MarkerOptions()
                .position(sestao)
                .title("Vía Galindo Kalea, 48910 Sestao, Bizkaia"));

        LatLng kabiezes = new LatLng(43.322818297986224, -3.039722329610171);
        mMap.addMarker(new MarkerOptions()
                .position(kabiezes)
                .title("Doctor Ferran Kalea, 10, 48980 Santurtzi, Bizkaia"));

        LatLng basauri = new LatLng(43.23570983534051, -2.8891110594587763);
        mMap.addMarker(new MarkerOptions()
                .position(basauri)
                .title("Kale Nagusia, 1, 48970 Basauri, Bizkaia"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(barakaldo));
    }
}