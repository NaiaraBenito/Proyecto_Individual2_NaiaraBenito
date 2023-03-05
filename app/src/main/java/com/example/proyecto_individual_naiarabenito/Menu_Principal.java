
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.proyecto_individual_naiarabenito.databinding.ActivityMenuPrincipalBinding;


/* ###################################### CLASE MENU_PRINCIPAL #####################################
    *) Descripción:
        La función de esta clase es mostrar y gestionar la vista de toda la aplicación.

    *) Tipo: Activity
*/
public class Menu_Principal extends AppCompatActivity {

// ___________________________________________ Variables ___________________________________________
    private static final int REQUEST_CALL = 1;      // ID para realizar llamadas telefónicas
    private ActivityMenuPrincipalBinding binding;   // Variable para gestionar los fragmentos del menú

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
        // Cargar las preferencias configuradas por el usuario
        cargar_configuracion();

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
}