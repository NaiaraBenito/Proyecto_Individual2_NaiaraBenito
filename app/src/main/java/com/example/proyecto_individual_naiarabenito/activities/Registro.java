
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.activities;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import androidx.annotation.NonNull;
import  androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyecto_individual_naiarabenito.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/* ######################################### CLASE REGISTRO ########################################
    *) Descripción:
        La función de esta clase es mostrar y gestionar el proceso de registro de los usuarios en la
        aplicación.

    *) Tipo: Activity
*/
public class Registro extends AppCompatActivity {

// ___________________________________________ Variables ___________________________________________
    private EditText et_nombre;     // EditText que contiene el nombre del usuario que intenta registrarse
    private EditText et_apellido;   // EditText que contiene el apellido del usuario que intenta registrarse
    private EditText et_email;      // EditText que contiene el email del usuario que intenta registrarse
    private EditText et_password1;  // EditText que contiene la contraseña del usuario que intenta registrarse
    private EditText et_password2;  // EditText que contiene la contraseña del usuario que intenta registrarse

    private String idioma;          // String que contiene el idioma actual de la aplicación

    private RequestQueue requestQueue;
    private View v;

// ____________________________________________ Métodos ____________________________________________

/*  Método onCreate:
    ----------------
        *) Parámetros (Input):
                1) (Bundle) savedInstanceState: Contiene el diseño predeterminado del Activity.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta la primera vez que se crea el Activity y crea la vista del
                registro
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

        // Crear la vista
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Cargar las preferencias configuradas por el usuario
        cargar_configuracion();

        // Obtener los objetos de la vista editados por el usuario
        et_nombre = findViewById(R.id.et_nombreRegistro);
        et_apellido = findViewById(R.id.et_apellidoRegistro);
        et_email = findViewById(R.id.et_emailRegistro);
        et_password1 = findViewById(R.id.et_passwordRegistro1);
        et_password2 = findViewById(R.id.et_passwordRegistro2);

        requestQueue = Volley.newRequestQueue(this);
    }

// _________________________________________________________________________________________________

/*  Método volverLogin:
    -------------------
        *) Parámetros (Input):
                1) (View) v: Vista asociada al Activity actual
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta al pulsar el texto "Ya tengo cuenta". Redirige la ejecución
                del programa al Activity Login sin registrar al usuario.
*/
    public void volverLogin(View v){

        // Crear el intent que redirige la ejecución al Login
        Intent intent = new Intent(this, Login.class);

        // Guardar idioma actual de la aplicación
        intent.putExtra("idioma",idioma);
        startActivity(intent);
        finish();
    }

// _________________________________________________________________________________________________

/*  Método registrarUsuario:
    ------------------------
        *) Parámetros (Input):
                1) (View) v: Vista asociada al Activity actual
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta cuando el usuario pulsa el botón "REGISTRAR".
                Valida la entrada de datos:
                    - Si los datos son válidos: Registra al usuario y redirige la ejecución al Login.
                    - Si los datos no son válidos: Muestra un mensaje de error.
*/
    public void registrarUsuario(View v){

        // Obtener los datos introducidos por el usuario + eliminar espacios en blanco del comienzo
        String nombre = et_nombre.getText().toString().replaceAll("^\\s*","");
        String apellido = et_apellido.getText().toString().replaceAll("^\\s*","");
        String email = et_email.getText().toString().replaceAll("^\\s*","");
        String password1 = et_password1.getText().toString().replaceAll("^\\s*","");
        String password2 = et_password2.getText().toString().replaceAll("^\\s*","");

        // Comprobar que los campos no se encuentren vacíos
        if(nombre.equals("")){
            String msg = getResources().getString(R.string.t_ingresaNombre);
            Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
        } else if (apellido.equals("")) {
            String msg = getResources().getString(R.string.t_ingresaApellido);
            Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
        } else if (email.equals("")) {
            String msg = getResources().getString(R.string.t_ingresaEmail);
            Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
        } else if (password1.equals("") || password2.equals("")) {
            String msg = getResources().getString(R.string.t_ingresaPassword);
            Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
        } else if (!password1.equals(password2)) {    // Comprobar que las contraseñas coincidan
            String msg = getResources().getString(R.string.t_coincidePassword);
            Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
        } else{     // Si se han completado todos los campos: Comprobar email + Registrar

            // Crear patrón para validar el email
            Pattern pattern = Pattern.compile("([a-z\\d]+(\\.?[a-z\\d])*)+@(([a-z]+)\\.([a-z]+))+");
            Matcher mather = pattern.matcher(email);

            // Comprobar que el email sea válido
            if (mather.find()) {    // El email ingresado es válido
                this.v = v;
                // Registrar el usuario en la BBDD
                validarUsuario("http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/nbenito012/WEB/validar_usuario.php");

            } else {     // El email ingresado es inválido: Imprimir mensaje de error
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
        LinearLayout l = findViewById(R.id.registro);

        if(modoOscuro){        // Si el modo oscuro está activado: Pintar el fondo de gris
            l.setBackgroundColor(getResources().getColor(R.color.gris_claro));
        } else{                // Si el modo oscuro está desactivado: Pintar el fondo de blanco
            l.setBackgroundColor(getResources().getColor(R.color.white));
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

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void añadirUsuario(String pUrl){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, pUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String msg = getResources().getString(R.string.t_registroCompletado);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                // Volver al Login
                volverLogin(v);
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
                parametros.put("nombre", et_nombre.getText().toString());
                parametros.put("apellido", et_apellido.getText().toString());
                parametros.put("email", et_email.getText().toString());
                parametros.put("password", et_password1.getText().toString());
                parametros.put("foto", "https://cdn-icons-png.flaticon.com/512/5087/5087579.png");
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void validarUsuario(String pUrl){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, pUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;
                try {
                    json = new JSONObject(response);

                    if(json.get("exist").toString().equals("false")){
                        añadirUsuario("http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/nbenito012/WEB/registrar_usuario.php");
                    } else{
                        String msg = getResources().getString(R.string.t_errorRegistro);
                        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
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
                parametros.put("id", "registro");
                parametros.put("email", et_email.getText().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

}