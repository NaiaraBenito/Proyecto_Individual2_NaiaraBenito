package com.example.proyecto_individual_naiarabenito;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Detalles_Producto extends AppCompatActivity {

    //Variables
    TextView nombre;
    TextView descrip;
    ImageView img;

    String nUser;
    String aUser;
    String eUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_producto);

        nombre = (TextView) findViewById(R.id.det_prod_nombre);
        descrip = (TextView) findViewById(R.id.det_prod_descrip);
        img = (ImageView) findViewById(R.id.det_prod_img);

        // Recibir los datos
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String n = extras.getString("nombreProducto");
            String descripcion = extras.getString("decripProducto");
            int imagen = extras.getInt("imgProducto");
            nombre.setText(n);
            descrip.setText(descripcion);
            img.setImageResource(imagen);

            nUser = extras.getString("nombreUsuario");
            aUser = extras.getString("apellidoUsuario");
            eUser = extras.getString("emailUsuario");
        }
    }

    // Método que te devuelve al inicio
    public void btn_aceptar (View v){
        Intent intent = new Intent(this, Menu_Principal.class);
        intent.putExtra("nombreUsuario", nUser);
        intent.putExtra("apellidoUsuario", aUser);
        intent.putExtra("emailUsuario", eUser);
        startActivity(intent);
        finish();
    }
}