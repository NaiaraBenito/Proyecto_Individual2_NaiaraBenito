package com.example.proyecto_individual_naiarabenito.ui.perfil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyecto_individual_naiarabenito.Login;
import com.example.proyecto_individual_naiarabenito.R;
import com.example.proyecto_individual_naiarabenito.Registro;
import com.example.proyecto_individual_naiarabenito.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment {

    String[] datosUser;
    Button btn_cerrarSesion;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        cargar_configuracion(view);

        datosUser = new String[3];
        datosUser[0] = getActivity().getIntent().getExtras().getString("nombreUsuario");
        datosUser[1] = getActivity().getIntent().getExtras().getString("apellidoUsuario");
        datosUser[2] = getActivity().getIntent().getExtras().getString("emailUsuario");

        btn_cerrarSesion = (Button) view.findViewById(R.id.btn_logout);
        btn_cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion(view);
            }
        });

        return view;
    }

    public void cerrarSesion(View v){

        // Crear un intent para pasar a la Actividad Registro
        Intent intent = new Intent(getContext(), Login.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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