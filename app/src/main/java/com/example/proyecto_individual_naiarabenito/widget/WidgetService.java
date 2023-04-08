package com.example.proyecto_individual_naiarabenito.widget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;


public class WidgetService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Despertar el dispositivo
        WakeLocker.acquire(context);

        // Actualizar el número en el Widget
        cuentaAtras(context);

        // Forzar la actualización del Widget
        Intent i = new Intent(context,WidgetAplicacion.class);
        i.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, WidgetAplicacion.class));
        i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        context.sendBroadcast(i);

        Log.d("WIDGET","Widget set to update!");

        // Volver a dormir
        WakeLocker.release();
    }

    private void cuentaAtras(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("PREFS",0);
        int dias = preferences.getInt("dias",0);
        int horas = preferences.getInt("horas",0);
        int minutos = preferences.getInt("minutos",3);
        int segundos = preferences.getInt("segundos",0);

        if(segundos <= 0){
            segundos = 55;  //La cuenta atrás se actualiza cada 5s
            if(minutos <= 0){
                minutos = 59;
                if(horas <= 0){
                    horas = 23;
                    if(dias <= 0){
                        dias = 6;
                    } else{
                        dias--;
                    }
                } else{
                    horas--;
                }
            } else{
                minutos--;
            }
        } else{
            segundos-=5;
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("dias",dias);
        editor.putInt("horas",horas);
        editor.putInt("minutos",minutos);
        editor.putInt("segundos",segundos);
        editor.apply();
    }
}
