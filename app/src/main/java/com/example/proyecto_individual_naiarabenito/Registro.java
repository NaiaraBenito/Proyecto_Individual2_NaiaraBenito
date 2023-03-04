package com.example.proyecto_individual_naiarabenito;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.proyecto_individual_naiarabenito.db.DBHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {

    // Variables auxiliares
    private EditText et_nombre;
    private EditText et_apellido;
    private EditText et_email;
    private EditText et_password1;
    private EditText et_password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        cargar_configuracion();

        // Obtener datos metidos por el usuario
        et_nombre = (EditText) findViewById(R.id.et_nombreRegistro);
        et_apellido = (EditText) findViewById(R.id.et_apellidoRegistro);
        et_email = (EditText) findViewById(R.id.et_emailRegistro);
        et_password1 = (EditText) findViewById(R.id.et_passwordRegistro1);
        et_password2 = (EditText) findViewById(R.id.et_passwordRegistro2);
    }

    // Método llamado por el botón Volver que llama a la actividad de Login
    public void volverLogin(View v){

        // Crear un intent para pasar a la Actividad Registro
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    // Método llamado por el botón Volver que llama a la actividad de Login
    public void registrarUsuario(View v){

        // Obtener los datos introducidos por el usuario (eliminando los espacios en blanco del comienzo)
        String nombre = et_nombre.getText().toString().replaceAll("^\\s*","");;
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
        } else{     // Si se han completado todos los campos

            // Patrón para validar el email
            Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");
            Matcher mather = pattern.matcher(email);

            if (mather.find() == true) {    // El email ingresado es válido
                // Llamada al método que añade al nuevo usuario a la BBDD
                DBHelper dbHelper = new DBHelper(this);
                String msg = dbHelper.registrarUsuario(nombre, apellido, email, password1);
                Toast.makeText(this,msg, Toast.LENGTH_LONG).show();

                // Volver al Login
                volverLogin(v);
            } else {     // El email ingresado es inválido
                Toast.makeText(this, "El email ingresado es inválido", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void cargar_configuracion(){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        boolean modoOscuro = sp.getBoolean("modo_oscuro", false);
        LinearLayout l = (LinearLayout) findViewById(R.id.registro);
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