
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.ui.cesta;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.WorkManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyecto_individual_naiarabenito.GestorIdioma;
import com.example.proyecto_individual_naiarabenito.Mapa;
import com.example.proyecto_individual_naiarabenito.Menu_Principal;
import com.example.proyecto_individual_naiarabenito.PedidoWorkManager;
import com.example.proyecto_individual_naiarabenito.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/* ####################################### CLASE CESTA_FRAGMENT ####################################
    *) Descripción:
        La función de esta clase es mostrar y gestionar la configuración del carrito.

    *) Tipo: Fragment
*/
public class CestaFragment extends Fragment implements InterfazActualizarCesta {

// ___________________________________________ Variables ___________________________________________
    private String[] datosUser; // Lista que contiene los datos del usuario para mantener la sesión

    private List<Orden> lista_ordenes;  // Lista que contiene los pedidos que el usuario ha añadido a la cesta
    private RecyclerView recyclerViewOrdenes;   // RecyclerView que muestra la lista de pedidos
    private ListAdapter_Ordenes adapterOrdenes; // Adaptador para gestionar lista de pedidos

    private TextView precioProd;    // TextView que contiene el coste total de la suma de los productos
    private TextView envio;         // TextView que contiene los costes de envío
    private TextView impuestos;     // TextView que contiene los impuestos
    private TextView total;         // TextView que contiene el coste total del pedido
    private Button pagar;           // Botón para finalizar el pedido
    private Button mapa;

    private String tituloPDF;       // Variable que contiene el título de la factura que se genera al finalizar el pedido
    private String descripcionPDF;  // Variable que contiene el cuerpo de la factura

    private PendingIntent pendingIntent;        // Variable que gestiona la notificación de compra
    private PendingIntent siPendingIntent;      // Variable que gestiona la notificación de compra
    private PendingIntent noPendingIntent;      // Variable que gestiona la notificación de compra
    final static String CHANNEL_ID = "NOTIFICACION";    // Variable que contiene el id del canal para la notificación
    final static int NOTIFICACION_ID = 0;       // Variable que contiene el id de la notificación

    private String idioma;          // String que contiene el idioma actual de la aplicación
    private RequestQueue requestQueue;
    private View v;
    private CestaFragment cf;
    private String nombreProd;

// ____________________________________________ Métodos ____________________________________________

/*  Método onCreateView:
    --------------------
        *) Parámetros (Input):
                1) (LayoutInflater) inflater: Se utiliza para enlazar el fragmento con su vista.
                2) (ViewGroup) container: Contiene la vista principal a la que se debe adjuntar la
                   vista del fragmento.
                3) (Bundle) savedInstanceState: Contiene el diseño predeterminado del Fragmento.
        *) Parámetro (Output):
                (View) view: Vista asociada al Fragment actual.
        *) Descripción:
                Este método se ejecuta cuando el usuario se ha logueado correctamente y accede al
                apartado Cesta del menú.
*/
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        requestQueue = Volley.newRequestQueue(getContext());
        // Obtener el idioma de la aplicación del Bundle (mantener idioma al girar la pantalla)
        if (savedInstanceState != null) {
            idioma = savedInstanceState.getString("idioma");
        }

