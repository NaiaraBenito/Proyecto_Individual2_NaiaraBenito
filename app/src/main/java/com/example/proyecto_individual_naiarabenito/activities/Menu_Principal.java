
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.activities;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyecto_individual_naiarabenito.R;
import com.example.proyecto_individual_naiarabenito.databinding.ActivityMenuPrincipalBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;


/* ###################################### CLASE MENU_PRINCIPAL #####################################
    *) Descripción:
        La función de esta clase es mostrar y gestionar la vista de toda la aplicación.

    *) Tipo: Activity
*/
public class Menu_Principal extends AppCompatActivity {

// ___________________________________________ Variables ___________________________________________
    private static final int REQUEST_CALL = 1;      // ID para realizar llamadas telefónicas
    private ActivityMenuPrincipalBinding binding;   // Variable para gestionar los fragmentos del menú

    private String idioma;          // String que contiene el idioma actual de la aplicación

// ____________________________________________ Métodos ____________________________________________

/*  Método onCreate:
    ----------------
        *) Parámetros (Input):
                1) (Bundle) savedInstanceState: Contiene el diseño predeterminado del Activity.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta cuando el usuario se ha logueado correctamente. Gestiona los
                fragmentos del menú
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Obtener el idioma de la aplicación del Bundle (mantener idioma al girar la pantalla)
        if (savedInstanceState != null) {
            idioma = savedInstanceState.getString("idioma");
        }

        // Obtener el idioma de la aplicación del intent (mantener idioma al moverse por la aplicación)
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            idioma = (String) extras.get("idioma");
        }

        // Si en la anterior ejecución se ha guardado el idioma
        if (idioma != null){
            // Instanciar el Gestor de idiomas
            GestorIdioma gI = new GestorIdioma();

            // Asignar el idioma a la pantalla actual
            gI.cambiarIdioma(getBaseContext(), idioma);
        }

        // Cargar las preferencias configuradas por el usuario
        cargar_configuracion();

        registrarDispositivo();

        // Crear la vista
        super.onCreate(savedInstanceState);

        // Gestionar los fragmentos
        binding = ActivityMenuPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_inicio, R.id.navigation_cesta, R.id.navigation_perfil, R.id.navigation_configuracion)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_menu_principal);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


    }

// _________________________________________________________________________________________________

/*  Método llamada:
    -------------------
        *) Parámetros (Input):
            1) (View) v: Vista asociada al Activity actual
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta cuando el usuario pulsa el número de teléfono del inicio.
                Comprueba el permiso de llamada:
                    - Si tiene el permiso: Realiza la llamada al número.
                    - Si no tiene el permiso: Pide permiso para realizar llamadas.
*/
    public void llamada(View v){

        // Comprueba que la aplicación tenga permiso para realizar llamadas telefónicas
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){   // Si no tiene permiso
            // Pide permiso para poder realizar llamadas telefónicas
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
        } else{     // Si tiene permiso: Realiza la llamada al número
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:943466484")));
        }
    }

// _________________________________________________________________________________________________

/*  Método cargar_configuracion:
    ----------------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método carga las preferencias configuradas por el usuario (modo oscuro,
                orientación de la pantalla...).
*/
    private void cargar_configuracion(){

        // Obtener las preferencias configuradas por el usuario
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        // Comprobar el estado de la preferencia del modo oscuro
        boolean modoOscuro = sp.getBoolean("modo_oscuro", false);

        if(modoOscuro){     // Si el modo oscuro está activado: Pintar el fondo de gris
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.gris_claro));
        } else{             // Si el modo oscuro está desactivado: Pintar el fondo de blanco
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.white));
        }

        // Comprobar el estado de la preferencia de la orientación
        String ori = sp.getString("orientacion","false");
        switch (ori) {
            case "1":       // Si la orientación es 1: Desbloquear el giro automático de la app
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                break;
            case "2":       // Si la orientación es 2: Bloquear la orientacion vertical
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case "3":       // Si la orientación es 3: Bloquear la orientacion horizontal
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }

// _________________________________________________________________________________________________

/*  Método onSaveInstanceState:
    ------------------------
        *) Parámetros (Input):
                1) (Bundle) outState: Contiene el diseño predeterminado del Activity.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta antes de eliminar la actividad. Guarda el idioma actual en el
                Bundle, para que al refrescar la actividad se mantenga.
*/
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Guardar en el Bundle el idioma actual de la aplicación
        outState.putString("idioma",idioma);
    }

    private void registrarDispositivo(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.getException());
                    Log.w("TOKEN", "PROBLEMAS PARA OBTENER EL TOKEN");
                    return;
                }
                String email = getIntent().getExtras().getString("emailUsuario");
                String token = task.getResult();
                if (token != null && email != null){
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/nbenito012/WEB/registrar_token.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("TOKEN",response.toString());
                            //Toast.makeText(getApplicationContext(),"Se registro correctamente", Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Imprimir estado del registro
                            String msg = getResources().getString(R.string.t_errorBBDD);
                            Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
                        }
                    }
                    ){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parametros = new HashMap<String, String>();
                            parametros.put("token", token);
                            parametros.put("email", email);
                            return parametros;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                } else{
                    Log.d("TOKEN","Token o email null");
                }

            }
        });
    }
}