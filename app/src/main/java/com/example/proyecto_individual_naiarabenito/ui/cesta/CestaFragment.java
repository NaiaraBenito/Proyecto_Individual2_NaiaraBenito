
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.ui.cesta;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextPaint;
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
import com.example.proyecto_individual_naiarabenito.Menu_Principal;
import com.example.proyecto_individual_naiarabenito.R;
import com.example.proyecto_individual_naiarabenito.db.DBHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


/* ####################################### CLASE CESTA_FRAGMENT ####################################
    *) Descripción:
        La función de esta clase es mostrar y gestionar la configuración del carrito.

    *) Tipo: Fragment
*/
public class CestaFragment extends Fragment implements ListAdapter_Ordenes.ListenerCesta {

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

    private String tituloPDF;       // Variable que contiene el título de la factura que se genera al finalizar el pedido
    private String descripcionPDF;  // Variable que contiene el cuerpo de la factura

    private PendingIntent pendingIntent;        // Variable que gestiona la notificación de compra
    private PendingIntent siPendingIntent;      // Variable que gestiona la notificación de compra
    private PendingIntent noPendingIntent;      // Variable que gestiona la notificación de compra
    final static String CHANNEL_ID = "NOTIFICACION";    // Variable que contiene el id del canal para la notificación
    final static int NOTIFICACION_ID = 0;       // Variable que contiene el id de la notificación

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

        // Actualizar los costes (Total producto, envio, impuestos y total)
        cargarResumen(view);

        // Obtener los Objetos de la vista
        pagar = view.findViewById(R.id.btn_pagar_carrito);

        // Gestionar el proceso de compra del usuario y asignar acción al pulsar el botón "PAGAR"
        pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Comprobar que la lista de pedidos no esté vacía
                if(lista_ordenes.isEmpty()){    // Si está vacía: Imprimir mensaje de error
                    Toast.makeText(getContext(), "No tienes productos en la cesta", Toast.LENGTH_LONG).show();
                } else{     // Si no está vacía: Sacar diálogo de confirmación
                    // Gestionar el diálogo de confirmación del pedido
                    gestionarDialogo(view);
                }
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

        // Añadir los pedidos del usuario a la lista
        DBHelper dbHelper = new DBHelper(getContext());
        lista_ordenes = dbHelper.getOrdenes(datosUser[2]);

        // Cargar las lista en el RecyclerView
        recyclerViewOrdenes = view.findViewById(R.id.lista_carrito);
        recyclerViewOrdenes.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        adapterOrdenes = new ListAdapter_Ordenes(lista_ordenes,getContext(),datosUser);
        recyclerViewOrdenes.setAdapter(adapterOrdenes);
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
                (double) auxImp: Contiene el coste de los impuestos del pedido .
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
        confirmacion.setTitle("Confirmar pedido");      // Asignar el título
        confirmacion.setMessage("¿Seguro que has terminado tu pedido?");    // Asignar el contenido
        confirmacion.setCancelable(false);  // Indicar que no se pueda cancelar

        // Añadir y gestionar el botón "Si"
        confirmacion.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                // Notificar que se ha confirmado la compra
                Toast.makeText(getContext(), "Compra confirmada", Toast.LENGTH_LONG).show();

                // Crear y gestionar la notificación de agradecimiento
                gestionarNotificacion();
            }
        });

        // Añadir y gestionar el botón "No"
        confirmacion.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // Notificar que se ha rechazado la compra
                Toast.makeText(getContext(), "Compra rechazada", Toast.LENGTH_LONG).show();
            }
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
                además de gestionar su funcionamiento.
