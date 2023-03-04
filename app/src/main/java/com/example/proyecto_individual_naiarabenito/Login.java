package com.example.proyecto_individual_naiarabenito;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.proyecto_individual_naiarabenito.db.DBHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    // Variables auxiliares
    private EditText et_email;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Obtener los campos que editan los usuarios
        //et_nombre = (EditText) findViewById(R.id.et_nombreLogin);
        et_email = (EditText) findViewById(R.id.et_emailLogin);
        et_password = (EditText) findViewById(R.id.et_passwordLogin);
        cargar_configuracion();
    }

    // Método llamado por el texto Crear cuenta que llama a la actividad de Registro
    public void crearCuenta(View v){

        // Crear un intent para pasar a la Actividad Registro
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
        finish();
    }

    // Método llamado por el botón Ingresar que verifica que los datos sean correctos e inicia la actividad del Menú Principal
    public void ingresar(View v){

        // Obtener los datos introducidos por el usuario (eliminando los espacios en blanco del comienzo)
        String email = et_email.getText().toString().replaceAll("^\\s*","");;
        String password = et_password.getText().toString().replaceAll("^\\s*","");;

        // Comprobar que los campos no se encuentren vacíos
        if(email.equals("")){
            Toast.makeText(this,"Debes ingresar tu email", Toast.LENGTH_LONG).show();
        } else if (password.equals("")) {
            Toast.makeText(this, "Debes ingresar tu contraseña", Toast.LENGTH_LONG).show();
        } else{

            // Patrón para validar el email
            Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");
            Matcher mather = pattern.matcher(email);

            if (mather.find() == true) {    // El email ingresado es válido
                // Realizar la verificación del login
                DBHelper dbHelper = new DBHelper(this);
                //boolean existe = dbHelper.verificarUsuarioLogin(email, password);
                String[] datos = dbHelper.verificarUsuarioLogin(email, password);
                if(datos != null){ // Si existe --> Ir a Menu_Principal

                    // Obtener todos los datos del usuario
                    //dbHelper.getDatos(email);

                    // Crear un intent para pasar a la Actividad Menu_Principal
                    Intent intent = new Intent(this, Menu_Principal.class);

                    // Guardar los datos del usuario
                    intent.putExtra("nombreUsuario", datos[0]);
                    intent.putExtra("apellidoUsuario", datos[1]);
                    intent.putExtra("emailUsuario", datos[2]);
                    startActivity(intent);
                    finish();
                }
                // Si no existe --> Error
                else{
                    Toast.makeText(this,"Email o contraseña incorrectos. Intente de nuevo", Toast.LENGTH_LONG).show();
                }
            } else {    // El email ingresado es inválido
                Toast.makeText(this, "El email ingresado es inválido", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void cargar_configuracion(){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        boolean modoOscuro = sp.getBoolean("modo_oscuro", false);
        LinearLayout l = (LinearLayout) findViewById(R.id.login);

        if(modoOscuro){
            l.setBackgroundColor(getResources().getColor(R.color.gris_claro));

        } else{
            l.setBackgroundColor(getResources().getColor(R.color.white));
        }

        String ori = sp.getString("orientacion","false");
        if("1".equals(ori)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else if("2".equals(ori)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if("3".equals(ori)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
}