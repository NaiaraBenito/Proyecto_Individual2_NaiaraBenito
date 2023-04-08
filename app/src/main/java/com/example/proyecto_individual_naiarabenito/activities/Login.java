
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.activities;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
    private String idioma;          // String que contiene el idioma actual de la aplicación
    private RequestQueue requestQueue;  // Variable que gestiona el envío de peticiones a la BBDD remota
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
        setContentView(R.layout.activity_login);

        // Cargar las preferencias configuradas por el usuario
        cargar_configuracion();

        // Obtener los objetos de la vista editados por el usuario
        et_email = findViewById(R.id.et_emailLogin);
        et_password = findViewById(R.id.et_passwordLogin);

        // Inicializar la variable que realiza las peticiones a la BBDD remota
        requestQueue = Volley.newRequestQueue(this);
    }

// _________________________________________________________________________________________________

/*  Método crearCuenta:
    -------------------
        *) Parámetros (Input):
                1) (View) v: Vista asociada al Activity actual.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta cuando el usuario pulsa el texto "¡Regístrate!". Se encarga
                de redirigir la ejecución al Activity Registro.
*/
    public void crearCuenta(View v){

        // Crear el intent que redirige la ejecución al Registro
        Intent intent = new Intent(this, Registro.class);

        // Guardar el idioma actual de la aplicación
        intent.putExtra("idioma",idioma);
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
                    - Si los datos son válidos: Llama a la función que comprueba si el usuario está
                      registrado en la BBDD.
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
                // Validar registro del usuario
                validarUsuario("http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/nbenito012/WEB/validar_usuario.php");
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

// _________________________________________________________________________________________________

/*  Método cambiarIdiomaEsp:
    ------------------------
        *) Parámetros (Input):
                1) (View) view: Vista asociada al Activity actual.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se utiliza cuando el usuario pulsa la etiqueta "Español" del Login.
                Traduce la aplicación completa al idioma español.
*/
    public void cambiarIdiomaEsp(View v){

        // Cambiar variable auxiliar que indica el idioma de la aplicación
        idioma = "es";

        // Instanciar el Gestor de idiomas
        GestorIdioma gI = new GestorIdioma();

        // Asignar el idioma a la pantalla actual
        gI.cambiarIdioma(getBaseContext(), idioma);

        // Recargar de nuevo la actividad
        Intent intent = new Intent(this, Login.class);

        // Enviar el idioma actual
        intent.putExtra("idioma",idioma);
        startActivity(intent);
        finish();
    }

// _________________________________________________________________________________________________

/*  Método cambiarIdiomaIng:
    ------------------------
        *) Parámetros (Input):
                1) (View) view: Vista asociada al Activity actual.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se utiliza cuando el usuario pulsa la etiqueta "Inglés" del Login.
                Traduce la aplicación completa al idioma inglés.
*/
    public void cambiarIdiomaIng(View v){
        // Cambiar variable auxiliar que indica el idioma de la aplicación
        idioma = "en";

        // Instanciar el Gestor de idiomas
        GestorIdioma gI = new GestorIdioma();

        // Asignar el idioma a la pantalla actual
        gI.cambiarIdioma(getBaseContext(), idioma);

        // Recargar de nuevo la actividad
        Intent intent = new Intent(this, Login.class);

        // Enviar el idioma actual
        intent.putExtra("idioma",idioma);
        finish();
        startActivity(intent);
    }

// _________________________________________________________________________________________________

/*  Método onSaveInstanceState:
    ---------------------------
        *) Parámetros (Input):
                1) (Bundle) outState: Contiene el diseño predeterminado del Activity.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta antes de eliminar la actividad. Guarda el idioma actual en el
                Bundle, para que al refrescar, la actividad se mantenga.
*/
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Guardar en el Bundle el idioma actual de la aplicación
        outState.putString("idioma",idioma);
    }

// _________________________________________________________________________________________________

/*  Método validarUsuario:
    ----------------------
        *) Parámetros (Input):
                1) (String) pUrl: Contiene la dirección URL del PHP que valida al usuario en la BBDD.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta tras validar los datos introducidos en el formulario.
                Se encarga de comprobar si el usuario se encuentra registrado en la app:
                    - Si está registrado: Redirige la ejecución al menú principal de la aplicación.
                    - Si no está registrado: Muestra un mensaje de error.

*/
   private void validarUsuario(String pUrl){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, pUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {   // Si la petición ha sido exitosa

                JSONObject json = null;
                try {
                    // Parsear la respuesta a JSON
                    json = new JSONObject(response);

                    // Comprobar que el usuario se encuentre registrado
                    if(json.get("exist").toString().equals("true")){

                        // Crear un intent para pasar a la Actividad Menu_Principal
                        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);

                        // Guardar los datos del usuario (para mantener la sesión)
                        intent.putExtra("nombreUsuario", json.get("nombre").toString());
                        intent.putExtra("apellidoUsuario", json.get("apellido").toString());
                        intent.putExtra("emailUsuario", json.get("email").toString());
                        intent.putExtra("fotoUsuario", json.get("foto").toString());

                        // Guardar el idioma actual de la aplicación
                        intent.putExtra("idioma",idioma);

                        // Cargar el Menú Principal
                        startActivity(intent);
                        finish();

                    } else{ // Si el usuario no se encuentra registrado --> Imprimir mensaje de error
                        String msg = getResources().getString(R.string.t_loginIncorrecto);
                        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {    // Si ha ocurriddo un error
                // Mostrar un mensaje de error
                String msg = getResources().getString(R.string.t_errorBBDD);
                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError { // Parámetros que enviar con la petición
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id", "login");  // Identificador para que el PHP sepa qué función ejecutar
                parametros.put("email", et_email.getText().toString());
                parametros.put("password", et_password.getText().toString());
                return parametros;
            }
        };
        // Añadir petición a la cola
        requestQueue.add(stringRequest);
    }


}