        // Obtener el idioma de la aplicación del intent (mantener idioma al moverse por la aplicación)
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null){
            idioma = (String) extras.get("idioma");
        }

        // Si en la anterior ejecución se ha guardado el idioma
        if (idioma != null){
            // Instanciar el Gestor de idiomas
            GestorIdioma gI = new GestorIdioma();

            // Asignar el idioma a la pantalla actual
            gI.cambiarIdioma(getActivity().getBaseContext(), idioma);
        }

        // Obtener la vista
        View view = inflater.inflate(R.layout.fragment_cesta, container, false);

        // Cargar las preferencias configuradas por el usuario
        cargar_configuracion(view);

        // Obtener los datos del usuario para mantener la sesión
        datosUser = new String[3];
        datosUser[0] = getActivity().getIntent().getExtras().getString("nombreUsuario");
        datosUser[1] = getActivity().getIntent().getExtras().getString("apellidoUsuario");
        datosUser[2] = getActivity().getIntent().getExtras().getString("emailUsuario");

        // Cargar la lista de pedidos
        cargarListaOrdenes(view);

        // Obtener los Objetos de la vista
        pagar = view.findViewById(R.id.btn_pagar_carrito);

        // Gestionar el proceso de compra del usuario y asignar acción al pulsar el botón "PAGAR"
        pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Comprobar que la lista de pedidos no esté vacía
                if(lista_ordenes.isEmpty()){    // Si está vacía: Imprimir mensaje de error
                    String msg = getResources().getString(R.string.t_cestaVacia);
                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                } else{     // Si no está vacía: Sacar diálogo de confirmación
                    // Gestionar el diálogo de confirmación del pedido
                    gestionarDialogo(view);
                }
            }
        });

        // Obtener los Objetos de la vista
        mapa = view.findViewById(R.id.btn_mapa);

        // Gestionar el proceso de compra del usuario y asignar acción al pulsar el botón "PAGAR"
        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Crear el intent que redirige la ejecución al Menú Principal
                Intent intent = new Intent(getContext(), Mapa.class);

                // Guardar los datos del usuario (mantener la sesión)
                intent.putExtra("nombreUsuario", datosUser[0]);
                intent.putExtra("apellidoUsuario", datosUser[1]);
                intent.putExtra("emailUsuario", datosUser[2]);

                // Guardar el idioma actual de la aplicación
                intent.putExtra("idioma", idioma);

                startActivity(intent);
            }
        });
        return view;
    }

// _________________________________________________________________________________________________

/*  Método cargarListaOrdenes:
    --------------------------
        *) Parámetros (Input):
            1) (View) v: Vista asociada al Fragment actual.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método carga el RecyclerView con los productos añadidos al carrito del usuario.
*/
    private void cargarListaOrdenes(View view) {
        cf = this;
        v = view;
        // Añadir los pedidos del usuario a la lista
        obtenerOrdenes("http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/nbenito012/WEB/gestionar_ordenes.php");
    }

// _________________________________________________________________________________________________

/*  Método cargarResumen:
    ---------------------
        *) Parámetros (Input):
            1) (View) v: Vista asociada al Activity actual.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método actualiza los TextView que contienen los datos de la compra (Precio
                total de los productos, impuestos, costes de envío y total del pedido).
*/
    private void cargarResumen(View view) {

        // Actualizar el coste total de la suma de todos los productos
        precioProd = view.findViewById(R.id.total_prod_carrito);
        double precioTotProd = calcularPrecioTotProductos();
        precioProd.setText(String.valueOf(precioTotProd) + "€");

        // Actualizar los costes de envío
        envio = view.findViewById(R.id.total_envio_carrito);
        double env = calcularEnvio(precioTotProd);
        envio.setText(String.valueOf(env) + "€");

        // Actualizar los costes por impuestos (%10 del total de los productos)
        impuestos = view.findViewById(R.id.total_impuesto_carrito);
        double imp = calcularImpuestos(precioTotProd);
        impuestos.setText(String.valueOf(imp) + "€");

        // Actualizar el coste total del pedido
        total = view.findViewById(R.id.total_carrito);
        double tot = calcularTotal(precioTotProd, imp, env);
        total.setText(String.valueOf(tot) + "€");
    }

// _________________________________________________________________________________________________

/*  Método calcularPrecioTotProductos:
    ----------------------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                (double) total: Contiene el coste total de los productos de la cesta.
        *) Descripción:
                Este método calcula el coste total de los productos que tiene el usuario en la cesta.
*/
    public double calcularPrecioTotProductos(){

        // Inicializar variable de retorno
        double total = 0.00;

        // Sumar todos coste de todos los productos de la cesta
        for(int i = 0; i < adapterOrdenes.getItemCount(); i++){     // Por cada producto

            // Multiplicar la cantidad por el precio del producto
            double auxPrecio = (double) Math.round(adapterOrdenes.getListaOrden().get(i).getCantidadProd() * adapterOrdenes.getListaOrden().get(i).getPrecioProd() * 100.0) / 100.0;

            // Actualizar variable de retorno
            total = Math.round((total + auxPrecio) * 100.0) / 100.0;
        }
        return total;
    }

// _________________________________________________________________________________________________

/*  Método calcularImpuestos:
    -------------------------
        *) Parámetros (Input):
                1) (double) pPrecio: Contiene el coste total de los productos de la cesta.
        *) Parámetro (Output):
                (double) auxImp: Contiene el coste de los impuestos del pedido.
        *) Descripción:
                Este método calcula el coste de los impuestos del pedido realizando %10 del total de
                los productos.
*/
    public double calcularImpuestos(double pPrecio){

        // Inicializar variable de retorno
        double auxImp = 0;

        // Calcular los impuestos
        auxImp = Math.round(((pPrecio * 0.1) * 100.0))/100.0;
        return auxImp;
    }

