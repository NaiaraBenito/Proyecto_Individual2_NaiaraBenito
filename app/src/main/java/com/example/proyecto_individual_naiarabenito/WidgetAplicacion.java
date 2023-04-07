package com.example.proyecto_individual_naiarabenito;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetAplicacion  extends AppWidgetProvider {

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        Intent i = new Intent(context, Launch_Screen.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i,0);

        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_diseno);

        // Obtener el valor del Widget
        SharedPreferences preferences = context.getSharedPreferences("PREFS",0);
        int dias = preferences.getInt("dias",0);
        int horas = preferences.getInt("horas",0);
        int minutos = preferences.getInt("minutos",3);
        int segundos = preferences.getInt("segundos",0);

        views.setOnClickPendingIntent(R.id.w_widgetImage,pendingIntent);

        // Asignar el valor al Widget
        views.setTextViewText(R.id.w_widgetText, "FIN DE LA PROMOCIÓN:\n" + dias + "d "+ horas + "h " + minutos + "m " + segundos + "s");

        // Actualizar el Widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

        // Reiniciar el refresco del Widget
        AlarmHandler alarmHandler = new AlarmHandler(context);
        alarmHandler.cancelAlarmManager();
        alarmHandler.setAlarmManager();

        Log.d("WIDGET","Widget updated!");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int appWidgetId: appWidgetIds){
            /*Intent i = new Intent(context,Login.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i,0);
            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_diseno);
            views.setOnClickPendingIntent(R.id.w_widgetImage,pendingIntent);
            //views.setOnClickPendingIntent(R.id.w_widgetText,pendingIntent);*/

            updateAppWidget(context,appWidgetManager,appWidgetId);
        }
    }

    @Override
    public void onDisabled(Context context) {
        // Detener la actualización del Widget
        AlarmHandler alarmHandler = new AlarmHandler(context);
        alarmHandler.cancelAlarmManager();

        Log.d("WIDGET","Widget removed!");
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        updateAppWidget(context, appWidgetManager, appWidgetId);
    }
}
