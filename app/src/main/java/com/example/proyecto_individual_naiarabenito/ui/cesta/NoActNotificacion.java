
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.ui.cesta;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import static com.example.proyecto_individual_naiarabenito.ui.cesta.CestaFragment.NOTIFICACION_ID;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.os.Bundle;

import com.example.proyecto_individual_naiarabenito.Menu_Principal;
import com.example.proyecto_individual_naiarabenito.R;

public class NoActNotificacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_act_notificacion);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            // Obtener los datos del usuario para mantener la sesión
            String[] datosUser = new String[3];
            datosUser[0] = getIntent().getExtras().getString("nombreUsuario");
            datosUser[1] = getIntent().getExtras().getString("apellidoUsuario");
            datosUser[2] = getIntent().getExtras().getString("emailUsuario");
            // Crear el intent que redirige la ejecución al Menú Principal
            Intent intent = new Intent(this, Menu_Principal.class);

            // Pasarle la información del usuario (mantener la sesión)
            intent.putExtra("nombreUsuario", datosUser[0]);
            intent.putExtra("apellidoUsuario", datosUser[1]);
            intent.putExtra("emailUsuario", datosUser[2]);
            // Para hacer que la notificación desaparezca al ejecutar la actividad
            // Crear un manager de notificaciones
            NotificationManagerCompat nmc = NotificationManagerCompat.from(getApplicationContext());
            // Cancelar la notificación, pasándole el NOTIFICATION_ID
            nmc.cancel(NOTIFICACION_ID);

            // Evitar que se llene la pila de Actividades (Menú Principal solo tiene una instancia)
            onNewIntent(intent);
            finish();
        }
    }
}