// _________________________________________________________________________________________________

/*  Método calcularEnvio:
    -------------------------
        *) Parámetros (Input):
                1) (double) pPrecio: Contiene el coste total de los productos de la cesta.
        *) Parámetro (Output):
                (double) auxenvio: Contiene los costes de envío del pedido.
        *) Descripción:
                Este método calcula los costes de envio de la compra:
                    - Si hay algún producto en la cesta: Los costes de envío serán de 3€.
                    - Si no hay ningún producto en la cesta: Los costes de envío serán de 0€.
*/
    public double calcularEnvio(double pPrecio){

        // Inicializar variable de retorno
        double auxenvio = 0.00;

        // Comprobar si hay algún producto en la cesta
        if (pPrecio > 0){
            // Actualizar variable de retorno
            auxenvio = 3.00;
        }
        return auxenvio;
    }


// _________________________________________________________________________________________________

/*  Método calcularTotal:
    -------------------------
        *) Parámetros (Input):
                1) (double) pPrecioProd: Contiene el coste total de los productos de la cesta.
                2) (double) pImpuestos: Contiene el coste por impuestos del pedido.
                3) (double) pEnvio: Contiene los costes de envio de la compra.
        *) Parámetro (Output):
                (double) auxTotal: Contiene el coste total del pedido.
        *) Descripción:
                Este método calcula el coste total del pedido con la suma del coste en productos,
                los impuestos y los costes de envío.
*/
    public double calcularTotal(double pPrecioProd, double pImpuestos, double pEnvio){

        // Inicializar variable de retorno
        double auxTotal = 0;

        // Calcular el coste total del pedido
        auxTotal = Math.round((pPrecioProd + pImpuestos + pEnvio) * 100.0) / 100.0;
        return auxTotal;

    }

// _________________________________________________________________________________________________

/*  Método gestionarDialogo:
    ------------------------
        *) Parámetros (Input):
            1) (View) v: Vista asociada al Activity actual.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método crea el diálogo de confirmación del pedido y gestiona la respuesta.
*/
    private void gestionarDialogo(View view){

        // Crear Diálogo de confirmación
        AlertDialog.Builder confirmacion = new AlertDialog.Builder(view.getContext());

        String dTitulo = getResources().getString(R.string.d_confirmarPedido);
        String dContenido = getResources().getString(R.string.d_confirmarPedidoMsg);

        confirmacion.setTitle(dTitulo);      // Asignar el título
        confirmacion.setMessage(dContenido);    // Asignar el contenido
        confirmacion.setCancelable(false);  // Indicar que no se pueda cancelar

        // Añadir y gestionar el botón "Si"
        String dSi = getResources().getString(R.string.d_si);

        confirmacion.setPositiveButton(dSi, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                // Notificar que se ha confirmado la compra
                String msg = getResources().getString(R.string.t_confirmarCompra);
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();

                // Crear y gestionar la notificación de agradecimiento
                gestionarNotificacion();

                // Añadir una tarea para notificar cuando el pedido está listo

                // Crear un objeto Calendar para obtener la fecha actual
                Calendar alertaCal = Calendar.getInstance();

                // Sumar 5 minutos a la fecha actual
                alertaCal.add(Calendar.MINUTE,5);

                // Crear un tag random
                String tag = UUID.randomUUID().toString();

                // Calcular la duración para que se lance la notificación
                Long duracion  = alertaCal.getTimeInMillis() - System.currentTimeMillis();

                // Generar un id random
                int random = (int)(Math.random()*50+1);

                // Obtener el titulo y el contenido de la notificación
                String pedTitulo = getResources().getString(R.string.n_PNotifTitulo);
                String pedContenido = getResources().getString(R.string.n_PNotifContenido);
                Data data = guardarData(pedTitulo,pedContenido, random);

                // Ejecutar el servicio en segundo plano
                PedidoWorkManager.NotificarPedido(duracion,data,tag);
            }
        });

        // Añadir y gestionar el botón "No"
        String dNo = getResources().getString(R.string.d_no);
        confirmacion.setNegativeButton(dNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) { } // Si pulsa "No": No hacer nada
        });

        // Crear y mostrar el diálogo
        confirmacion.create().show();
    }

