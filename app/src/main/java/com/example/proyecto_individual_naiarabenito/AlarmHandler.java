package com.example.proyecto_individual_naiarabenito;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

public class AlarmHandler {
    private final Context context;

    public AlarmHandler(Context context){
        this.context = context;
    }

    public void setAlarmManager() {
        Intent i = new Intent(context, WidgetService.class);
        PendingIntent sender;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sender = PendingIntent.getBroadcast(context, 2, i, PendingIntent.FLAG_IMMUTABLE);
        } else {
            sender = PendingIntent.getBroadcast(context, 2, i,0);
        }
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Obtener la hora actual y sumarle 5s
        Calendar c = Calendar.getInstance();
        long l = c.getTimeInMillis() + 5000;

        // Activar la alarma para dentro de 60s
        if(am != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,l,sender);
            } else {
                am.set(AlarmManager.RTC_WAKEUP,l,sender);
            }

        }
    }

    public void cancelAlarmManager(){
        Intent i = new Intent(context, WidgetService.class);
        PendingIntent sender;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sender = PendingIntent.getBroadcast(context, 2, i, PendingIntent.FLAG_IMMUTABLE);
        } else {
            sender = PendingIntent.getBroadcast(context, 2, i,0);
        }
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if(am != null){
            am.cancel(sender);
        }
    }
}
