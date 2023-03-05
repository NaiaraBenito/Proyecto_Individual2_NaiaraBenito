package com.example.proyecto_individual_naiarabenito;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.proyecto_individual_naiarabenito.ui.inicio.InicioFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.proyecto_individual_naiarabenito.databinding.ActivityMenuPrincipalBinding;

public class Menu_Principal extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;
    private ActivityMenuPrincipalBinding binding;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cargar_configuracion();
        super.onCreate(savedInstanceState);
        binding = ActivityMenuPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_inicio, R.id.navigation_cesta, R.id.navigation_perfil, R.id.navigation_configuracion)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_menu_principal);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

    public void llamada(View v){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
        } else{
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:943466484")));
        }
    }

    private void cargar_configuracion(){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        boolean modoOscuro = sp.getBoolean("modo_oscuro", false);

        if(modoOscuro){

            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.gris_claro));
        } else{
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.white));
        }

        String ori = sp.getString("orientacion","1");
        if("1".equals(ori)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else if("2".equals(ori)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if("3".equals(ori)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
}