// _________________________________________________________________________________________________

/*  Método gestionarNotificacion:
    -----------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método crea la el canal y la notificación de agradecimiento tras el pedido,
                además de gestionar su funcionamiento, como actualizar la vista y la BBDD.
*/
    private void gestionarNotificacion(){

        // Crear el contenido de la factura de la compra realizada por el usuario
        disenarFactura();

        // Crear un canal para una notificación
        crearNotificationChannel();

        // Crear la notificación que agradece la compra
        crearNotificacion();

        // Comprobar que la aplicación tenga permisos para utilizar notificaciones
        if(!comprobarPermiso()){        // Si no tiene permiso: Pide los permisos
            pedirPermisos();
        }

        // Eliminar los productos comprados de la BBDD
        for(int i = 0; i < lista_ordenes.size(); i++){  // Por cada producto

            // Obtener la Orden
            Orden o = lista_ordenes.get(i);
            nombreProd = o.getNombreProd();

            // Eliminar la orden del la BBDD
            eliminarOrdenes("http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/nbenito012/WEB/gestionar_ordenes.php");
        }

        // Vaciar la lista de órdenes
        while(!lista_ordenes.isEmpty()){
            lista_ordenes.remove(0);
        }

        // Actualizar lista del recyclerView
        adapterOrdenes.setListaOrden(lista_ordenes);

    }

// _________________________________________________________________________________________________

/*  Método crearNotificationChannel:
    --------------------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método crea la un canal para poder añadir una notificación.
*/
    public void crearNotificationChannel(){

        // Comprobar que el dispositivo tenga una versión igual o superior a Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){  // Si es igual o superior: Crear canal
            CharSequence name = "Notificacion";
            NotificationChannel nc = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager nm = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
            nm.createNotificationChannel(nc);
        }
    }

// _________________________________________________________________________________________________

/*  Método crearNotificacion:
    -------------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método crea una notificación para agradecerle al usuario el pedido y
                preguntarle si desea obtener la factura del pedido.
                    - Si el usuario pulsa "Si por favor": Se le descargará la factura en la SD.
                    - Si el usuario pulsa "No gracias": La notificación desaparecerá sin hacer nada.
*/
    private void crearNotificacion() {

        // Gestionar los estados de la notificación
        setPendingIntent();     // Estado de espera
        setSiPendingIntent();   // Estado al pulsar Si
        setNoPendingIntent();   // Estado al pulsar No

        // Crear la notificación
        NotificationCompat.Builder notif = new NotificationCompat.Builder(getActivity().getApplicationContext(), CHANNEL_ID);
        notif.setSmallIcon(R.drawable.icon_notif);      // Asignar icono

        String notifTitulo = getResources().getString(R.string.n_NotifTitulo);
        String notifContenido = getResources().getString(R.string.n_NotifContenido);
        notif.setContentTitle(notifTitulo);                      // Asignar título
        notif.setContentText(notifContenido);                          // Asignar contenido
        notif.setColor(Color.rgb(239, 70, 240));     // Asignar color
        notif.setPriority(NotificationCompat.PRIORITY_DEFAULT);     // Asignar prioridad
        notif.setLights(Color.rgb(239, 70, 240), 1000, 1000);   // Asignar luces
        notif.setVibrate(new long[]{1000, 1000, 1000});     // Asignar vibración
        notif.setDefaults(Notification.DEFAULT_SOUND);      // Asignar sonido

        // Indicar que tiene un PendingIntent (acción al tocar)
        notif.setContentIntent(pendingIntent);

        // Asignar botones con acciones: Elegir icono + texto que aparece + pendingIntent
        String nSi = getResources().getString(R.string.n_si);
        String nNo = getResources().getString(R.string.n_no);
        notif.addAction(R.drawable.icon_notif, nSi, siPendingIntent);
        notif.addAction(R.drawable.icon_notif, nNo, noPendingIntent);

        //Solicitar permiso para crear una notificación
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) { // Si no tiene permiso
            // Pedir el permiso
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 11);
        }

        // Imprimir notificación
        NotificationManagerCompat nmc = NotificationManagerCompat.from(getActivity().getApplicationContext());
        nmc.notify(NOTIFICACION_ID, notif.build());
    }

// _________________________________________________________________________________________________

