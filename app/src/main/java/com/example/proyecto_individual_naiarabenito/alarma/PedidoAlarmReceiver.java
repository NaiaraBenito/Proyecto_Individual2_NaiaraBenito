
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.alarma;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.proyecto_individual_naiarabenito.R;


/* ################################### CLASE PEDIDO ALARM RECEIVER #################################
    *) Descripción:
        La función de esta clase es recibir la alarma lanzada para informar al usuario de que su
        pedido está listo mostrar una notificación.

    *) Tipo: BroadcastReceiver
*/
public class PedidoAlarmReceiver extends BroadcastReceiver {

// ___________________________________________ Variables ___________________________________________
    final static String CHANNEL_ID = "Pedido";  // Id del canal de la notificación

// ____________________________________________ Métodos ____________________________________________

/*  Método onReceive:
    -----------------
        *) Parámetros (Input):
                1) (Context) context: Contiene el contexto en el que se ejecuta el receptor.
                2) (Intent) intent: El Intent que se está recibiendo.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este metodo se ejecuta 10s después de haber terminado la compra. Se encarga de crear
                la notificación que informa al usuario que su pedido está listo.
*/
    @Override
    public void onReceive(Context context, Intent intent) {
        // Crear la notificación
        String notifTitulo = context.getApplicationContext().getResources().getString(R.string.n_PNotifTitulo);
        String notifContenido = context.getApplicationContext().getResources().getString(R.string.n_PNotifContenido);

        NotificationCompat.Builder notif = new NotificationCompat.Builder(context, CHANNEL_ID);
        notif.setSmallIcon(R.drawable.icon_notif);      // Asignar icono
        notif.setContentTitle(notifTitulo);                      // Asignar título
        notif.setContentText(notifContenido);                          // Asignar contenido
        notif.setColor(Color.rgb(239, 70, 240));     // Asignar color
        notif.setPriority(NotificationCompat.PRIORITY_DEFAULT);     // Asignar prioridad
        notif.setLights(Color.rgb(239, 70, 240), 1000, 1000);   // Asignar luces
        notif.setVibrate(new long[]{1000, 1000, 1000});     // Asignar vibración
        notif.setDefaults(Notification.DEFAULT_SOUND);      // Asignar sonido

        // Comprobar que se tengan los permisos necesarios
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // Imprimir notificación
        NotificationManagerCompat nmc = NotificationManagerCompat.from(context);
        nmc.notify(200, notif.build());
    }
}