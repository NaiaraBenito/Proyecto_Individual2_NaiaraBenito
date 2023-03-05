
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
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
import com.example.proyecto_individual_naiarabenito.db.DBHelper;
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

// ____________________________________________ Métodos ____________________________________________

/*  Método onCreate:
    ----------------
        *) Parámetos (Input):
                1) (Bundle) savedInstanceState: Contiene el diseño predeterminado del Activity.
        *) Parámetro (Output):
                void
        *) Descripción:
                Éste método se ejecuta la primera vez que se crea el Activity y crea la vista del
                registro
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }

// _________________________________________________________________________________________________

/*  Método volverLogin:
    -------------------
        *) Parámetos (Input):
                1) (View) v: Vista asociada al Activity actual
        *) Parámetro (Output):
                void
        *) Descripción:
                Éste método se ejecuta al pulsar el texto "Ya tengo cuenta". Redirige la ejecución
                del programa al Activity Login sin registrar al usuario.
*/
    public void volverLogin(View v){

        // Crear el intent que redirige la ejecución al Login
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

// _________________________________________________________________________________________________

/*  Método registrarUsuario:
    ------------------------
        *) Parámetos (Input):
                1) (View) v: Vista asociada al Activity actual
        *) Parámetro (Output):
                void
        *) Descripción:
                Éste método se ejecuta cuando el usuario pulsa el botón "REGISTRAR".
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
            Toast.makeText(this,"Debes ingresar tu nombre", Toast.LENGTH_LONG).show();
        } else if (apellido.equals("")) {
            Toast.makeText(this,"Debes ingresar tu apellido", Toast.LENGTH_LONG).show();
        } else if (email.equals("")) {
            Toast.makeText(this,"Debes ingresar tu email", Toast.LENGTH_LONG).show();
        } else if (password1.equals("") || password2.equals("")) {
            Toast.makeText(this,"Debes ingresar tu contraseña", Toast.LENGTH_LONG).show();
        } else if (!password1.equals(password2)) {    // Comprobar que las contraseñas coincidan
            Toast.makeText(this,"Las contraseñas deben coincidir", Toast.LENGTH_LONG).show();
        } else{     // Si se han completado todos los campos: Comprobar email + Registrar

            // Crear patrón para validar el email
            Pattern pattern = Pattern.compile("([a-z\\d]+(\\.?[a-z\\d])*)+@(([a-z]+)\\.([a-z]+))+");
            Matcher mather = pattern.matcher(email);

            // Comprobar que el email sea válido
            if (mather.find()) {    // El email ingresado es válido
                // Registrar el usuario en la BBDD
                DBHelper dbHelper = new DBHelper(this);
                String msg = dbHelper.registrarUsuario(nombre, apellido, email, password1);

                // Imprimir estado del registro
                Toast.makeText(this,msg, Toast.LENGTH_LONG).show();

                // Comprobar que el usuario se haya registrado con éxito
                if(msg.equals("El usuario " + nombre + " ha sido registrado con exito")) {
                    // Volver al Login
                    volverLogin(v);
                }
            } else {     // El email ingresado es inválido: Imprimir mensaje de error
                Toast.makeText(this, "El email ingresado es inválido", Toast.LENGTH_LONG).show();
            }
        }
    }

// _________________________________________________________________________________________________

/*  Método cargar_configuracion:
    ----------------------------
        *) Parámetos (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Éste método carga las preferencias configuradas por el usuario (modo oscuro,
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
}