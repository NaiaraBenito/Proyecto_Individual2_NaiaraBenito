package com.example.proyecto_individual_naiarabenito.ui.configuracion;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_individual_naiarabenito.Menu_Principal;
import com.example.proyecto_individual_naiarabenito.R;
import com.example.proyecto_individual_naiarabenito.ui.inicio.InicioFragment;


public class ConfiguracionFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configuracion, container, false);
        cargar_configuracion(view);
        Intent intent = new Intent(getContext(), ConfiguracionActivity.class);
        startActivity(intent);

        return view;
    }

    private void cargar_configuracion(View view){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        boolean modoOscuro = sp.getBoolean("modo_oscuro", false);
        if(modoOscuro){
            view.setBackgroundColor(getResources().getColor(R.color.black));
        } else{
            view.setBackgroundColor(getResources().getColor(R.color.white));
        }

        String ori = sp.getString("orientacion","false");
        if("1".equals(ori)){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else if("2".equals(ori)){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if("3".equals(ori)){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
}