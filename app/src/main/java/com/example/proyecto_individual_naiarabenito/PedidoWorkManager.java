package com.example.proyecto_individual_naiarabenito;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public class PedidoWorkManager extends Worker {
    final static String CHANNEL_ID = "NOTIFICACION";

    public PedidoWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public static void NotificarPedido (Long duracion, Data data, String tag){
        OneTimeWorkRequest noti = new OneTimeWorkRequest.Builder(PedidoWorkManager.class)
                .setInitialDelay(duracion, TimeUnit.MILLISECONDS).addTag(tag)
                .setInputData(data).build();

        WorkManager instance = WorkManager.getInstance();
        instance.enqueue(noti);
    }

    @NonNull
    @Override
    public Result doWork() {
        String titulo = getInputData().getString("titulo");
        String contenido = getInputData().getString("contenido");
        int id = (int) getInputData().getLong("idnoti",0);
        notificarPedido(titulo,contenido,id);
        return Result.success();
    }

    @SuppressLint("MissingPermission")
    private void notificarPedido(String titulo, String contenido, int id){
        Log.d("PEDIDO", "Titulo: " + titulo + " y Detalle: " + contenido);

        // Crear la notificación
        NotificationCompat.Builder notif = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        notif.setSmallIcon(R.drawable.icon_notif);      // Asignar icono

        String notifTitulo = titulo;
        String notifContenido = contenido;
        notif.setContentTitle(notifTitulo);                      // Asignar título
        notif.setContentText(notifContenido);                          // Asignar contenido
        notif.setColor(Color.rgb(239, 70, 240));     // Asignar color
        notif.setPriority(NotificationCompat.PRIORITY_DEFAULT);     // Asignar prioridad
        notif.setLights(Color.rgb(239, 70, 240), 1000, 1000);   // Asignar luces
        notif.setVibrate(new long[]{1000, 1000, 1000});     // Asignar vibración
        notif.setDefaults(Notification.DEFAULT_SOUND);      // Asignar sonido


        //Solicitar permiso para crear una notificación
        /*if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) { // Si no tiene permiso
            // Pedir el permiso
            ActivityCompat.requestPermissions(g, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 11);
        }
*/
        // Imprimir notificación
        NotificationManagerCompat nmc = NotificationManagerCompat.from(getApplicationContext());
        nmc.notify(id, notif.build());
    }
}
