package com.example.proyecto_individual_naiarabenito;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Launch_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cargar_configuracion();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);

        // Agregar animaciones
        Animation animacion1 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba);
        Animation animacion2 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_abajo);

        // Obtener los objetos de la vista
        ImageView cir = (ImageView) findViewById(R.id.cir_rosa);
        ImageView manch = (ImageView) findViewById(R.id.mancha_rosa);
        ImageView logo = (ImageView) findViewById(R.id.logo_anim);
        TextView de = (TextView) findViewById(R.id.de_anim);
        TextView aut = (TextView) findViewById(R.id.autora_anim);

        // Asignar animaciones a cada Objeto
        cir.setAnimation(animacion1);
        manch.setAnimation(animacion1);
        logo.setAnimation(animacion2);
        de.setAnimation(animacion2);
        aut.setAnimation(animacion2);

        // Método que tras un retraso de 5s cambiará a la actividad del Login con una animación
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Crear el intent para pasar a la actividad del Login
                Intent intent = new Intent(Launch_Screen.this, Login.class);

                // Hacer el arreglo para que se conecten el Launch Screen con el Login mediante una animación
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(logo, "logoImageTrans");
                pairs[1] = new Pair<View, String>(aut, "textTrans");

                // Las transiciones solo sirven con versiones igual o superiores a Lollipop
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    // Cargar el Login con una animación
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Launch_Screen.this, pairs);
                    startActivity(intent, options.toBundle());
                    finish();
                } else {
                    // Si tiene una versión inferior a Lollipop cargar el Login sin animaciones
                    startActivity(intent);
                    finish();
                }
            }
        }, 5000); // Esperar 5s
    }

    private void cargar_configuracion(){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        boolean modoOscuro = sp.getBoolean("modo_oscuro", false);

        if(modoOscuro){

            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.black));
        } else{
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.white));
        }

        String ori = sp.getString("orientacion","false");
        if("1".equals(ori)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else if("2".equals(ori)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if("3".equals(ori)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
}