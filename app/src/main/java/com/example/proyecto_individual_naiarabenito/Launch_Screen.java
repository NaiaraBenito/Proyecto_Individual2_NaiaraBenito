
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


/* ###################################### CLASE LAUNCH_SCREEN ######################################
    *) Descripción:
        La función de esta clase es mostrar una animación al abrir la aplicación donde se muestra el
        logo y el nombre de la autora de la aplicación.

    *) Tipo: Activity
*/
public class Launch_Screen extends AppCompatActivity {

// ___________________________________________ Variables ___________________________________________
    private ImageView circulo;      // ImageView con la imagen circulo_rosa.png
    private ImageView mancha;       // ImageView con la imagen mancha.png
    private ImageView logo;         // ImageView con la imagen logo.png
    private TextView hecho_por;     // TextView que contiene el mensaje "Hecho por:"
    private TextView autora;        // TextView que contiene el mensaje "Naiara Benito Balbás"

    private Animation animacion1;   // Animación que realiza un desplazamiento ascendente
    private Animation animacion2;   // Animación que realiza un desplazamiento descendente

// ____________________________________________ Métodos ____________________________________________

/*  Método onCreate:
    ----------------
        *) Parámetros (Input):
                1) (Bundle) savedInstanceState: Contiene el diseño predeterminado del Activity.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método se ejecuta la primera vez que se crea el Activity.
                Crea la vista y el funcionamiento de la animación que aparece al entrar en la
                aplicación.
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Cargar las preferencias configuradas por el usuario
        cargar_configuracion();

        // Crear la vista
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);

        // Cargar las animaciones
        cargarAnimacion();
        cargarLogin();
    }

// _________________________________________________________________________________________________

/*  Método cargarAnimacion:
    -----------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método añade las animaciones a la vista.
*/
    private void cargarAnimacion(){

        // Cargar animaciones
        animacion1 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba);
        animacion2 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_abajo);

        // Obtener los objetos de la vista a los se les añadirán las animaciones
        circulo = findViewById(R.id.cir_rosa);
        mancha = findViewById(R.id.mancha_rosa);
        logo = findViewById(R.id.logo_anim);
        hecho_por = findViewById(R.id.de_anim);
        autora = findViewById(R.id.autora_anim);

        // Asignar animaciones a cada Objeto de la vista
        circulo.setAnimation(animacion1);
        mancha.setAnimation(animacion1);
        logo.setAnimation(animacion2);
        hecho_por.setAnimation(animacion2);
        autora.setAnimation(animacion2);
    }

// _________________________________________________________________________________________________

/*  Método cargarLogin:
    -------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método realiza el cambio Activity a la pantalla del Login con un retraso de 5s
                para darle tiempo de cargar a las animaciones.
*/
    private void cargarLogin(){
        new Handler().postDelayed(() -> {
            // Crear el intent para pasar al Activity del Login
            Intent intent = new Intent(Launch_Screen.this, Login.class);

            // Conectar el este Activity (Launch Screen) con el Login mediante una animación
            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair<View, String>(logo, "logoImageTrans");
            pairs[1] = new Pair<View, String>(autora, "textTrans");

            /* Comprobar que la versión del dispositivo sea igual o superior a Lollipop, ya que
            las transiciones solo sirven con dichas versiones */
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){ // Si soporta animaciones
                // Cargar el Login con una animación
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Launch_Screen.this, pairs);
                startActivity(intent, options.toBundle());
                finish();
            } else {    // Si no soporta animaciones
                // Cargar el Login sin animaciones
                startActivity(intent);
                finish();
            }
        }, 5000); // Esperar 5s
    }

// _________________________________________________________________________________________________

/*  Método cargar_configuracion:
    ----------------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método carga las preferencias configuradas por el usuario (modo oscuro,
                orientación de la pantalla...).
*/
    private void cargar_configuracion(){

        // Obtener las preferencias configuradas por el usuario
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        // Comprobar el estado de la preferencia del modo oscuro
        boolean modoOscuro = sp.getBoolean("modo_oscuro", false);

        if(modoOscuro){     // Si el modo oscuro está activado: Pintar el fondo de gris
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.gris_claro));
        } else{             // Si el modo oscuro está desactivado: Pintar el fondo de blanco
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.white));
        }

        // Comprobar el estado de la preferencia de la orientación
        String ori = sp.getString("orientacion","false");
        switch (ori) {
            case "1":     // Si la orientación es 1: Desbloquear el giro automático de la app
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                break;
            case "2":     // Si la orientación es 2: Bloquear la orientacion vertical
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case "3":     // Si la orientación es 3: Bloquear la orientacion horizontal
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }
}