/*  Método setPendingIntent:
    ------------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método gestiona el estado de espera de la notificación.
*/
    private void setPendingIntent(){

        // Crear el intent que redirige la ejecución al NoActNotificacion
        Intent i = new Intent(getContext(), Menu_Principal.class);
        // Pasarle la información del usuario (mantener la sesión)
        i.putExtra("nombreUsuario", datosUser[0]);
        i.putExtra("apellidoUsuario", datosUser[1]);
        i.putExtra("emailUsuario", datosUser[2]);

        // Enviar el idioma actual
        i.putExtra("idioma",idioma);


        pendingIntent = PendingIntent.getActivity(getActivity(),0,i,PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
    }

// _________________________________________________________________________________________________

    /*  Método setSiPendingIntent:
        --------------------------
            *) Parámetros (Input):
            *) Parámetro (Output):
                    void
            *) Descripción:
                    Este método gestiona el estado de que el usuario haya pulsado el botón de
                    aceptación de la notificación. Redirige la ejecución al Activity
                    SiActNotificacion.
    */
    private void setSiPendingIntent(){

        // Crear el intent que redirige la ejecución al NoActNotificacion
        Intent i = new Intent(getContext(), SiActNotificacion.class);
        // Pasarle la información del usuario (mantener la sesión)
        i.putExtra("nombreUsuario", datosUser[0]);
        i.putExtra("apellidoUsuario", datosUser[1]);
        i.putExtra("emailUsuario", datosUser[2]);

        // Enviar el idioma actual
        i.putExtra("idioma",idioma);

        // Pasarle los datos necesarios para crear la fatura
        i.putExtra("tituloFactura", tituloPDF);
        i.putExtra("descripcionFactura", descripcionPDF);

        // Redirigir ejecución al Activity SiActNotificacion
        siPendingIntent = PendingIntent.getActivity(getActivity(),0,i,PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
    }

// _________________________________________________________________________________________________

/*  Método setNoPendingIntent:
    --------------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método gestiona el estado de que el usuario haya pulsado el botón de
                rechazo de la notificación. Redirige la ejecución al Activity NoActNotificacion.
*/
    private void setNoPendingIntent(){

        // Crear el intent que redirige la ejecución al NoActNotificacion
        Intent i = new Intent(getContext(), NoActNotificacion.class);

        // Pasarle la información del usuario (mantener la sesión)
        i.putExtra("nombreUsuario", datosUser[0]);
        i.putExtra("apellidoUsuario", datosUser[1]);
        i.putExtra("emailUsuario", datosUser[2]);

        // Enviar el idioma actual
        i.putExtra("idioma",idioma);

        // Redirigir ejecución al Activity NoActNotificacion
        noPendingIntent = PendingIntent.getActivity(getActivity(),0,i,PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
    }

// _________________________________________________________________________________________________

/*  Método disenarFactura:
    ----------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método guarda la información necesaria para realizar la factura del peddido
                realizado por el usuario.
*/
    private void disenarFactura(){

        // Asignar el título de la factura
        String cfFacturaDe = getResources().getString(R.string.cf_facturaDe);
        tituloPDF = cfFacturaDe + " " + datosUser[0] + " " + datosUser[1];

        // Inicializar el contenido de la factura
        descripcionPDF = "";

        // Recorrer la lista de los productos añadidos a la cesta
        for(int i = 0; i < lista_ordenes.size(); i++){      // Por cada producto
            // Obtener la orden
            Orden orden = lista_ordenes.get(i);

            // Obtener el nombre del producto
            String nombre = orden.getNombreProd();

            // Obtener cantidad pedida
            int cantidad = orden.getCantidadProd();

            // Calcular el precio total de ese producto
            double precioTot = Math.round(cantidad * orden.getPrecioProd() * 100.0) / 100.0;

            // Añadir información al contenido de la factura
            String cfProd = getResources().getString(R.string.cf_producto);
            String cfAmo = getResources().getString(R.string.cf_cantidad);
            String cfPrice = getResources().getString(R.string.cf_precio);
            descripcionPDF = descripcionPDF + cfProd + ": " + nombre + "   " + cfAmo + ": " + String.valueOf(cantidad) + "  " + cfPrice + ": " + String.valueOf(precioTot) + "€\n";
        }

        // Calcular resúmen del pedido
        double pTotProd = calcularPrecioTotProductos(); // Costes en productos
        double imp = calcularImpuestos(pTotProd);       // Impuestos
        double env = calcularEnvio(pTotProd);           // Costes de envío
        double tot = calcularTotal(pTotProd,imp,env);   // Total del pedido

        // Añadir información al contenido de la factura
        String sFacTotProd = getResources().getString(R.string.cf_etiqPrecioProd);
        String sFacEnvio = getResources().getString(R.string.cf_etiqCosteEnvio);
        String sFacImp = getResources().getString(R.string.cf_etiqImpuestos);
        descripcionPDF = descripcionPDF + "\n\n" + sFacTotProd + " " + String.valueOf(pTotProd) + "€";
        descripcionPDF = descripcionPDF + "\n" + sFacEnvio + " " + String.valueOf(env) + "€";
        descripcionPDF = descripcionPDF + "\n" + sFacImp + " " + String.valueOf(imp) + "€";
        descripcionPDF = descripcionPDF + "\n--------------------------------------------------------------------------";
        descripcionPDF = descripcionPDF + "\nTotal: " + String.valueOf(tot) + "€";
    }

// _________________________________________________________________________________________________

/*  Método comprobarPermiso:
    ------------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                (boolean) permisoEsc: Contiene el resultado a la petición de escritura sobre el
                almacenamiento externo del dispositivo
                (boolean) permisoLec: Contiene el resultado a la petición de lectura sobre el
                almacenamiento externo del dispositivo
        *) Descripción:
                Este método comprueba los permisos de letura y escritura sobre el almacenamiento
                externo del dispositivo.
*/
    private boolean comprobarPermiso(){
        // Comprobar que la aplicación tenga los permisos necesarios para crear la factura
        int permisoEsc = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permisoLec = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permisoEsc == PackageManager.PERMISSION_GRANTED && permisoLec == PackageManager.PERMISSION_GRANTED;
    }

// _________________________________________________________________________________________________

/*  Método pedirPermisos:
    ------------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método pide los permisos de letura y escritura sobre el almacenamiento externo
                del dispositivo.
*/
    public void pedirPermisos(){
        ActivityCompat.requestPermissions(getActivity(),new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE},200);
    }

// _________________________________________________________________________________________________

/*  Método cargar_configuracion:
    ----------------------------
        *) Parámetros (Input):
                (View) view: Vista asociada al fragmento.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método carga las preferencias configuradas por el usuario (modo oscuro,
                orientación de la pantalla...).
*/
    private void cargar_configuracion(View view){

        // Obtener las preferencias configuradas por el usuario
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        // Comprobar el estado de la preferencia del modo oscuro
        boolean modoOscuro = sp.getBoolean("modo_oscuro", false);

        if(modoOscuro){        // Si el modo oscuro está activado: Pintar el fondo de negro
            view.setBackgroundColor(getResources().getColor(R.color.black));
        } else{                // Si el modo oscuro está desactivado: Pintar el fondo de blanco
            view.setBackgroundColor(getResources().getColor(R.color.white));
        }

        // Comprobar el estado de la preferencia de la orientación
        String ori = sp.getString("orientacion","false");
        switch (ori) {
            case "1":        // Si la orientación es 1: Desbloquear el giro automático de la app
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                break;
            case "2":        // Si la orientación es 2: Bloquear la orientacion vertical
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case "3":        // Si la orientación es 3: Bloquear la orientacion horizontal
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }



// _________________________________________________________________________________________________

/*  Método onDestroyView:
    ---------------------
        *) Parámetos (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta cuando se cierra el fragmento y borra la vista
*/
    @Override
    public void onDestroyView() {
        // Borrar la vista
        super.onDestroyView();
    }

// _________________________________________________________________________________________________

    /*  Método notificarBorrado:
        ------------------------
            *) Parámetos (Input):
                    (int) pPos: Índice ddel elemento que se desea eliminar.
            *) Parámetro (Output):
                    void
            *) Descripción:
                    Este método se ejecuta cuando el usuario desea eliminar un producto de la cesta.
                    Crea un diálogo para confirmar la decisión.
                        - Si pulsa el botón "Seguro": Elimina el producto de la cesta
                        - Si pulsa el botón "Me arreptiento": No hace nada
*/
    @Override
    public void notificarBorrado(int pPos) {
        // Crear Diálogo de confirmación
        AlertDialog.Builder confirmacion = new AlertDialog.Builder(getActivity());
        String dEspera = getResources().getString(R.string.d_espera);
        String dSeguroElimin = getResources().getString(R.string.d_seguroEliminado);
        confirmacion.setTitle(dEspera);      // Asignar el título
        confirmacion.setMessage(dSeguroElimin);    // Asignar el contenido
        confirmacion.setCancelable(false);  // Indicar que no se pueda cancelar

        // Añadir y gestionar el botón "Si"
        String dSeguro = getResources().getString(R.string.d_seguro);
        confirmacion.setPositiveButton(dSeguro, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                // Eliminar el producto de la cesta y de la BBDD
                adapterOrdenes.eliminar(pPos);

                // Notificar que se ha confirmado la compra
                String msg = getResources().getString(R.string.t_pedidoEliminado);
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();

            }
        });

        // Añadir y gestionar el botón "Me arrepiento"
        String dMeArrepiento = getResources().getString(R.string.d_meArrepiento);
        confirmacion.setNegativeButton(dMeArrepiento, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {}});       // No hace nada

        // Crear y mostrar el diálogo
        confirmacion.create().show();
    }

