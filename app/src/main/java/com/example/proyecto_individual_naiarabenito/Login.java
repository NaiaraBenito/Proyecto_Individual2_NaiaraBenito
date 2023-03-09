
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.proyecto_individual_naiarabenito.db.DBHelper;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/* ########################################## CLASE LOGIN ##########################################
    *) Descripción:
        La función de esta clase es mostrar y gestionar el proceso de logueo de los usuarios en la
        aplicación.

    *) Tipo: Activity
*/
public class Login extends AppCompatActivity {

// ___________________________________________ Variables ___________________________________________

    private EditText et_email;      // EditText que contiene el email del usuario que intenta loguearse
    private EditText et_password;   // EditText que contiene la contraseña del usuario que intenta loguearse

// ____________________________________________ Métodos ____________________________________________

/*  Método onCreate:
    ----------------
        *) Parámetros (Input):
                1) (Bundle) savedInstanceState: Contiene el diseño predeterminado del Activity.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta la primera vez que se crea el Activity y crea la vista del
                login
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Crear la vista
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Cargar las preferencias configuradas por el usuario
        cargar_configuracion();

        // Obtener los objetos de la vista editados por el usuario
        et_email = findViewById(R.id.et_emailLogin);
        et_password = findViewById(R.id.et_passwordLogin);
    }

// _________________________________________________________________________________________________

/*  Método crearCuenta:
    -------------------
        *) Parámetros (Input):
                1) (View) v: Vista asociada al Activity actual
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta cuando el usuario pulsa el texto "¡Regístrate!". Se encarga
                de redirigir la ejecución al Activity Registro.
*/
    public void crearCuenta(View v){

        // Crear el intent que redirige la ejecución al Registro
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
        finish();
    }

// _________________________________________________________________________________________________

/*  Método ingresar:
    -------------------
        *) Parámetros (Input):
            1) (View) v: Vista asociada al Activity actual
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta cuando el usuario pulsa el botón "INGRESAR".
                Valida la entrada de datos:
                    - Si los datos son válidos: Redirige la ejecución al Activity Menu_Principal.
                    - Si los datos no son válidos: Muestra un mensaje de error.
*/
    public void ingresar(View v){

        // Obtener los datos introducidos por el usuario + eliminar espacios en blanco del comienzo
        String email = et_email.getText().toString().replaceAll("^\\s*","");
        String password = et_password.getText().toString().replaceAll("^\\s*","");

        // Comprobar que los campos no se encuentren vacíos
        if(email.equals("")){               // Si el email está vacío: Escribir un mensaje de error
            String msg = getResources().getString(R.string.t_ingresaEmail);
            Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
        } else if (password.equals("")) {   // Si la contraseña está vacía: Escribir un mensaje de error
            String msg = getResources().getString(R.string.t_ingresaPassword);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } else{

            // Crear un patrón para validar el email
            Pattern pattern = Pattern.compile("([a-z\\d]+(\\.?[a-z\\d])*)+@(([a-z]+)\\.([a-z]+))+");
            Matcher mather = pattern.matcher(email);

            // Comprobar que el email sea válido
            if (mather.find()) {    // El email ingresado es válido
                // Comprobar que el usuario que intenta loguearse esté registrado en la BBDD
                DBHelper dbHelper = new DBHelper(this);
                String[] datos = dbHelper.verificarUsuarioLogin(email, password);

                if(datos != null){ // Si está registrado: Ir a Menu_Principal
                    // Crear un intent para pasar a la Actividad Menu_Principal
                    Intent intent = new Intent(this, Menu_Principal.class);

                    // Guardar los datos del usuario (para mantener la sesión)
                    intent.putExtra("nombreUsuario", datos[0]);
                    intent.putExtra("apellidoUsuario", datos[1]);
                    intent.putExtra("emailUsuario", datos[2]);

                    // Cargar el Menú Principal
                    startActivity(intent);
                    finish();
                } else{      // Si no está registrado: Imprimir mensaje de error
                    String msg = getResources().getString(R.string.t_loginIncorrecto);
                    Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
                }
            } else {        // El email ingresado es inválido: Imprimir mensaje de error
                String msg = getResources().getString(R.string.t_emailInvalido);
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            }
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
        LinearLayout l = findViewById(R.id.login);

        if(modoOscuro){     // Si el modo oscuro está activado: Pintar el fondo de gris
            l.setBackgroundColor(getResources().getColor(R.color.gris_claro));

        } else{             // Si el modo oscuro está desactivado: Pintar el fondo de blanco
            l.setBackgroundColor(getResources().getColor(R.color.white));
        }

        // Comprobar el estado de la preferencia de la orientación
        String ori = sp.getString("orientacion","false");

        switch (ori) {
            case "1":     // Si la orientación es 1: Desbloquear el giro automático de la app
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                break;
            case "2":     // Si la orientación es 2: Bloquear la orientacion vertical
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case "3":     // Si la orientación es 3: Bloquear la orientacion horizontal
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }

    //-------------------------
    public void cambiarIdiomaEsp(View v){
        // Forzar la localización de la aplicación al inglés
        Locale nuevaloc = new Locale("es");
        Locale.setDefault(nuevaloc);

        // Crear una configuración para la localización inglesa
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);
        Context context = getBaseContext().createConfigurationContext(configuration);

        // Actualizar todos los recursos de la aplicación
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

        // Regargar de nuevo la actividad:
        //   - finish() -> Finalizar la instancia en curso
        //   - startActivity(getIntent()) -> Lanzar una nueva instancia
        finish();
        startActivity(getIntent());
    }
    //-----------------------------
    public void cambiarIdiomaIng(View v){
        // Forzar la localización de la aplicación al inglés
        Locale nuevaloc = new Locale("en");
        Locale.setDefault(nuevaloc);

        // Crear una configuración para la localización inglesa
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);
        Context context = getBaseContext().createConfigurationContext(configuration);

        // Actualizar todos los recursos de la aplicación
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

        // Regargar de nuevo la actividad:
        //   - finish() -> Finalizar la instancia en curso
        //   - startActivity(getIntent()) -> Lanzar una nueva instancia
        finish();
        startActivity(getIntent());
    }
}