*/
    private void gestionarNotificacion(){

        // Crear un canal para una notificación
        crearNotificationChannel();

        // Crear la notificación que agradece la compra
        crearNotificacion();

        // Comprobar que la aplicación tenga permisos para utilizar notificaciones
        if(!comprobarPermiso()){        // Si no tiene permiso: Pide los permisos
            pedirPermisos();
        }

        // Crear la factura de la compra realizada por el usuario
        crearPDF();

        // Eliminar los productos comprados de la BBDD
        DBHelper dbHelper = new DBHelper(getContext());

        for(int i = 0; i < lista_ordenes.size(); i++){  // Por cada producto

            // Obtener la Orden
            Orden o = lista_ordenes.get(i);

            // Eliminar la orden del la BBDD
            dbHelper.borrarOrden(o.getNombreProd(), datosUser[2]);
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
                Este método crea una notificación para agradecerle al usuario el pedido.
*/
    private void crearNotificacion() {

        // Gestionar los estados de la notificación
        setPendingIntent();     // Estado de espera
        setSiPendingIntent();   // Estado al pulsar Si
        setNoPendingIntent();   // Estado al pulsar No

        // Crear la notificación
        NotificationCompat.Builder notif = new NotificationCompat.Builder(getActivity().getApplicationContext(), CHANNEL_ID);
        notif.setSmallIcon(R.drawable.icon_notif);      // Asignar icono
        notif.setContentTitle("Compra finalizada");     // Asignar título
        notif.setContentText("Gracias por pedir en  ¡Dale un Mordisco!\nNos vemos pronto"); // Asignar contenido
        notif.setColor(Color.rgb(239, 70, 240));     // Asignar color
        notif.setPriority(NotificationCompat.PRIORITY_DEFAULT);     // Asignar prioridad
        notif.setLights(Color.rgb(239, 70, 240), 1000, 1000);   // Asignar luces
        notif.setVibrate(new long[]{1000, 1000, 1000});     // Asignar vibración
        notif.setDefaults(Notification.DEFAULT_SOUND);      // Asignar sonido

        // Indicar que tiene un PendingIntent (acción al tocar)
        notif.setContentIntent(pendingIntent);

        // Asignar botones con acciones: Elegir icono + texto que aparece + pendingIntent
        notif.addAction(R.drawable.icon_notif, "Empezar", siPendingIntent);
        notif.addAction(R.drawable.icon_notif, "Finalizar", noPendingIntent);

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
                Este método gestiona el estado de espera de la notificación. Mientras espera a que
                el usuario responda, lo redirige al Menú Principal.
*/
    private void setPendingIntent(){

        // Crear el intent que redirige la ejecución al Menú Principal
        Intent i = new Intent(getContext(), Menu_Principal.class);

        // Redirigir al usuario al Menú Principal en el caso de que pulse "Back"
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        stackBuilder.addParentStack(Menu_Principal.class);
        stackBuilder.addNextIntent(i);

        // Asignar configuración al estado de espera
        pendingIntent = stackBuilder.getPendingIntent(1,PendingIntent.FLAG_IMMUTABLE);
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

        // Crear el intent que redirige la ejecución al SiActNotificacion
        Intent i = new Intent(getContext(), SiActNotificacion.class);

        // Redirigir al usuario al Menú Principal en el caso de que pulse "Back"
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        //stackBuilder.addParentStack(SiActNotificacion.class);
        stackBuilder.addParentStack(getActivity());
        stackBuilder.addNextIntent(i);

        // Asignar configuración al estado de confirmación
        siPendingIntent = stackBuilder.getPendingIntent(1,PendingIntent.FLAG_IMMUTABLE);

        // Configurar la desaparición de la notificación
        NotificationManagerCompat nmc = NotificationManagerCompat.from(getContext().getApplicationContext());

        // Cancelar la notificación con el NOTIFICATION_ID
        nmc.cancel(NOTIFICACION_ID);
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

        // Redirigir al usuario al Menú Principal en el caso de que pulse "Back"
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        stackBuilder.addParentStack(getActivity());
        stackBuilder.addNextIntent(i);

        // Asignar configuración al estado de rechazo
        noPendingIntent = stackBuilder.getPendingIntent(1,PendingIntent.FLAG_IMMUTABLE);

        // Configurar la desaparición de la notificación
        NotificationManagerCompat nmc = NotificationManagerCompat.from(getContext().getApplicationContext());

        // Cancelar la notificación con el NOTIFICATION_ID
        nmc.cancel(NOTIFICACION_ID);
    }

// _________________________________________________________________________________________________

/*  Método crearPDF:
    ----------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método crea la factura de la compra realizada por el usuario en formato PDF.
*/
    private void crearPDF(){

        // Asignar el título de la factura
        tituloPDF = "Factura de " + datosUser[0] + " " + datosUser[1];

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
            descripcionPDF = descripcionPDF + "Producto: " + nombre + "   Cantidad: " + String.valueOf(cantidad) + "  Precio: " + String.valueOf(precioTot) + "€\n";
        }

        // Calcular resúmen del pedido
        double pTotProd = calcularPrecioTotProductos(); // Costes en productos
        double imp = calcularImpuestos(pTotProd);       // Impuestos
        double env = calcularEnvio(pTotProd);           // Costes de envío
        double tot = calcularTotal(pTotProd,imp,env);   // Total del pedido

        // Añadir información al contenido de la factura
        descripcionPDF = descripcionPDF + "\n\nTotal en productos: " + String.valueOf(pTotProd) + "€";
        descripcionPDF = descripcionPDF + "\nCostes de envío: " + String.valueOf(env) + "€";
        descripcionPDF = descripcionPDF + "\nImpuestos: " + String.valueOf(imp) + "€";
        descripcionPDF = descripcionPDF + "\n--------------------------------------------------------------------------";
        descripcionPDF = descripcionPDF + "\nTotal: " + String.valueOf(tot) + "€";

        // Crear PDF
        PdfDocument pdfDocument = new PdfDocument();

        // Crear herramienta para editar el documento
        Paint paint = new Paint();

        // Escribir en el documento
        TextPaint titulo = new TextPaint();
        TextPaint descripcion = new TextPaint();

        // Obtener Bitmaps para dibujar el logo en la factura
        Bitmap bitmap, bitmapEscala;

        // Añadir especificaciones del documento (dimensiones de la página)
        PdfDocument.PageInfo paginaInfo = new PdfDocument.PageInfo.Builder(816,1054,1).create();
        PdfDocument.Page pagina = pdfDocument.startPage(paginaInfo);

        // Obtener página
        Canvas canvas = pagina.getCanvas();

        // Escalar el logo
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        bitmapEscala = Bitmap.createScaledBitmap(bitmap, 250, 250, false);

        // Pintar el logo en el documento
        canvas.drawBitmap(bitmapEscala, 500,15,paint);

        // Escribir el título en el documento
        titulo.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        titulo.setTextSize(25);
        canvas.drawText(tituloPDF, 10, 180, titulo);

        // Escribir el contenido en el documento
        descripcion.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        descripcion.setTextSize(14);
        String[] arrDescripcion = descripcionPDF.split("\n");

        int y = 237;    // Coordenada Y en la que comenzar a escribir el texto
        for(int i = 0; i<arrDescripcion.length; i++){
            canvas.drawText(arrDescripcion[i],10,y,descripcion);
            y +=  15;
        }

        // Finalizar la página
        pdfDocument.finishPage(pagina);

        // Exportar el documento PDF de la factura (sdcard/Factura_[nombre]_[apellido].pdf)
        File file = new File(Environment.getExternalStorageDirectory(),"Factura_" + datosUser[0] +"_"+ datosUser[1] +".pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        }catch (Exception e){
            e.printStackTrace();
        }

        // Cerrar documento
        pdfDocument.close();
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

/*  Método onRequestPermissionResult:
    ---------------------------------
        *) Parámetos (Input):
                (int) requestCode: Contiene el código de la petición (identificador).
                (String[]) permissions: Contiene la lista de los permisos que se piden.
                (int[]) grantResults: Contiene el resultado de las peticiones.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta cuando se ha realizado la petición de permisos de la
                aplicación.
*/
    @Override
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        // Comprobar que el código de la petición de los permisos sea el de lectura y escritura
        if(requestCode == 200){
            // Comprobar que se ha recibido la respuesta a las 2 peticiones
            if (grantResults.length >0){
                // Comprobar si se han concedido los permisos
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if(writeStorage && readStorage){    // Si se han concedido ambos permisos
                    // Informar de que se han concedido los permisos
                    Toast.makeText(getContext(),"Permiso concedido",Toast.LENGTH_LONG).show();
                } else {    // Si se ha rechazado alguno de los permisos
                    // Informar de que se han denegado los permisos
                    Toast.makeText(getContext(),"Permiso denegado",Toast.LENGTH_LONG).show();
                }
            }
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

//---------------------------------------
    @Override
    public void actualizarDatos() {
        cargarResumen(this.getView());
    }
/*
    @Override
    public FragmentManager obtenerDialogo() {
        return getActivity().getSupportFragmentManager();
    }*/
}