// _________________________________________________________________________________________________

/*  Método notificarCambios:
    ------------------------
        *) Parámetos (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este es un método abstracto de la interfaz InterfazActualizarCesta. Es llamado desde
                ListAdapter_Ordenes y se utiliza para actualizar el apartado de costes.
*/
    @Override
    public void notificarCambios() {
        // Actualizar los costes (Total producto, envio, impuestos y total)
        cargarResumen(getView());
    }

// _________________________________________________________________________________________________

/*  Método onSaveInstanceState:
    ------------------------
        *) Parámetros (Input):
                1) (Bundle) outState: Contiene el diseño predeterminado del Fragment.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta antes de eliminar la actividad. Guarda el idioma actual en el
                Bundle, para que al refrescar la actividad se mantenga.
*/
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Guardar en el Bundle el idioma actual de la aplicación
        outState.putString("idioma",idioma);
    }


//-----------------------------------------------------------------------------------------------------------------------------------
    private Data guardarData(String titulo, String contenido, int id){
        return new Data.Builder()
                .putString("titulo", titulo)
                .putString("contenido",contenido)
                .putInt("idnoti",id).build();

    }


    private void obtenerOrdenes(String pUrl){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, pUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    ArrayList<Orden> lista_ordenesAux = new ArrayList<>();
                    if(json.get("exist").toString().equals("true")){
                        JSONArray listaJSON = (JSONArray) json.get("orders");

                        for (int i = 0; i < listaJSON.length(); i++){
                            JSONObject aux = (JSONObject) listaJSON.get(i);

                            Orden orden = new Orden();                  // Crear una Órden
                            orden.setId(aux.getInt("id"));                // Asignar id
                            orden.setNombreProd(aux.getString("nombreProd"));     // Asignar nombre
                            orden.setPrecioProd(aux.getDouble("precioProd"));     // Asignar precio
                            orden.setImagenProd(aux.getInt("imagenProd"));        // Asignar foto
                            orden.setCantidadProd(aux.getInt("cantidadProd"));      // Asignar cantidad
                            orden.setEmailUsuario(aux.getString("emailUsuario"));   // Asignar email del usuario

                            lista_ordenesAux.add(orden);       // Añadir la nueva orden al listado
                        }

                    }
                    lista_ordenes = lista_ordenesAux;
                    // Cargar las lista en el RecyclerView
                    recyclerViewOrdenes = v.findViewById(R.id.lista_carrito);
                    recyclerViewOrdenes.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                    adapterOrdenes = new ListAdapter_Ordenes(lista_ordenes,getContext(),datosUser,cf);
                    recyclerViewOrdenes.setAdapter(adapterOrdenes);
                    // Actualizar los costes (Total producto, envio, impuestos y total)
                    cargarResumen(v);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Imprimir estado del registro
                String msg = getResources().getString(R.string.t_errorBBDD);
                Toast.makeText(getContext(),msg, Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("operacion", "obtener");
                parametros.put("email", datosUser[2]);
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void eliminarOrdenes(String pUrl){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, pUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    if(json.get("done").toString().equals("true")){
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
                Toast.makeText(getContext(),msg, Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("operacion", "eliminar");
                parametros.put("email", datosUser[2]);
                parametros.put("nombre", nombreProd);
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }
}