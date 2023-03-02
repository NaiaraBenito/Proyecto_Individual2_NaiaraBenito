package com.example.proyecto_individual_naiarabenito.ui.cesta;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_individual_naiarabenito.Menu_Principal;
import com.example.proyecto_individual_naiarabenito.R;
import com.example.proyecto_individual_naiarabenito.db.DBHelper;

import java.util.List;

public class CestaFragment extends Fragment implements ListAdapter_Ordenes.ListenerCesta {

    String[] datosUser;

    // Variables para mostrar la lista de productos
    List<Orden> lista_ordenes;
    RecyclerView recyclerViewOrdenes;
    ListAdapter_Ordenes adapterOrdenes;

    TextView precioProd;
    TextView envio;
    TextView impuestos;
    TextView total;
    Button pagar;

    // Gestores de lo que sucede al tocar la notificación
    private PendingIntent pendingIntent;
    private PendingIntent siPendingIntent;
    private PendingIntent noPendingIntent;
    final static String CHANNEL_ID = "NOTIFICACION";
    final static int NOTIFICACION_ID = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cesta, container, false);

        // Recibir datos del usuario
        datosUser = new String[3];
        datosUser[0] = getActivity().getIntent().getExtras().getString("nombreUsuario");
        datosUser[1] = getActivity().getIntent().getExtras().getString("apellidoUsuario");
        datosUser[2] = getActivity().getIntent().getExtras().getString("emailUsuario");

        cargarListaOrdenes(view);
        cargarResumen(view);

        // Gestión del diálogo tras pulsar el botón de pagar
        pagar = (Button) view.findViewById(R.id.btn_pagar_carrito);
        pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmacion = new AlertDialog.Builder(view.getContext());
                confirmacion.setTitle("Confirmar pedido");
                confirmacion.setMessage("¿Seguro que has terminado tu pedido?");

                confirmacion.setCancelable(false);
                confirmacion.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), "Compra confirmada", Toast.LENGTH_LONG).show();crearNotificationChannel();
                        crearNotificacion();
                    }
                });

                confirmacion.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), "Compra rechazada", Toast.LENGTH_LONG).show();
                    }
                });
                confirmacion.create().show();
            }
        });


        return view;
    }

    public void crearNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Notificacion";
            NotificationChannel nc = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager nm = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
            nm.createNotificationChannel(nc);
        }
    }
    public void crearNotificacion() {
        setPendingIntent();
        setSiPendingIntent();
        setNoPendingIntent();
        NotificationCompat.Builder notif = new NotificationCompat.Builder(getActivity().getApplicationContext(), CHANNEL_ID);
        notif.setSmallIcon(R.drawable.icon_notif);
        notif.setContentTitle("Compra finalizada");
        notif.setContentText("Gracias por pedir en  ¡Dale un Mordisco!\nNos vemos pronto");
        notif.setColor(Color.rgb(239, 70, 240));
        notif.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notif.setLights(Color.rgb(239, 70, 240), 1000, 1000);
        notif.setVibrate(new long[]{1000, 1000, 1000});
        notif.setDefaults(Notification.DEFAULT_SOUND);

        // Indicar que tiene un PendingIntent (acción al tocar)
        notif.setContentIntent(pendingIntent);
        // VARIANTE: Al tocar los botones/opciones de la notificación
        //Elegir icono + texto que aparece + pendingIntent
        notif.addAction(R.drawable.icon_notif, "Empezar", siPendingIntent);
        notif.addAction(R.drawable.icon_notif, "Finalizar", noPendingIntent);

        NotificationManagerCompat nmc = NotificationManagerCompat.from(getActivity().getApplicationContext());
        /* 4) Solicitar permisos, necesario apartir de Android v13 */
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            //PEDIR EL PERMISO
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 11);
        }
        nmc.notify(NOTIFICACION_ID, notif.build());
    }

    // Metodo para que la notificación te lleve a alguna actividad
    private void setPendingIntent(){
        // Le decimos de dónde a dónde vamos
        Intent i = new Intent(getContext(), Menu_Principal.class);
        // Para cuando el usuario pulse back, te lleva al MainActivity (opcional)
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
        stackBuilder.addParentStack(Menu_Principal.class);
        stackBuilder.addNextIntent(i);
        // Esto no funciona si no se lo pones en el manifest
        pendingIntent = stackBuilder.getPendingIntent(1,PendingIntent.FLAG_IMMUTABLE);
    }


    private void setSiPendingIntent(){
        // Le decimos de dónde a dónde vamos
        Intent i = new Intent(getContext(), SiActNotificacion.class);
        // Para cuando el usuario pulse back, te lleva al MainActivity (opcional)
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
        //stackBuilder.addParentStack(SiActNotificacion.class);
        stackBuilder.addParentStack(getActivity());
        stackBuilder.addNextIntent(i);
        // Esto no funciona si no se lo pones en el manifest
        siPendingIntent = stackBuilder.getPendingIntent(1,PendingIntent.FLAG_IMMUTABLE);

        // Para hacer que la notificación desaparezca al ejecutar la actividad
        // Crear un manager de notificaciones
        NotificationManagerCompat nmc = NotificationManagerCompat.from(getContext().getApplicationContext());
        // Cancelar la notificación, pasándole el NOTIFICATION_ID
        nmc.cancel(NOTIFICACION_ID);
    }

    private void setNoPendingIntent(){
        // Le decimos de dónde a dónde vamos
        Intent i = new Intent(getContext(), NoActNotificacion.class);
        // Para cuando el usuario pulse back, te lleva al MainActivity (opcional)
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
        stackBuilder.addParentStack(getActivity());
        stackBuilder.addNextIntent(i);

        // Esto no funciona si no se lo pones en el manifest
        noPendingIntent = stackBuilder.getPendingIntent(1,PendingIntent.FLAG_IMMUTABLE);
        // Para hacer que la notificación desaparezca al ejecutar la actividad
        // Crear un manager de notificaciones
        NotificationManagerCompat nmc = NotificationManagerCompat.from(getContext().getApplicationContext());
        // Cancelar la notificación, pasándole el NOTIFICATION_ID
        nmc.cancel(NOTIFICACION_ID);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

    private void cargarListaOrdenes(View view) {

        // Añadir valores a la lista
        DBHelper dbHelper = new DBHelper(getContext());
        lista_ordenes = dbHelper.getOrdenes(datosUser[2]);

        // Mostrar los valores
        recyclerViewOrdenes = view.findViewById(R.id.lista_carrito);
        recyclerViewOrdenes.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        adapterOrdenes = new ListAdapter_Ordenes(lista_ordenes,getContext(),datosUser);
        recyclerViewOrdenes.setAdapter(adapterOrdenes);
    }

    private void cargarResumen(View view) {

        double precioTotProd = calcularPrecioTotProductos(view);
        double envio = calcularEnvio(view, precioTotProd);
        double impuestos = calcularImpuestos(view, precioTotProd);
        calcularTotal(view, precioTotProd, impuestos, envio);
    }

    public double calcularPrecioTotProductos(View view){
        precioProd = (TextView) view.findViewById(R.id.total_prod_carrito);

        double total = 0.00;
        for(int i = 0; i < adapterOrdenes.getItemCount(); i++){
            double auxPrecio = (double) Math.round(adapterOrdenes.getListaOrden().get(i).getCantidadProd() * adapterOrdenes.getListaOrden().get(i).getPrecioProd() * 100.0) / 100.0;
            total = Math.round((total + auxPrecio) * 100.0) / 100.0;
        }
        precioProd.setText(String.valueOf(total) + "€");
        return total;
    }

    public double calcularImpuestos(View view, double precio){
        impuestos = (TextView) view.findViewById(R.id.total_impuesto_carrito);
        double auxImp = 0;
        auxImp = Math.round(((precio * 0.1) * 100.0))/100.0;
        //auxImp = precio * 0.1;
        impuestos.setText(String.valueOf(auxImp) + "€");
        return auxImp;
    }

    public double calcularEnvio(View view, double precio){
        envio = (TextView) view.findViewById(R.id.total_envio_carrito);

        double auxenvio = 0.00;

        if (precio > 0){
            auxenvio = 3.00;
        }
        envio.setText(String.valueOf(auxenvio) + "€");
        return auxenvio;
    }
    public void calcularTotal(View view, double precioProd, double impuestos, double envio){
        total = (TextView) view.findViewById(R.id.total_carrito);
        double auxTotal = 0;
        auxTotal = Math.round((precioProd + impuestos + envio) * 100.0) / 100.0;

        total.setText(String.valueOf(auxTotal) + "€");
    }

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