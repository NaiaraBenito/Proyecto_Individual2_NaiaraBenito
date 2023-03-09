
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.ui.configuracion;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import com.example.proyecto_individual_naiarabenito.R;


/* ################################ CLASE CONFIGURACION_ACTIVITY ###################################
    *) Descripción:
        La función de esta clase es mostrar y gestionar las preferencias de la aplicación.

    *) Tipo: Preference Activity
*/
public class PreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

// ____________________________________________ Métodos ____________________________________________

/*  Método onCreate:
    ----------------
        *) Parámetros (Input):
                1) (Bundle) savedInstanceState: Contiene el diseño predeterminado del Activity.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta la primera vez que se crea el Activity.
                Crea la vista y el funcionamiento de las preferencias de la aplicación.
*/
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        ListPreference orientacion = (ListPreference) findPreference("orientacion");
        orientacion.setSummary(orientacion.getEntry());
    }

// _________________________________________________________________________________________________

/*  Método onResume:
    ----------------
        *) Parámetos (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta cuando se realicen cambios en las preferencias de la
                aplicación.
*/
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        System.out.println("ENTRA AQUI");
        if(key.equals("modo_oscuro")){

            boolean switchMode = sharedPreferences.getBoolean("modo_oscuro",false);
            if(switchMode){
                getListView().setBackgroundColor(getResources().getColor(R.color.gris_claro));
            } else {
                getListView().setBackgroundColor(getResources().getColor(R.color.white));
            }
        } else if (key.equals("orientacion")) {
            ListPreference orientacion = (ListPreference) findPreference("orientacion");

            String ori = sharedPreferences.getString("orientacion","false");
            switch (ori) {
                case "1":  // Si la orientación es 1: Desbloquear el giro automático de la app
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    orientacion.setSummary(orientacion.getEntry());
                    break;
                case "2":  // Si la orientación es 2: Bloquear la orientacion vertical
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    orientacion.setSummary(orientacion.getEntry());
                    break;
                case "3":  // Si la orientación es 3: Bloquear la orientacion horizontal
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    orientacion.setSummary(orientacion.getEntry());
                    break;
            }
        }

    }
}
