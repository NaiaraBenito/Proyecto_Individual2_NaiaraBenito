
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.ui.perfil;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.proyecto_individual_naiarabenito.Login;
import com.example.proyecto_individual_naiarabenito.R;

import java.util.Locale;


/* ###################################### CLASE PERFIL_FRAGMENT ####################################
    *) Descripción:
        La función de esta clase es mostrar y gestionar la configuración del perfil de usuario.

    *) Tipo: Fragment
*/
public class PerfilFragment extends Fragment {

// ___________________________________________ Variables ___________________________________________
    private String[] datosUser; // Lista que contiene los datos del usuario para mantener la sesión
    private Button btn_cerrarSesion;    // Botón para cerrar la sesión
    private TextView tv_nombre;         // TextView que contiene el nombre del usuario
    private TextView tv_apellido;         // TextView que contiene el apellido del usuario
    private TextView tv_email;         // TextView que contiene el email del usuario

// ____________________________________________ Métodos ____________________________________________

/*  Método onCreateView:
    --------------------
        *) Parámetros (Input):
                1) (LayoutInflater) inflater: Se utiliza para enlazar el fragmento con su vista.
                2) (ViewGroup) container: Contiene la vista principal a la que se debe adjuntar la
                   vista del fragmento.
                3) (Bundle) savedInstanceState: Contiene el diseño predeterminado del Fragmento.
        *) Parámetro (Output):
                (View) view: Vista asociada al Fragment actual.
        *) Descripción:
                Este método se ejecuta cuando el usuario se ha logueado correctamente y accede al
                apartado Perfil del menú.
*/
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Obtener la vista
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Cargar las preferencias configuradas por el usuario
        cargar_configuracion(view);

        // Obtener los datos del usuario para mantener la sesión
        datosUser = new String[3];
        datosUser[0] = getActivity().getIntent().getExtras().getString("nombreUsuario");
        datosUser[1] = getActivity().getIntent().getExtras().getString("apellidoUsuario");
        datosUser[2] = getActivity().getIntent().getExtras().getString("emailUsuario");

        // Obtener los Objetos de la vista
        tv_nombre = view.findViewById(R.id.pf_nombre);
        tv_apellido = view.findViewById(R.id.pf_apellido);
        tv_email = view.findViewById(R.id.pf_email);
        btn_cerrarSesion = view.findViewById(R.id.btn_logout);

        // Asignar valor a los elementos
        tv_nombre.setText(datosUser[0]);
        tv_apellido.setText(datosUser[1]);
        tv_email.setText(datosUser[2]);

        // Asignar acción al pulsar el botón "CERRAR SESIÓN"
        btn_cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cerrar la sesión y volver al Login
                cerrarSesion();
            }
        });

        //----------------------
        Button bot_eng = view.findViewById(R.id.btn_en);
        // Cambiar idioma al Inglés --> Funcionamiento del botón English
        bot_eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Forzar la localización de la aplicación al inglés
                Locale nuevaloc = new Locale("en");
                Locale.setDefault(nuevaloc);

                // Crear una configuración para la localización inglesa
                Configuration configuration = getActivity().getBaseContext().getResources().getConfiguration();
                configuration.setLocale(nuevaloc);
                configuration.setLayoutDirection(nuevaloc);
                Context context = getActivity().getBaseContext().createConfigurationContext(configuration);

                // Actualizar todos los recursos de la aplicación
                getActivity().getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

                // Regargar de nuevo la actividad:
                //   - finish() -> Finalizar la instancia en curso
                //   - startActivity(getIntent()) -> Lanzar una nueva instancia
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
        });

        Button bot_esp = view.findViewById(R.id.btn_es);
        // Cambiar idioma al Inglés --> Funcionamiento del botón English
        bot_esp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Forzar la localización de la aplicación al inglés
                Locale nuevaloc = new Locale("es");
                Locale.setDefault(nuevaloc);

                // Crear una configuración para la localización inglesa
                Configuration configuration = getActivity().getBaseContext().getResources().getConfiguration();
                configuration.setLocale(nuevaloc);
                configuration.setLayoutDirection(nuevaloc);
                Context context = getActivity().getBaseContext().createConfigurationContext(configuration);

                // Actualizar todos los recursos de la aplicación
                getActivity().getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

                // Regargar de nuevo la actividad:
                //   - finish() -> Finalizar la instancia en curso
                //   - startActivity(getIntent()) -> Lanzar una nueva instancia
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
        });
        //----------------------
        return view;
    }

// _________________________________________________________________________________________________

/*  Método cerrarSesion:
    --------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta cuando el usuario pulsa el botón "Cerrar Sesión". Redirige al
                usuario al Login sin guardar sus datos.
*/
    private void cerrarSesion(){
        // Crear el intent que redirige la ejecución al Login
        Intent intent = new Intent(getContext(), Login.class);
        startActivity(intent);
        getActivity().finish();
    }

// _________________________________________________________________________________________________

    /*  Método onDestroyView:
        ---------------------
            *) Parámetos (Input):
            *) Parámetro (Output):
                    void
            *) Descripción:
                    Este método se ejecuta cuando se cierra el fragmento y borra la vista
    */
    @Override
    public void onDestroyView() {
        // Borrar la vista
        super.onDestroyView();
    }

// _________________________________________________________________________________________________

/*  Método cargar_configuracion:
    ----------------------------
        *) Parámetros (Input):
                (View) view: Vista asociada al fragmento.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método carga las preferencias configuradas por el usuario (modo oscuro,
                orientación de la pantalla...).
*/
    private void cargar_configuracion(View view){

        // Obtener las preferencias configuradas por el usuario
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        // Comprobar el estado de la preferencia del modo oscuro
        boolean modoOscuro = sp.getBoolean("modo_oscuro", false);

        if(modoOscuro){         // Si el modo oscuro está activado: Pintar el fondo de negro
            view.setBackgroundColor(getResources().getColor(R.color.black));
        } else{                 // Si el modo oscuro está desactivado: Pintar el fondo de blanco
            view.setBackgroundColor(getResources().getColor(R.color.white));
        }

        // Comprobar el estado de la preferencia de la orientación
        String ori = sp.getString("orientacion","false");
        switch (ori) {
            case "1": // Si la orientación es 1: Desbloquear el giro automático de la app
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                break;
            case "2": // Si la orientación es 2: Bloquear la orientacion vertical
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case "3": // Si la orientación es 3: Bloquear la orientacion horizontal
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }
}