
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.widget;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import android.content.Context;
import android.os.PowerManager;
import android.util.Log;


/* ###################################### CLASE WAKE LOCKER ########################################
    *) Descripción:
        Esta clase gestiona el bloqueo del Widget.

    *) Tipo: Class
*/
public abstract class WakeLocker {

// ___________________________________________ Variables ___________________________________________
    private static PowerManager.WakeLock wakeLock;  // Variable que contiene el bloqueador

// ____________________________________________ Métodos ____________________________________________

/*  Método acquire:
    ---------------
        *) Parámetros (Input):
                1) (Context) context: Contiene el contexto de la clase ejecuta el método.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método quita el bloqueo del dispositivo, lo "despierta".
*/
    public static void acquire(Context context){
        // Comprobar que el WakeLock no sea nulo
        if(wakeLock != null){   // Si es nulo --> Mantiene dormido el dispositivo
            // Si es nulo llama al método que mantiene el dispositivo dormido
            wakeLock.release();
        }

        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "WIDGET: Wake lock acquired!");

        // Volver a llamar a este método con un retraso de 2s
        wakeLock.acquire(2000);
    }

// ____________________________________________ Métodos ____________________________________________

/*  Método release:
    ---------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método libera la variable que contiene el bloqueador
*/
    public static void release(){
        // Comprobar si es nulo
        if(wakeLock != null){ // Si no es nulo --> llamarse a sí mismo de nuevo
            wakeLock.release();
        }
        // Poner a null el bloqueadro
        wakeLock = null;
    }
}
