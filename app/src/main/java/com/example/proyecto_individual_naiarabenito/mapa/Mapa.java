
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.mapa;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import com.example.proyecto_individual_naiarabenito.R;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


/* ######################################### CLASE MAPA ############################################
    *) Descripción:
        La función de esta clase es gestionar los servicios de Google Maps y geolocalización.

    *) Tipo: Activity
*/
public class Mapa extends AppCompatActivity {

// ___________________________________________ Variables ___________________________________________
    private FusedLocationProviderClient fusedLocationProviderClient; // Cliente que proporciona la localización del dispositivo
    private LocationRequest locationRequest;    // Variable para realizar la petición de localización
    private static int LOCATION_REQUEST_CODE = 10001;   // Código para obtener el permiso de ubicación
    private GoogleMap mMap; // Variable que contiene el mapa
    private SupportMapFragment mapFragment; // Variable que contiene el fragmento que contiene el mapa
    private boolean primeraVez = true;  // Variable auxiliar que indica si es la primera vez que se ejecuta el mapa

    LocationCallback locationCallback = new LocationCallback() {    // Variable que gestiona el mapa
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {

            // Comprobar si se ha obtenido la localización actual del dispositivo
            if(locationResult == null){ // Si no se tiene la localización

                // Pintar los marcadores que señalizan las tiendas físicas
                pintarMarcadores();
                return;
            }
            // Comprobar la localización del dispositivo
            for(Location location: locationResult.getLocations()){
                Log.d("MAPA", "onLocationResult: location=" + location.toString());

                // Pintar los marcadores que señalizan las tiendas físicas
                pintarMarcadores();

                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        // Comprobar que la localización obtenida del dispositivo no sea null
                        if (location != null) {
                            // Obtener el mapa
                            mMap = googleMap;

                            // Añadir un marcador que señaliza la localización actual del dispositivo
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(getResources().getString(R.string.m_PosicionActual)));

                            // Comprobar si es la primera vez que se ejecuta el mapa
                            if(primeraVez){ // Si es la primera vez --> Realizar un zoom
                                // Acercar la cámara al marcador que señaliza la ubicación actual
                                // del dispositivo
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                                // Actualizar valor de la variable auxiliar
                                primeraVez = false;
                            }
                        }
                    }
                });
            }
        }
    };

// ____________________________________________ Métodos ____________________________________________

/*  Método onCreate:
    ----------------
        *) Parámetros (Input):
                1) (Bundle) savedInstanceState: Contiene el diseño predeterminado del Activity.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta la primera vez que se crea el Activity y crea la vista del
                mapa.
*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // Crear la vista
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        // Obtener el fragmento del mapa
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Crear y configurar un cliente que se encarga de pedir la geolocalización del dispositivo
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

// _________________________________________________________________________________________________

/*  Método onStart:
    ---------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta al abrir el mapa. Se encarga de pedir los permisos necesarios
                para poder obtener la geolocalización del dispositivo.
*/
    @Override
    protected void onStart() {
        super.onStart();
        // Comprobar si se tienen los permisos necesarios
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Llamar al método que realiza las peticiones de localización
            checkSettingsAndStartLocationUpdates();
        } else{     // Si no se tienen permisos
            // Pedir los permisos
            askLocationPermission();
        }
    }

// _________________________________________________________________________________________________

/*  Método onStop:
    --------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta al cerrar el mapa. Se encarga de detener las peticiones de
                geolocalización.
*/
    @Override
    protected void onStop() {
        super.onStop();
        // Llamada al método para detener la actualización de ubicación
        stopLocationUpdates();
    }

// _________________________________________________________________________________________________

/*  Método checkSettingsAndStartLocationUpdates:
    --------------------------------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Se encarga de hacer las configuraciones necesarias para poder realizar las
                peticiones de geolocalización.
*/
    private void checkSettingsAndStartLocationUpdates(){
        // Crear una petición de ubicación
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();

        // Obtener cliente que comprueba las configuraciones necesarias
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);

        // Comprobar el estado de la configuración
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {  // Si ha sido exitosa
                // Llamada al método que actualiza la localización
                startLocationUpdates();
            }
        });
        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) { // Si ha ocurrido algún error

                // Si no se tiene la ubicación activada solo se pintan los marcadores
                pintarMarcadores();
                if(e instanceof ResolvableApiException){
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(Mapa.this,1001);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

// _________________________________________________________________________________________________

/*  Método startLocationUpdates:
    ----------------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Se encarga de realizar las peticiones de geolocalización en un bucle.
*/
    @SuppressLint("MissingPermission")
    private void startLocationUpdates(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());
    }

// _________________________________________________________________________________________________

/*  Método stopLocationUpdates:
    ---------------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Se encarga de detener las peticiones de geolocalización en un bucle.
*/
    private void stopLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

// _________________________________________________________________________________________________

/*  Método askLocationPermission:
    -----------------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método solicita los permisos necesarios para poder trabajar con la localización.
*/
    private void askLocationPermission(){
        // Comprobar si se tienen los permisos necesarios
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // Pedir esos permisos
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
        }
    }

// _________________________________________________________________________________________________

/*  Método onRequestPermissionsResult:
    ----------------------------------
        *) Parámetros (Input):
                1) (int) requestCode: Contiene el código de solicitud.
                2) (String[]) permissions: Contiene el listado de permisos solicitados.
                3) (int[]) grantResults: Contiene el listado de los resultados obtenidos en las
                   peticiones.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta tras realizar las peticiones de permisos de localización y
                obtener una respuesta.
*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Comprobar que el código de solicitud sea el de solicitar la localización
        if (requestCode == LOCATION_REQUEST_CODE) {
            // Comprobar que se han concedido los permisos
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Llamada al método que configura las peticiones
                checkSettingsAndStartLocationUpdates();
            }
        }
    }

// _________________________________________________________________________________________________

/*  Método pintarMarcadores:
    ------------------------
        *) Parámetros (Input):
                1) (int) requestCode: Contiene el código de solicitud.
                2) (String[]) permissions: Contiene el listado de permisos solicitados.
                3) (int[]) grantResults: Contiene el listado de los resultados obtenidos en las
                   peticiones.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método muestra en el mapa marcadores que indican todas los restaurantes físicos
                que tiene la empresa.
*/
    public void pintarMarcadores(){

        // Obtener el mapa del fragmento
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {  // Cuando el mapa se haya cargado
                // Obtener el mapa
                mMap = googleMap;

                // Eliminar todos los marcadores
                mMap.clear();

                // Crear los marcadores
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
            }
        });
    }
}