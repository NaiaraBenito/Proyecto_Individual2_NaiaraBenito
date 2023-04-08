
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.widget;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import java.util.Calendar;


/* ##################################### CLASE ALARM HANDLER #######################################
    *) Descripción:
        La función de esta clase es activar o desactivar la alarma que actualiza el Widget.

    *) Tipo: Class
*/
public class AlarmHandler {

// ___________________________________________ Variables ___________________________________________
    private final Context context;  // Variable que contiene el contexto de la actividad que ha llamado a esta clase

// __________________________________________ Constructor __________________________________________
    public AlarmHandler(Context context){
        this.context = context;
    }

// ____________________________________________ Métodos ____________________________________________

/*  Método setAlarmManager:
    -----------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método setea la alarma del Widget con una duracion de 5s.
*/
    public void setAlarmManager() {
        // Crear un intent que ejecute la clase WidgetService
        Intent i = new Intent(context, WidgetService.class);

        // Crear un pending intent para que se quede a la espera de que pase el tiempo
        PendingIntent sender;
        // Comprobar que la versión de android del dispositivo sea igual o superior a Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Crear el remitente de la alarma
            sender = PendingIntent.getBroadcast(context, 2, i, PendingIntent.FLAG_IMMUTABLE);
        } else {
            // Crear el remitente de la alarma
            sender = PendingIntent.getBroadcast(context, 2, i,0);
        }

        // Obtener el AlarmManager del contexto
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Obtener la hora actual y sumarle 5s
        Calendar c = Calendar.getInstance();
        long l = c.getTimeInMillis() + 5000;

        // Activar la alarma para dentro de 5s
        if(am != null){
            // Comprobar que la versión de android del dispositivo sea igual o superior a Marshmallow
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Iniciar la alarma
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,l,sender);
            } else {
                // Iniciar la alarma
                am.set(AlarmManager.RTC_WAKEUP,l,sender);
            }

        }
    }

// ____________________________________________ Métodos ____________________________________________

    /*  Método cancelAlarmManager:
        --------------------------
            *) Parámetros (Input):
            *) Parámetro (Output):
                    void
            *) Descripción:
                    Este método cancela la alarma del Widget.
    */
    public void cancelAlarmManager(){

        // Crear un intent que ejecute la clase WidgetService
        Intent i = new Intent(context, WidgetService.class);

        // Crear un pending intent para que se quede a la espera de que pase el tiempo
        PendingIntent sender;

        // Comprobar que la versión de android del dispositivo sea igual o superior a Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Crear el remitente de la alarma
            sender = PendingIntent.getBroadcast(context, 2, i, PendingIntent.FLAG_IMMUTABLE);
        } else {
            // Crear el remitente de la alarma
            sender = PendingIntent.getBroadcast(context, 2, i,0);
        }

        // Obtener el AlarmManager del contexto
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Si existe un AlarmManager
        if(am != null){
            // Cancelar la alarma
            am.cancel(sender);
        }
    }
}
