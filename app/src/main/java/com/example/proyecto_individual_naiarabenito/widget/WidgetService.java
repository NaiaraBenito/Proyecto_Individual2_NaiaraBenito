
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.widget;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/* ###################################### CLASE WIDGET SERVICE #####################################
    *) Descripción:
        La función de esta clase es gestionar la actualización del Widget. Gestiona la lógica de la
        cuenta atrás del Widget.

    *) Tipo: BroadcastReceiver
*/
public class WidgetService extends BroadcastReceiver {

// ____________________________________________ Métodos ____________________________________________

/*  Método onReceive:
    -----------------
        *) Parámetros (Input):
                1) (Context) context: Contiene el contexto de la clase ejecuta el método.
                2) (Intent) intent: Contiene el intent a la clase que ha llamado al método.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método actualiza el Widget de la aplicación.
*/
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

// _________________________________________________________________________________________________

/*  Método cuentaAtras:
    -------------------
        *) Parámetros (Input):
                1) (Context) context: Contiene el contexto de la clase ejecuta el método.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método actualiza el valor de la cuenta atrás del Widget.
*/
    private void cuentaAtras(Context context) {
        // Obtener el valor actual del texto del Widget almacenado en las preferencias
        SharedPreferences preferences = context.getSharedPreferences("PREFS",0);
        int dias = preferences.getInt("dias",0);
        int horas = preferences.getInt("horas",0);
        int minutos = preferences.getInt("minutos",3);
        int segundos = preferences.getInt("segundos",0);

        // Decrementar 5s a la cuenta atrás (Porque el Widget se actualiza cada 5s)
        if(segundos <= 0){
            segundos = 55;
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

        // Volver a guardar el valor actualizado de la cuenta atrás en las preferencias
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("dias",dias);
        editor.putInt("horas",horas);
        editor.putInt("minutos",minutos);
        editor.putInt("segundos",segundos);
        editor.apply();
    }
}
