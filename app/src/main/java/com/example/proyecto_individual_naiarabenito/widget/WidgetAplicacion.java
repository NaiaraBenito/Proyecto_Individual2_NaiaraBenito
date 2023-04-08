
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.widget;


// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import com.example.proyecto_individual_naiarabenito.activities.Launch_Screen;
import com.example.proyecto_individual_naiarabenito.R;


/* ################################### CLASE WIDGET APLICACION #####################################
    *) Descripción:
        Esta clase gestiona el Widget.

    *) Tipo: AppWidgetProvider
*/
public class WidgetAplicacion extends AppWidgetProvider {

// ____________________________________________ Métodos ____________________________________________

/*  Método updateAppWidget:
    -----------------------
        *) Parámetros (Input):
                1) (Context) context: Contiene el contexto de la clase ejecuta el método.
                2) (AppWidgetManager) appWidgetManager: Contiene el WidgetManager del Widget de la
                   aplicación.
                3) (int) appWidgetId: Contiene el id del Widget de la aplicación.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método actualiza el Widget de la aplicación.
*/
    void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){

        // Crear un intent que redirecciona la ejecución al Launch Screen para cuando se pulse el
        // Widget
        Intent i = new Intent(context, Launch_Screen.class);
        // Crear un pendingIntent que se quede a la espera de que se pulse el Widget
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i,0);

        // Obtener las vistas del Widget
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_diseno);

        // Obtener el valor del texto actualizable del Widget
        SharedPreferences preferences = context.getSharedPreferences("PREFS",0);
        int dias = preferences.getInt("dias",0);
        int horas = preferences.getInt("horas",0);
        int minutos = preferences.getInt("minutos",3);
        int segundos = preferences.getInt("segundos",0);

        // Asignar el intent para cuando se pulse la imagen del Widget
        views.setOnClickPendingIntent(R.id.w_widgetImage,pendingIntent);

        // Actualizar el valor del texto actualizable del Widget
        views.setTextViewText(R.id.w_widgetText, "FIN DE LA PROMOCIÓN:\n" + dias + "d "+ horas + "h " + minutos + "m " + segundos + "s");

        // Actualizar el Widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

        // Reiniciar el refresco del Widget
        AlarmHandler alarmHandler = new AlarmHandler(context);
        alarmHandler.cancelAlarmManager();
        alarmHandler.setAlarmManager();

        Log.d("WIDGET","Widget updated!");
    }

// _________________________________________________________________________________________________

/*  Método onUpdate:
    ----------------
        *) Parámetros (Input):
                1) (Context) context: Contiene el contexto de la clase ejecuta el método.
                2) (AppWidgetManager) appWidgetManager: Contiene el WidgetManager del Widget de la
                   aplicación.
                3) (int) appWidgetId: Contiene el id del Widget de la aplicación.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta cuando se tiene que actualizar el Widget.
*/
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Recorrer todos los Widgets de la aplicación
        for(int appWidgetId: appWidgetIds){
            // Llamar al método que actualiza el Widget
            updateAppWidget(context,appWidgetManager,appWidgetId);
        }
    }

// _________________________________________________________________________________________________

/*  Método onDisabled:
    ------------------
        *) Parámetros (Input):
                1) (Context) context: Contiene el contexto de la clase ejecuta el método.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta cuando se elimina el Widget.
*/
    @Override
    public void onDisabled(Context context) {
        // Detener la actualización del Widget
        AlarmHandler alarmHandler = new AlarmHandler(context);
        alarmHandler.cancelAlarmManager();

        Log.d("WIDGET","Widget removed!");
    }
}
