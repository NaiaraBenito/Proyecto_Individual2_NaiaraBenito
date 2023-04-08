
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.activities;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import android.content.Context;
import android.content.res.Configuration;
import java.util.Locale;


/* ################################ CLASE GESTOR_IDIOMA ###################################
    *) Descripción:
        La función de esta clase auxiliar es cambiar el idioma de la aplicación.

    *) Tipo: Class
*/
public class GestorIdioma {

// __________________________________________ Constructor __________________________________________
    public GestorIdioma() {}

// ____________________________________________ Métodos ____________________________________________

/*  Método cambiarIdioma:
    ---------------------
        *) Parámetros (Input):
                1) (Context) pContext: Contiene el contexto de la clase que utiliza el método.
                2) (String) pIdioma: Contiene el idioma al que se quiere traducir la aplicación.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta cada vez que se quiere establecer el idioma de la aplicación.
*/
    public void cambiarIdioma(Context pContext, String pIdioma){

        // Obtener la nueva localización
        Locale nuevaloc = new Locale(pIdioma);
        Locale.setDefault(nuevaloc);

        // Crear una configuración para la nueva localización
        Configuration configuration = pContext.getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);
        Context context = pContext.createConfigurationContext(configuration);

        // Actualizar todos los recursos de la aplicación
        pContext.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }
}
