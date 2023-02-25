package com.example.proyecto_individual_naiarabenito;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Menu_Principal extends AppCompatActivity {

    // Variables auxiliares
    private TextView tv_email;
    private TextView tv_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        // Obtener los campos en donde imprimiremos la información del usuario
        tv_email = (TextView) findViewById(R.id.tv_MuestraEmail);
        tv_password = (TextView) findViewById(R.id.tv_MuestraPassword);

        // Sacar datos almacenados en el Intent
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            String email = extras.get("emailUsuario").toString();
            String password = extras.get("passwordUsuario").toString();

            // Imprimir los datos en los campos
            tv_email.setText(email);
            tv_password.setText(password);
        }
    }

    // Método llamado por el botón Volver que llama a la actividad de Login
    public void volverLogin(View v){

        // Crear un intent para pasar a la Actividad Registro
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}