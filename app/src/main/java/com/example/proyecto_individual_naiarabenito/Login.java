package com.example.proyecto_individual_naiarabenito;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto_individual_naiarabenito.db.DBHelper;

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
            Toast.makeText(this,"Debes ingresar tu contraseña", Toast.LENGTH_LONG).show();
        } else{

            // Si se han completado todos los campos
            //try{
                // Realizar la verificación del login
            DBHelper dbHelper = new DBHelper(this);
            boolean existe = dbHelper.verificarUsuarioLogin(email, password);
                /*SQLiteDatabase db = dbHelper.getWritableDatabase();

                // Comprobar que el usuario y constraseña se encuentra registrado en la BBDD
                Cursor fila = db.rawQuery("SELECT email, password FROM t_usuarios WHERE email='" + email + "' AND password='" + password + "'", null);

                // Comprobar si ha encontrado al usuario
                if(fila.moveToFirst()){     // Si existe --> Ir a Menu_Principal

                    // Cerrar la BBDD
                    db.close();*/

                if(existe){ // Si existe --> Ir a Menu_Principal
                    // Crear un intent para pasar a la Actividad Menu_Principal
                    Intent intent = new Intent(this, Menu_Principal.class);

                    // Guardar los datos del usuario
                    intent.putExtra("emailUsuario", email);
                    intent.putExtra("passwordUsuario", password);
                    startActivity(intent);
                    finish();
                }
                // Si no existe --> Error
                else{
                    Toast.makeText(this,"Email o contraseña incorrectos. Intente de nuevo", Toast.LENGTH_LONG).show();
                    //db.close();
                }/*
            }
            catch (Exception e){
                Toast.makeText(this,"ERROR EN LA BASE DE DATOS", Toast.LENGTH_LONG).show();
            }*/
        }
    }
}