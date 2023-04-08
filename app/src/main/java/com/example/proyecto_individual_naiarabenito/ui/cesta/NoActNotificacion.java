
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.ui.cesta;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import static com.example.proyecto_individual_naiarabenito.ui.cesta.CestaFragment.NOTIFICACION_ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import android.content.Intent;
import android.os.Bundle;

import com.example.proyecto_individual_naiarabenito.activities.GestorIdioma;
import com.example.proyecto_individual_naiarabenito.activities.Menu_Principal;
import com.example.proyecto_individual_naiarabenito.R;


/* ################################### CLASE NO_ACT_NOTIFICACION ##################################
    *) Descripción:
        La función de esta clase es gestionar el proceso de haber pulsado No en la notificación
        que aparece al terminar un pedido y pregunta por la factura.

    *) Tipo: Activity
*/
public class NoActNotificacion extends AppCompatActivity {

// ___________________________________________ Variables ___________________________________________
    private String idioma;          // String que contiene el idioma actual de la aplicación

// ____________________________________________ Métodos ____________________________________________

/*  Método onCreate:
    ----------------
        *) Parámetros (Input):
                1) (Bundle) savedInstanceState: Contiene el diseño predeterminado del Activity.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta la primera vez que se crea el Activity.
                Elimina la notificación y devuelve la ejecución al Menú Principal.
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Obtener el idioma de la aplicación del Bundle (mantener idioma al girar la pantalla)
        if (savedInstanceState != null) {
            idioma = savedInstanceState.getString("idioma");
        }

        // Obtener el idioma de la aplicación del intent (mantener idioma al moverse por la aplicación)
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            idioma = (String) extras.get("idioma");
        }

        // Si en la anterior ejecución se ha guardado el idioma
        if (idioma != null){
            // Instanciar el Gestor de idiomas
            GestorIdioma gI = new GestorIdioma();

            // Asignar el idioma a la pantalla actual
            gI.cambiarIdioma(getBaseContext(), idioma);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_act_notificacion);

        if(extras != null){
            // Obtener los datos del usuario para mantener la sesión
            String[] datosUser = new String[4];
            datosUser[0] = getIntent().getExtras().getString("nombreUsuario");
            datosUser[1] = getIntent().getExtras().getString("apellidoUsuario");
            datosUser[2] = getIntent().getExtras().getString("emailUsuario");
            datosUser[3] = getIntent().getExtras().getString("fotoUsuario");

            // Crear el intent que redirige la ejecución al Menú Principal
            Intent intent = new Intent(this, Menu_Principal.class);

            // Pasarle la información del usuario (mantener la sesión)
            intent.putExtra("nombreUsuario", datosUser[0]);
            intent.putExtra("apellidoUsuario", datosUser[1]);
            intent.putExtra("emailUsuario", datosUser[2]);
            intent.putExtra("fotoUsuario", datosUser[3]);

            // Enviar el idioma actual
            intent.putExtra("idioma",idioma);

            // Crear un manager de notificaciones
            NotificationManagerCompat nmc = NotificationManagerCompat.from(getApplicationContext());

            // Eliminar la notificación pasándole el NOTIFICATION_ID
            nmc.cancel(NOTIFICACION_ID);

            // Evitar que se llene la pila de Actividades (Menú Principal solo tiene una instancia)
            onNewIntent(intent);
            finish();
        }
    }

// _________________________________________________________________________________________________

/*  Método onSaveInstanceState:
    ------------------------
        *) Parámetros (Input):
                1) (Bundle) outState: Contiene el diseño predeterminado del Activity.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta antes de eliminar la actividad. Guarda el idioma actual en el
                Bundle, para que al refrescar la actividad se mantenga.
*/
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Guardar en el Bundle el idioma actual de la aplicación
        outState.putString("idioma",idioma);
    }
}