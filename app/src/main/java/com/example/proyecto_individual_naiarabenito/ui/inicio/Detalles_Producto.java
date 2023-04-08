
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.ui.inicio;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyecto_individual_naiarabenito.activities.GestorIdioma;
import com.example.proyecto_individual_naiarabenito.activities.Menu_Principal;
import com.example.proyecto_individual_naiarabenito.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/* ################################### CLASE DETALLES_PRODUCTO #####################################
    *) Descripción:
        La función de esta clase es mostrar y gestionar la vista que muestra los detalles del
        producto seleccionado.

    *) Tipo: Activity
*/
public class Detalles_Producto extends AppCompatActivity {

// ___________________________________________ Variables ___________________________________________
    private String nUser;       // Nombre del usuario logueado
    private String aUser;       // Apellido del usuario logueado
    private String eUser;       // Email del usuario logueado
    private String fUser;       // Foto de perfil del usuario logueado

    private ImageView imgProd;         // Imagen del producto seleccionado
    private TextView nombreProd;       // Nombre del producto seleccionado
    private TextView descripProd;      // Descripción del producto seleccionado
    private TextView precioProd;       // Precio del producto seleccionado
    private TextView cantidadProd;     // Cantidad del producto seleccionado
    private int cantidad = 1;       // Cantidad seleccionada 
    private int imagen;             // Id de la imagen del producto seleccionado

    private String idioma;          // String que contiene el idioma actual de la aplicación
    private RequestQueue requestQueue;

// ____________________________________________ Métodos ____________________________________________

/*  Método onCreate:
    ----------------
        *) Parámetros (Input):
                1) (Bundle) savedInstanceState: Contiene el diseño predeterminado del Activity.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta la primera vez que se crea el Activity y crea la vista con 
                la información del producto seleccionado.
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(getBaseContext());
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

        // Cargar las preferencias configuradas por el usuario
        cargar_configuracion();
        setContentView(R.layout.activity_detalles_producto);


        // Obtener los objetos de la vista
        imgProd = findViewById(R.id.det_prod_img);
        nombreProd = findViewById(R.id.det_prod_nombre);
        descripProd = findViewById(R.id.det_prod_descrip);
        precioProd = findViewById(R.id.det_prod_precio);
        cantidadProd = findViewById(R.id.det_prod_cantidad);

        // Obtener la información del producto
        getInformacion();

    }

// _________________________________________________________________________________________________

/*  Método getInformacion:
    ----------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método obtiene la información del producto obtenida en el intent y la imprime
                en la vista.
*/
    private void getInformacion(){

        // Recibir los datos del intent
        Bundle extras = getIntent().getExtras();

        // Comprobar que se ha recibido algo
        if(extras != null){     // Si ha llegado información: Imprimirla
            String n = extras.getString("nombreProducto");
            String descripcion = extras.getString("decripProducto");
            imagen = extras.getInt("imgProducto");
            double precio = extras.getDouble("precioProducto");
            nombreProd.setText(n);
            descripProd.setText(descripcion);
            imgProd.setImageResource(imagen);
            precioProd.setText(String.valueOf(precio));

            // Obtener la información del usuario logueado
            nUser = extras.getString("nombreUsuarionombreUsuario");
            aUser = extras.getString("apellidoUsuario");
            eUser = extras.getString("emailUsuario");
            fUser = extras.getString("fotoUsuario");
        }
    }

// _________________________________________________________________________________________________

/*  Método regresar:
    ----------------------
        *) Parámetros (Input):
                1) (View) v: Vista asociada al Activity actual.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta cuando el usuario pulsa el botón "REGRESAR". Se encarga de
                redirigir la ejecución al Menú Principal.
*/
    public void regresar (View v){
        // Crear el intent que redirige la ejecución al Menú Principal
        Intent intent = new Intent(this, Menu_Principal.class);

        // Pasarle la información del usuario (mantener la sesión)
        intent.putExtra("nombreUsuario", nUser);
        intent.putExtra("apellidoUsuario", aUser);
        intent.putExtra("emailUsuario", eUser);
        intent.putExtra("fotoUsuario", fUser);

        // Guardar el idioma actual de la aplicación
        intent.putExtra("idioma", idioma);

        // Evitar que se llene la pila de Actividades (Menú Principal solo tiene una instancia)
        onNewIntent(intent);
        finish();
    }

// _________________________________________________________________________________________________

/*  Método anadirCarrito:
    ----------------------
        *) Parámetros (Input):
                1) (View) v: Vista asociada al Activity actual.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta cuando el usuario pulsa el botón "AÑADIR AL CARRITO".
                Se encarga de actualizar al BBDD y añadir el producto al carrito. Por último
                redirige la ejecución al Menú Principal.
*/
    public void anadirCarrito (View v){

        // Añadir pedido en la BBDD
        anadirOrdenes("http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/nbenito012/WEB/gestionar_ordenes.php");

    }

// _________________________________________________________________________________________________

/*  Método btn_plus:
    ----------------
        *) Parámetros (Input):
                1) (View) v: Vista asociada al Activity actual.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta cuando el usuario pulsa el botón "+".
                Se encarga de actualizar (aumentar) el número que indica la cantidad que deseamos
                pedir.
*/
    public void btn_plus (View v){
        // Aumentar cantidad y actualizar la vista
        cantidad++;
        cantidadProd.setText(String.valueOf(cantidad));
    }
// _________________________________________________________________________________________________

    /*  Método btn_minus:
        -----------------
            *) Parámetros (Input):
                    1) (View) v: Vista asociada al Activity actual.
            *) Parámetro (Output):
                    void
            *) Descripción:
                    Este método se ejecuta cuando el usuario pulsa el botón "+".
                    Se encarga de actualizar (reducir) el número que indica la cantidad que deseamos
                    pedir.
    */
    public void btn_minus (View v){

        // Comprobar que la cantidad sea mayor a 1
        if(cantidad > 1){       // Si es mayor: Reducir en uno la cantidad
            cantidad--;
        }
        // Actualizar la vista
        cantidadProd.setText(String.valueOf(cantidad));
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
        if(modoOscuro){         // Si el modo oscuro está activado: Pintar el fondo de negro
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.black));
        } else{                 // Si el modo oscuro está desactivado: Pintar el fondo de blanco
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.white));
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

    private void anadirOrdenes(String pUrl){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, pUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    if(json.get("done").toString().equals("true")){
                        String msg = getResources().getString(R.string.t_pedidoAnadido);
                        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();

                        // Crear el intent que redirige la ejecución al Menú Principal
                        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);

                        // Guardar los datos del usuario (mantener la sesión)
                        intent.putExtra("nombreUsuario", nUser);
                        intent.putExtra("apellidoUsuario", aUser);
                        intent.putExtra("emailUsuario", eUser);
                        intent.putExtra("fotoUsuario", fUser);

                        // Guardar el idioma actual de la aplicación
                        intent.putExtra("idioma", idioma);

                        startActivity(intent);
                        finish();
                    } else{             // Si no se ha añadido: Imprimir mensaje de error
                        String msg = getResources().getString(R.string.t_errorBBDD);
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
                Toast.makeText(getBaseContext(),msg, Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("operacion", "añadir");
                parametros.put("nombre", nombreProd.getText().toString());
                parametros.put("precio", precioProd.getText().toString());
                parametros.put("imagen", String.valueOf(imagen));
                parametros.put("cantidad", cantidadProd.getText().toString());
                parametros.put("email", eUser);
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }
}