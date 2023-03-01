package com.example.proyecto_individual_naiarabenito.ui.inicio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto_individual_naiarabenito.Menu_Principal;
import com.example.proyecto_individual_naiarabenito.R;
import com.example.proyecto_individual_naiarabenito.db.DBHelper;
import com.example.proyecto_individual_naiarabenito.ui.inicio.Producto;

public class Detalles_Producto extends AppCompatActivity {

    //Variables
    String nUser;
    String aUser;
    String eUser;

    TextView anadir_carrito;
    ImageView imgProd;
    TextView nombreProd;
    TextView descripProd;
    TextView precioProd;
    Button minus;
    Button plus;
    TextView cantidadProd;
    int cantidad = 1;
    int imagen;
    Producto producto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_producto);

        // Gestionar funcionamiento del carrito

        anadir_carrito = (TextView) findViewById(R.id.btn_anadirCarrito_det_prod);
        imgProd = (ImageView) findViewById(R.id.det_prod_img);
        nombreProd = (TextView) findViewById(R.id.det_prod_nombre);
        descripProd = (TextView) findViewById(R.id.det_prod_descrip);
        precioProd = (TextView) findViewById(R.id.det_prod_precio);
        minus = (Button) findViewById(R.id.det_prod_minus);
        plus = (Button) findViewById(R.id.det_prod_plus);
        cantidadProd = (TextView) findViewById(R.id.det_prod_cantidad);


        getInformacion();

    }

    public void getInformacion(){

        // Recibir los datos
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String n = extras.getString("nombreProducto");
            String descripcion = extras.getString("decripProducto");
            imagen = extras.getInt("imgProducto");
            double precio = extras.getDouble("precioProducto");
            nombreProd.setText(n);
            descripProd.setText(descripcion);
            imgProd.setImageResource(imagen);
            precioProd.setText(String.valueOf(precio));

            nUser = extras.getString("nombreUsuario");
            aUser = extras.getString("apellidoUsuario");
            eUser = extras.getString("emailUsuario");
        }
    }

    // Método que te devuelve al inicio
    public void regresar (View v){
        Intent intent = new Intent(this, Menu_Principal.class);
        intent.putExtra("nombreUsuario", nUser);
        intent.putExtra("apellidoUsuario", aUser);
        intent.putExtra("emailUsuario", eUser);
        startActivity(intent);
        finish();
    }

    public void anadirCarrito (View v){
        DBHelper dbHelper = new DBHelper(this);
        boolean insertado = dbHelper.anadirOrden(nombreProd.getText().toString(),
                Double.parseDouble(precioProd.getText().toString()),
                imagen,
                Integer.parseInt(cantidadProd.getText().toString()),
                eUser);

        if(insertado){
            Toast.makeText(this,"Tu compra se ha añadido al carrito", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"ERROR BASE DE DATOS", Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent(this, Menu_Principal.class);
        intent.putExtra("nombreUsuario", nUser);
        intent.putExtra("apellidoUsuario", aUser);
        intent.putExtra("emailUsuario", eUser);
        startActivity(intent);
        finish();
    }

    public void btn_plus (View v){
        cantidad++;
        cantidadProd.setText(String.valueOf(cantidad));
    }

    public void btn_minus (View v){
        if(cantidad > 1){
            cantidad--;
        }
        cantidadProd.setText(String.valueOf(cantidad));
    }
}